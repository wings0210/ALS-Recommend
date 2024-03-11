#coding=UTF-8
#产生实时日志，上传至虚拟机封装为定时脚本，然后启动crontab
import random

#食物id
food_id = [1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,
           1011,1012,1013,1014,1015,1016,1017,1018,1019,1020,
           1021,1022,1023,1024,1025,1026,1027,1028,1029,1030]

#用户id
user_id = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]

#用户对食物的评价
food_rating = [1,2,3,4,5]


#sample(seq, n)从序列seq中选择n个随机且独立的元素
#随机生成一个food
def sample_food():
    return random.sample(food_id, 1)[0]

#随机生成一个用户
def sample_user():
    return random.sample(user_id, 1)[0]

#随机生成一个食物评价
def sample_foodrating():
    return random.sample(food_rating, 1)[0]

def generate_dataset(count = 50):

    #打开rating文件,并赋予 f 写的权限
    #w+为消除文件内容，然后以读写方式打开文件。
    #a+为以读写方式打开文件，并把文件指针移到文件尾
    #为了将数据连续的写入文件采用 a+
    f = open("C:\\Users\\mine\\Desktop\\数据集\\access.log","a+")

    while count >=1:
        #query_log="{food}\t{user}\t{foodrating}".format(food=sample_food(),user=sample_user(),foodrating=sample_foodrating())
        query_log="{user}::{food}::{foodrating}".format(user=sample_user(),food=sample_food(),foodrating=sample_foodrating())
        #测试时输出到控制台，在实际使用中是将他存入相应的文件中
        print (query_log)

        #将日志输入到rating
        f.write(query_log + "\n")

        count = count - 1

if __name__ == '__main__':
    generate_dataset()