

import random
import time

#从什么搜索引擎跳转过来
http_referers = [
    "http://www.baidu.com/s?wd={query}",
    "https://www.google.com/search?q={query}",
    "https://www.sougou.com/web?query={query}",
    "http://cn.bing.com/search?q={query}",
    "https://search.yahoo.com/search?p={query}"
]

#搜索关键字
search_keyword = [
    "鸡排饭",
    "虾排饭",
    "芒果布丁",
    "海鲜自助",
    "水果拼盘",
    "黄焖鸡米饭",
    "西红柿牛腩面"
]

#网站链接路径
url_path = [
    "class/111.html",
    "class/112.html",
    "class/113.html",
    "class/114.html",
    "class/115.html",
    "class/116.html"
]

#ip地址
ip_slices = [10,123,132,125,168,187,25,66,37,168,131,86,53,19,163]

#状态码
status_codes = ["200","404","500"]


#sample(seq, n)从序列seq中选择n个随机且独立的元素

#随机生成一个带refer和keyword的url
def sample_refer():
    if random.uniform(0, 1) > 0.2:
        return "_"

    refer_str = random.sample(http_referers, 1)
    query_str = random.sample(search_keyword, 1)
    return refer_str[0].format(query=query_str[0])

#随机生成一个url
def sample_url():
    return random.sample(url_path,1)[0]

#随机生成一个ip
def sample_ip():
    slice = random.sample(ip_slices,4)
    return ".".join([str(item) for item in slice])

#随机生成一个状态码
def sample_status_code():
    return random.sample(status_codes,1)[0]


def generate_log(count = 10):
    #获取本地时间
    time_str = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())

    #打开access.log文件,并赋予 f 写的权限

    f = open("/opt/log/access.log","a+")

    while count >=1:
        query_log="{ip}\t{local_time}\t\"GET /{url} HTTP/1.1\"\t{status_code}\t{refer}".format(local_time=time_str,url=sample_url(),ip=sample_ip(),refer=sample_refer(),status_code=sample_status_code())
        #测试时输出到控制台，在实际使用中是将他存入相应的文件中
        print(query_log)

        #将日志输入到access.log
        f.write(query_log + "\n")

        count = count - 1

if __name__ == '__main__':
    generate_log()