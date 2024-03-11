# food_recommend

#### 介绍
基于ALS协同过滤算法的大数据菜品推荐系统，
其中由Spark MLlib实现离线推荐，由Flume接收实时日志信息作为生产者，发送至Kafka消息队列，然后由Spark Streaming消费、使得推荐模型更新而实现实时推荐；最后将推荐信息持久到Hbase中，并在前端由Thymleaf实现可视化。





#### 系统结构
![输入图片说明](%E6%8E%A8%E8%8D%90%E6%B5%81%E7%A8%8B%E5%9B%BE.png)

#### 业务逻辑实现
![输入图片说明](%E4%B8%9A%E5%8A%A1%E9%80%BB%E8%BE%91%E5%AE%9E%E7%8E%B0.png)





