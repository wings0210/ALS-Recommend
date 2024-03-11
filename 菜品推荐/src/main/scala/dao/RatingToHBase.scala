package dao

import domain.RatingInfo
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 初始化 'users' 'foods' 'ratings' 三张表
  */
object RatingToHBase {
  def main(args: Array[String]) {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("SparkStreamingApp").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)

    //初始化ratings表
    val rating = sparkContext.textFile("hdfs://192.168.88.128:9000/input/rating").map { lines =>
      val fields = lines.split("::")
      new RatingInfo(fields(0), fields(1), fields(2))
    }.cache()

    var row = 1
    rating.foreach{ rating=>
      HBaseDAO.put("rating", row.toString, "info", "fid", rating.fid)
      HBaseDAO.put("rating", row.toString, "info", "uid", rating.uid)
      HBaseDAO.put("rating", row.toString, "info", "rating", rating.rating)
      row=row+1
    }
    sparkContext.stop()
  }
}
