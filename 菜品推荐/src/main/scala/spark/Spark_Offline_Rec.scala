package spark

import dao.HBaseDAO
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.{ALS, Rating}

object Spark_Offline_Rec {
  def carry(){

    println("离线引擎启动")
    // TODO... 离线初始化

    //设置日志提示等级
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    val sparkConf = new SparkConf().setAppName("Spark_Offline_Rec").setMaster("local[*]").set("spark.akka.frameSize", "2000").set("spark.network.timeout", "1200")
    val sparkContext1 = new SparkContext(sparkConf)



    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "192.168.88.128")
    hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
    hbaseConf.set("zookeeper.session.timeout", "6000000")

    println("\n=====================step 2 load data==========================")
    //加载HBase中的数据

    //读取数据并转化成rdd
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "rating")
    val ratingsData = sparkContext1.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat],
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


    //第一次运行，初始化用户的推荐信息

    println("\n=====================system initiallizing...==========================")
    println("\n[DEBUG]training model...")
    val firstTrainTime = System.nanoTime()

    val model = ALS.train(hbaseRatings, rank, numIter, lambda)

    val firstTrainEndTime = System.nanoTime() - firstTrainTime
    println("[DEBUG]first training consuming:" + firstTrainEndTime / 1000000000 + "s")
    println("\n[DEBUG]save recommended data to hbase...")
    val firstPutTime = System.nanoTime()


    //为每一个用户产生初始的推荐食物，取top10

    for (i <- 1 to 20) {
      val topRatings = model.recommendProducts(i, 10)
      var recFoods = ""
      for (r <- topRatings) {
        val rat=r.rating.toString
        recFoods += r.product+ ","
      }
      HBaseDAO.put("user", i.toString, "info", "recFoods", recFoods.substring(0, recFoods.length - 1))
    }
    val firstPutEndTime = System.nanoTime() - firstPutTime
    println("[DEBUG]finish job consuming:" + firstPutEndTime / 1000000000 + "s")
    sparkContext1.stop()
  }


}
