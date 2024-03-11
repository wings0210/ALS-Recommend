package dao

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}


object UserToHBase {
  def main(args: Array[String]) {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("SparkStreamingApp").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)

    //初始化users表
    for (i <- 1 to 20) {
      val uid = i.toString
      val pwd = "123456"
      HBaseDAO.put("user", i.toString, "info", "uid", uid)
      HBaseDAO.put("user", i.toString, "info", "pwd", pwd)
    }
    sparkContext.stop()
  }
}
