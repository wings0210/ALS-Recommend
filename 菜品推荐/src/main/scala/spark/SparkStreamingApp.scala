package spark



import dao.HBaseDAO
import org.apache.commons.codec.StringDecoder
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD.numericRDDToDoubleRDDFunctions
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
//import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable





object SparkStreamingApp {
  def main(args: Array[String]): Unit = {




    println("离线引擎启动")
    // TODO... 离线初始化

    //设置日志提示等级
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    val sparkConf = new SparkConf().setAppName("SparkStreamingApp").setMaster("local[*]").set("spark.akka.frameSize", "2000").set("spark.network.timeout", "1200")
    val sparkContext = new SparkContext(sparkConf)



    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "192.168.88.128")
    hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
    hbaseConf.set("zookeeper.session.timeout", "6000000")

    println("\n=====================加载HBase中数据==========================")


    //读取数据并转化成rdd
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "rating")
    val ratingsData = sparkContext.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result])

    val hbaseRatings = ratingsData.map { case (_, res) =>
      val foodId = Bytes.toString(res.getValue(Bytes.toBytes("info"), Bytes.toBytes("fid")))
      val rating = Bytes.toString(res.getValue(Bytes.toBytes("info"), Bytes.toBytes("rating")))
      val userId = Bytes.toString(res.getValue(Bytes.toBytes("info"), Bytes.toBytes("uid")))
      Rating(userId.toInt, foodId.toInt, rating.toDouble)
    }.cache()




   val numTrainRatings = hbaseRatings.count()
    println(s"[DEBUG]get $numTrainRatings train data from hbase")
    val rank = 10
    val lambda = 0.01
    val numIter = 10




    println("实时引擎启动")


/*
    args为 bogon:2181 test streamingtopic 1
    val Array(zkQuorum, group, topics, numThreads) = args
    val zkQuorum="192.168.88.128:2181"
    val topics="topic1"
    val numThreads=1
    val group="test1"

    println("\n=====================start real-time recommendation engine...==========================")
    val streamingTime = 120
    println(s"[DEBUG]The time interval to refresh model is: $streamingTime s")

    //接受实时的用户行为数据
    //    val streamingContext = new StreamingContext(sparkContext, Seconds(streamingTime))
    //    val ssc = new StreamingContext(sparkContext, Seconds(60))


    val ssc = new StreamingContext(sparkContext, Seconds(10))
    // 将topics转换成topic-->numThreads的哈稀表
    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap


    // 创建连接Kafka的消费者链接
    val logs = KafkaUtils.createStream(ssc, zkQuorum, group,topicMap).map(_._2)
    val ssc = new StreamingContext(sparkContext, Seconds(60))
    val zkQuorum="192.168.88.128:2181"
    val groupId="topic1-test"

    //设置对应topic
    val topics = Map("topic1"->1)
   val log= KafkaUtils.createStream(ssc, zkQuorum, groupId, topics)

 */

val ssc = new StreamingContext(sparkContext, Seconds(60))
    ssc.checkpoint("c://checkpoint")
    val kafkaParams = Map[String, String](
      "bootstrap.servers" -> "192.168.88.128:9092",
      "group.id" -> "topic1-test",
      //此时我们相当于在消费数据，指定反序列数据的方式,实现org.apache.kafka.common.serialization.Deserializer的类
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer")
    val topic = Set("topic1")


/*
    val log: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topic, kafkaParams))
 */

val log = KafkaUtils.createDirectStream[String, String](
  ssc,
  PreferConsistent,
  Subscribe[String, String](topic, kafkaParams)
)





   val logs=log.map(_.value())

    val cleanData = logs.map(line => {
      val infos = line.split("::")
      Rating(infos(0).toInt, infos(1).toInt, infos(2).toDouble)

    })








    var allData = hbaseRatings
    allData.cache.count()
    hbaseRatings.unpersist()
    var index = 0

    cleanData.foreachRDD{ rdd =>
      rdd.persist(StorageLevel.DISK_ONLY)
      index += 1
      println("\n[DEBUG]this round (" + index + ") received: " + rdd.count() + " data lines.")
      val refreshStartTime = System.nanoTime()

      val tmpData = allData.union(rdd).cache()


      allData = tmpData
      tmpData.unpersist()

      allData = allData.union(rdd).repartition(10).cache()
      val model = ALS.train(allData, rank, numIter, lambda)

      val refreshEndTime = System.nanoTime() - refreshStartTime
      println("[DEBUG]training consuming:" + refreshEndTime / 1000000000 + " s")
   println("[DEBUG]begin refresh hbase user's recFoods...")
      val refreshAgainStartTime = System.nanoTime()

      //再更新当前有行为产生的用户的推荐数据(收集到的日志用户）
      val usersId = rdd.map(_.user).distinct().collect()
      for (u <- usersId) {
        val topRatings = model.recommendProducts(u, 10)
        var recFoods = ""
        for (r <- topRatings) {


          val rat = r.rating.toString
          recFoods += r.product + ","
        }
        HBaseDAO.put("user", u.toString, "info", "recFoods", recFoods.substring(0, recFoods.length - 1))
      }
      val refreshAgainConsumingTime = System.nanoTime() - refreshAgainStartTime
      println("[DEBUG]finish refresh job,consuming:" + refreshAgainConsumingTime / 1000000000 + " s")
      allData.unpersist();
    }


    ssc.start()
    ssc.awaitTermination()
    sparkContext.stop()



  }

}


