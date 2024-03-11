package com.example.food_rec.util;



/**
 * HBase操作工具类：Java工具类建议采用单例模式封装
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseUtils {
    HBaseAdmin admin = null;
    Configuration configuration = null;
    /**
     * 私有构造方法
     */
    private HBaseUtils(){
        configuration = new Configuration();
        //设置zookeeper的访问地址
        configuration.set("hbase.zookeeper.quorum", "192.168.88.128:2181");
        //设置hbase的root路径hbase.rootdir
        configuration.set("hbase.rootdir", "hdfs://192.168.88.128:9000/hbase");
        try {
            admin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static HBaseUtils instance = null;
    public  static synchronized HBaseUtils getInstance() {
        if(null == instance) {
            instance = new HBaseUtils();
        }
        return instance;
    }
    /**
     * 根据表名获取到HTable实例
     */
    public HTable getTable(String tableName) {
        HTable table = null;
        try {
            table = new HTable(configuration, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }
    /**
     * 添加一条记录到HBase表
     * @param tableName HBase表名
     * @param rowkey  HBase表的rowkey
     * @param cf HBase表的columnfamily
     * @param column HBase表的列
     * @param value  写入HBase表的值
     */
    public void put(String tableName, String rowkey, String cf, String column, String value) {
        HTable table = getTable(tableName);
        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
