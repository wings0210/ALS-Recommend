package dao

import com.example.food_rec.util.HBaseUtils

import org.apache.hadoop.hbase.client.{Put, Scan}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ArrayBuffer


object HBaseDAO {

  //写入
  def put(tableName: String, rowKey: String, family: String, qualifier: String, value: String) {
    val table = HBaseUtils.getInstance().getTable(tableName)
    val put = new Put(Bytes.toBytes(rowKey))

    put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value))
    table.put(put)
  }

  //获得所有行健
  def getAllRow(tableName: String): Array[String] = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    val resultScaner = table.getScanner(new Scan())
    val resIter = resultScaner.iterator()
    var resArr = new ArrayBuffer[String]()
    while (resIter.hasNext) {
      val res = resIter.next()
      if (res != null && !res.isEmpty) {
        resArr += Bytes.toString(res.getRow)
      }
    }
    resArr.toArray
  }

}
