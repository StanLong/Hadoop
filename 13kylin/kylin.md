# kylin

# kylin 安装

```shell
[root@node01 ~]# tar -zxf apache-kylin-2.6.6-bin-hbase1x.tar.gz -C /opt/stanlong/
[root@node01 ~]# cd /opt/stanlong/
[root@node01 stanlong]# mv apache-kylin-2.6.6-bin-hbase1x/ kylin
[root@node01 stanlong]# ll
total 4
drwxr-xr-x  9 root root   210 Jun 20 11:12 flume
drwxr-xr-x 10 root root   161 Jun 11 10:27 hadoop-2.9.2
drwxr-xr-x 10 root root   161 Jun 11 10:13 hadoop-2.9.2-full
drwxr-xr-x  8 root root   172 Jun 14 11:28 hbase
drwxr-xr-x 10 root root   245 Jun 28 11:04 hive
drwxr-xr-x  7 root root   101 Jun 22 01:17 kafka
drwxr-xr-x  8 root root   138 Jul  4 07:31 kafka-manager
drwxr-xr-x  8 root root   150 May 12 07:54 kylin
drwxrwxr-x  6 2000  2000   79 Jun 25 16:59 scala
drwxr-xr-x 13 1000  1000  211 Jun  6 08:09 spark
drwxr-xr-x  5  502 games 4096 Mar 19  2019 tez-0.9.2
[root@node01 stanlong]# 

配置 kylin 环境变量
[root@node01 kylin]# pwd
/opt/stanlong/kylin
[root@node01 kylin]# vi /etc/profile
export KYLIN_HOME=/opt/stanlong/kylin
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin:$HBASE_HOME/bin:$SPARK_HOME/bin:$SPARK_HOME/sbin:$SCALA_HOME/bin:$KYLIN_HOME/bin
[root@node01 kylin]# source /etc/profile
```

# 启动kylin

kylin 启动之前要先把环境准备好。这里准备一个脚本

```
kylin启动时遇到 ERROR: Check hive's usability failed, please check the status of your cluster解决办法如下:

1、检查/etc/profile.d/env.sh中HIVE_HOME是否有配置，配置后需要source一下

2、bin/hive进入hive客户端，执行hive命令检查hive是否可用

3、以上1、2都正确，则问题可能是hive执行时间过长超时，kylin中bin目录下的check-hive-usability.sh中timeLeft时间是60s，可以将其调到适合自己机器的值

kylin启动后，tomcat 又报了一个错
Caused by: java.lang.ClassCastException: com.fasterxml.jackson.datatype.jsr310.JavaTimeModule cannot be cast to com.fasterxml.jackson.databind.Module
```



