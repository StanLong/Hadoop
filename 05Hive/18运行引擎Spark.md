# 运行引擎Spark

## 简介

​    Hive引擎包括：默认MR、tez、spark

​    Hive on Spark：Hive既作为存储元数据又负责SQL的解析优化，语法是HQL语法，执行引擎变成了Spark，Spark负责采用RDD执行。

​    Spark on Hive : Hive只作为存储元数据，Spark负责SQL解析优化，语法是Spark SQL语法，Spark负责采用RDD执行

## 兼容性检查

hive 2.3.9 版本源码的pom.xml文件用的spark版本是2.0.0的

## 配置Spark引擎

（1）Spark官网下载jar包地址：

http://spark.apache.org/downloads.html

```shell
[root@node01 ~]# ll *spark*
-rw-r--r-- 1 root root 220272364 2020/06/30 22:02:34 spark-2.0.0-bin-hadoop2.7.tgz
-rw-r--r-- 1 root root 220272364 2020/06/30 22:02:34 spark-2.0.0-bin-without-hadoop.tgz
```

（2） 解压并配置环境变量

```shell
[root@node01 stanlong]# pwd
/opt/stanlong
[root@node01 stanlong]# tar -zxf spark-2.0.0-bin-hadoop2.7.tgz 
[root@node01 stanlong]# mv spark-2.0.0-bin-hadoop2.7 spark


[root@node01 ~]# vi .bashrc 
export SPARK_HOME=/opt/stanlong/spark # Spark环境变量
export PATH=$PATH:$SPARK_HOME/bin

[root@node01 ~]# source .bashrc  # 使环境变量生效
```

（3） 在Hive中新增Spark配置文件

```shell
[root@node01 conf]# pwd
/opt/stanlong/hive/apache-hive-2.3.9-bin/conf

[root@node01 conf]# vi spark-defaults.conf
spark.master                           yarn
spark.eventLog.enabled                 true
spark.eventLog.dir                     hdfs://node01:8020/spark-history
spark.executor.memory                  1g
spark.driver.memory                    1g
```

在HDFS创建如下路径，用于存储历史日志

```shell
[root@node01 conf]# hadoop fs -mkdir /spark-history
```

（4） 上传Spark纯净版jar包到HDFS

采用Spark纯净版jar包，不包含hadoop和hive相关依赖，避免冲突

```shell
# 在HDFS上创建目录存储Spark的jar包
[root@node01 ~]# hadoop fs -mkdir /spark-jars

[root@node01 stanlong]# pwd
/opt/stanlong
[root@node01 stanlong]# tar -zxf spark-2.0.0-bin-without-hadoop.tgz 
[root@node01 stanlong]# mv spark-2.0.0-bin-without-hadoop spark-without-hadoop
[root@node01 stanlong]# cd spark-without-hadoop/jars
[root@node01 jars]# pwd
/opt/stanlong/spark-without-hadoop/jars
[root@node01 jars]# hdfs dfs -put -f * /spark-jars
```

（5）修改hive-site.xml文件

```shell
[root@node01 conf]# pwd
/opt/stanlong/hive/apache-hive-2.3.9-bin/conf

[root@node01 conf]# vi hive-site.xml

<!--Spark依赖位置（注意：端口号8020必须和namenode的端口号一致）-->
<property>
    <name>spark.yarn.jars</name>
    <value>${fs.defaultFS}:8020/spark-jars/*</value>
</property>
  
<!--Hive执行引擎-->
<property>
    <name>hive.execution.engine</name>
    <value>spark</value>
</property>

<!--Hive和spark连接超时时间, 单位是毫秒-->
<property>
    <name>hive.spark.client.connect.timeout</name>
    <value>10000000</value>
</property>
```

## 测试

报错

类型A

Failed to create spark client

Timed out waiting for client connection

类型B

java.lang.IllegalStateException(RPC channel is closed.)

## 结论

在node1上偶尔成功，可能是集群内存不够。但是注要安装步骤没有错
