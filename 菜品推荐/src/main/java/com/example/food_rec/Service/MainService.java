package com.example.food_rec.Service;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;


public class MainService {

    /**
     * 根据用户名/id，查询离线推荐菜品结果
     * @param tableName：hbase中的user表
     * @param rowKey：主键
     * @param family：family
     * @param column：列字段
     * @return 返回推荐菜品的id数组
     * by Cai Yang
     * 2022.3.10
     */
    public static String[] get_id(String tableName,String rowKey,String family,String column){
        String res = "";                    //返回结果
        Table table = null;

        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family)           //判断输入值是否为空
                || StringUtils.isBlank(rowKey) || StringUtils.isBlank(column)) {
            return null;
        }

        try{
            Configuration configuration = new Configuration();
            configuration.set( "hbase.zookeeper.quorum","192.168.88.128:2181" );
            configuration.set("hbase.rootdir", "hdfs://192.168.88.128:9000/hbase");


            Connection connection = ConnectionFactory.createConnection( configuration ); // hbase链接
            table = connection.getTable( TableName.valueOf( tableName ) );
            Get g = new Get( rowKey.getBytes() );
            g.addColumn( family.getBytes(),column.getBytes() );
            Result result = table.get( g );
            List<Cell> ceList = result.listCells();
            if (ceList != null && ceList.size() > 0) {
                for (Cell cell : ceList) {
                    res = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                }
            }

            connection.close();



        }catch (Exception e){
            e.printStackTrace();
        }
        String[] a;
        a=res.split(",");
        return a;

    }

    /**
     * 综合查询{菜品图片url数组，推荐菜品名name数组}
     * @param c：接收联查，推荐菜品名的数组
     * @param tableName：菜品表food
     * @param family：菜品表family
     * @param column：菜品表查询字段-图片地址url
     * @param tableName1：用户表user
     * @param rowKey1：用户表主键
     * @param family1：用户表family
     * @param column1：用户表查询字段-推荐菜品recFoods
     * @param family2：菜品表family
     * @param column2：菜品表查询字段-菜品名name
     * @return 返回接收推荐菜品的图片数组
     * by Cai Yang
     * 2022.3.10
     */
    public static String[]  get_url(String[] c,
                                    String tableName,    String family,   String column,
                                    String tableName1,   String rowKey1,  String family1,   String column1,
                                                         String family2,  String column2)
    {
        String[] a=get_id(tableName1,rowKey1,family1,column1);
        String res1 = "";//返回结果
        String res2 = "";
        Table table1 = null;
        String[] b=new String[10];


        try{
            Configuration configuration = new Configuration();
            configuration.set( "hbase.zookeeper.quorum","192.168.88.128:2181" );
            configuration.set("hbase.rootdir", "hdfs://192.168.88.128:9000/hbase");


            Connection connection = ConnectionFactory.createConnection( configuration ); // hbase链接
            table1 = connection.getTable( TableName.valueOf( tableName ) );

            for(int i=0;i<10;i++) {
                assert a != null;
                Get g = new Get(a[i].getBytes());
                g.addColumn(family.getBytes(), column.getBytes());
                Result result = table1.get(g);
                List<Cell> ceList = result.listCells();
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        res1 = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());

                    }
                }

                b[i] = res1;
                //System.out.println(res1);
            }
            for(int i=0;i<10;i++) {

                Get g = new Get(a[i].getBytes());
                g.addColumn(family2.getBytes(), column2.getBytes());
                Result result = table1.get(g);
                List<Cell> ceList = result.listCells();
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        res2 = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());

                    }
                }

                c[i] = res2;
                //System.out.println(res1);
            }
            connection.close();



        }catch (Exception e){
            e.printStackTrace();
        }
        return b;
    }



    public static void main(String[] args) {
        /*
        String[] a=get_id("users","1","info","recFoods");
        for(int i=0;i<10;i++){
            assert a != null;
            System.out.println(a[i]);
        }
         */

        String[]c=new String[10];
        String[]b =get_url(c,"food","info","url",
                             "user","1","info","recFoods",
                                                           "info","name");

        for(int i=0;i<10;i++){
            assert b != null;
            System.out.println(b[i]);
        }



    }

}
