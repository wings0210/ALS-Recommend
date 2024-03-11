package dao

import domain.FoodInfo
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}


object FoodToHBase {
  def main(args: Array[String]) {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("SparkStreamingApp").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)

    //初始化foods表
    val food = sparkContext.textFile("hdfs://192.168.88.128:9000/input/food").map { lines =>
      val fields = lines.split("::")
      new FoodInfo(fields(0), fields(1),fields(2))
    }.cache()



    food.foreach { food =>
      HBaseDAO.put("food", food.fid, "info", "id", food.fid)
      HBaseDAO.put("food", food.fid, "info", "name", food.name)
      HBaseDAO.put("food", food.fid, "info", "url", food.f_url)
    }




    sparkContext.stop()
  }
}
