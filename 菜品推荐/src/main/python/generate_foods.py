


food_id = [1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,
           1011,1012,1013,1014,1015,1016,1017,1018,1019,1020,
           1021,1022,1023,1024,1025,1026,1027,1028,1029,1030]


food_name=["蛋炒饭","炒河粉","西红柿鸡蛋面","煎饺","蒸饺","水饺","辣子鸡","爆炒鸡蛋","蒜苔炒肉","铁锅鸡",
           "鱼香肉丝","素丸子汤","红烧肉","糖醋鲤鱼","巴沙螃蟹","红烧大虾","炒小黄鱼","清炖排骨","红烧草鱼","盐焗鸡",
           "豆干炒肉末","小炒青菜","素烧千张","烧莴苣","韭菜炒鸡蛋","红焖小龙虾","炒西兰花","鸡蛋羹","西洋汉堡","小鸡炖蘑菇"]

food_url=["/1.jpg","/2.jpg","/3.jpg","/4.jpg","/5.jpg","/6.jpg","/7.jpg","/8.jpg","/9.jpg","/10.jpg",
          "/11.jpg","/12.jpg","/13.jpg","/14.jpg","/15.jpg","/16.jpg","/17.jpg","/18.jpg","/19.jpg","/20.jpg",
          "/21.jpg","/22.jpg","/23.jpg","/24.jpg","/25.jpg","/26.jpg","/27.jpg","/28.jpg","/29.jpg","/30.jpg"]
def generate_foods(count = 30):

    #打开rating文件,并赋予 f 写的权限
    #w+为消除文件内容，然后以读写方式打开文件。
    #a+为以读写方式打开文件，并把文件指针移到文件尾
    #为了将数据连续的写入文件采用 a+
    #f = open("/abs/project/data/dataset/rating","w+")
    f = open("C:\\Users\\mine\\Desktop\\数据集\\food","a+",encoding="utf-8")
    i=0
    while count >=1:

        query_log="{foodid}::{foodname}::{foodurl}".format(foodid=food_id[i],foodname=food_name[i],foodurl=food_url[i])
        #测试时输出到控制台，在实际使用中是将他存入相应的文件中
        print (query_log)

        #将日志输入到rating
        f.write(query_log + "\n")
        i = i + 1
        count = count - 1

if __name__ == '__main__':
    generate_foods()