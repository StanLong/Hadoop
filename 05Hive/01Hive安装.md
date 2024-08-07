# Hive

Hive下载地址： https://archive.apache.org/dist/hive/

Hive 是基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射成一张表，并提供类SQL的查询功能。

本质是：将HQL转化成MapReduce程序

1）Hive处理的数据存储在HDFS

2）Hive分析数据底层的实现是MapReduce

3）执行程序运行在Yarn上

![](./doc/01.png)

Hive中搭建分为三种方式 `内嵌Derby方式` 、`Local方式`、 `Remote方式` 三种方式归根到底就是元数据的存储位置不一样

## 节点规划

| 服务器端(apache-hive-1.2.2-bin.tar.gz) | 客户端(apache-hive-1.2.2-bin.tar.gz) |
| -------------------------------------- | ------------------------------------ |
| node01                                 | node02                               |

## Hive安装部署

### 启动hadoop集群

参考07Hadoop/04HA搭建启动hadoop集群

### 解压

```shell
[root@node01 ~]# tar -zxf apache-hive-1.2.1-bin.tar.gz -C /opt/stanlong/hive
```

### 配置hive环境变量

```shell
[root@node01 bin]# pwd
/opt/stanlong/hive/apache-hive-1.2.1-bin
[root@node01 bin]# vi /etc/profile # 在文件最后添加
export HIVE_HOME=/opt/stanlong/hive/apache-hive-1.2.1-bin  # HIVE环境变量
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin
[root@node01 bin]# source /etc/profile # 使配置文件生效
[root@node01 bin]# hi # 命令提示
history         hive            hive-config.sh  hiveserver2   
```

## 安装Hive

### 内嵌Derby方式

略

### Local方式

这种存储方式需要在本地运行一个mysql服务器，元数据存储在mysql里。安装mysql 参考 DBA/mysql/01mysql安装.md

1. 上传mysql驱动jar包

```shell
[root@node01 lib]# pwd
/opt/stanlong/hive/apache-hive-1.2.2-bin/lib
[root@node01 lib]# mv ~/mysql-connector-java-5.1.37.jar .
```

2. 配置hive-env.sh

```shell
[root@node01 conf]# pwd
/opt/stanlong/hive/apache-hive-1.2.2-bin/conf
[root@node01 conf]# cp hive-env.sh.template hive-env.sh
[root@node01 conf]# vi hive-env.sh
```

```properties
... 省略掉文件中的注释

# 配置hive需要的第三方jar包路径，不然启动hive可能会报错
export HIVE_AUX_JARS_PATH=/opt/stanlong/hadoop-ha/hadoop-2.10.2/share/hadoop/common

# 配置Hadoop环境变量，在/etc/profile里配置过的话，这里也可以不用再配置
export HADOOP_HOME=/opt/stanlong/hadoop/hadoop-2.10.2

# 配置Hive配置文件的路径
export HIVE_CONF_DIR=/opt/stanlong/hive/apache-hive-1.2.2-bin/conf
```

3. 更改hive 默认配置

默认情况：Hive内部表都属于缺省库default，在HDFS的目录为/user/hive/warehouse/

在HDFS上创建hive默认目录/tmp和/user/hive/warehouse两个目录并修改他们的同组权限可写

```shell
[root@node01 hive]# hdfs dfs -mkdir /tmp
[root@node01 hive]# hdfs dfs -mkdir -p /user/hive/warehouse
[root@node01 hive]# hdfs dfs -chmod g+w /tmp
[root@node01 hive]# hdfs dfs -chmod g+w /user/hive/warehouse
```

4. 配置 hive-site.xml

官方文档路径 https://cwiki.apache.org/confluence/display/Hive/AdminManual+MetastoreAdmin

   ```shell
[root@node01 conf]# pwd
/opt/stanlong/hive/apache-hive-1.2.1-bin/conf
[root@node01 conf]# vi hive-site.xml
   ```

   ```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <!-- 配置hive文件在hdfs上的保存路径 -->
    <property>
        <name>hive.metastore.warehouse.dir</name>
        <value>/user/hivedb/warehouse</value>
    </property>
    <!-- 元数据库的链接地址 mysql -->
    <property>
        <name>javax.jdo.option.ConnectionURL</name>
        <value>jdbc:mysql://192.168.235.11:3306/hivedb?createDatabaseIfNotExist=true&amp;useSSL=false&amp;characterEncoding=UTF-8</value>
    </property>
    <!-- 指定mysql连接信息 -->
    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>com.mysql.jdbc.Driver</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionUserName</name>
        <value>root</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionPassword</name>
        <value>root</value>
    </property>
    <!-- 表头信息配置 -->
    <property>
        <name>hive.cli.print.header</name>
        <value>true</value>
    </property>
    <!-- 显示当前数据库 -->
    <property>
        <name>hive.cli.print.current.db</name>
        <value>true</value>
    </property>
    <!-- hive 升级到 1.2.1 版本后需要增加如下配置 -->
    <!-- 关闭metastore版本验证 -->
    <property>
        <name>hive.metastore.schema.verification</name>
        <value>false</value>
    </property>
    <!-- 允许自己建表建视图 -->
    <property>
        <name>datanucleus.schema.autoCreateTables</name>
        <value>true</value>
    </property>
    <!-- 指定hiveserver2连接的host -->
    <property>
        <name>hive.server2.thrift.bind.host</name>
        <value>node01</value>
    </property>
    <!-- 指定hiveserver2连接的端口号 -->
    <property>
        <name>hive.server2.thrift.port</name>
        <value>10000</value>
    </property>
    <!-- hiveserver2的高可用参数，开启此参数可以提高hiveserver2的启动速度 -->
    <property>
        <name>hive.server2.active.passive.ha.enable</name>
        <value>true</value>
    </property>
</configuration>
   ```

5. 启动hive

启动前执行初始化命令，如果配置了SPARK_HOME环境变量，启动后会有告警，百度处理。

```shell
[root@node01 bin]# schematool -dbType mysql -initSchema
Metastore connection URL:	 jdbc:mysql://192.168.235.11:3306/hivedb?createDatabaseIfNotExist=true&useSSL=false
Metastore Connection Driver :	 com.mysql.jdbc.Driver
Metastore connection User:	 root
Starting metastore schema initialization to 1.2.0
Initialization script hive-schema-1.2.0.mysql.sql
Initialization script completed
schemaTool completed

# 初始化完成之后会在hive数据库里看到 hivedb 这个库
```

```shell
[root@node01 ~]# hive
21/01/23 18:36:46 WARN conf.HiveConf: HiveConf of name hive.metastore.local does not exist

Logging initialized using configuration in jar:file:/opt/stanlong/hive/apache-hive-1.2.1-bin/lib/hive-common-1.2.1.jar!/hive-log4j.properties
hive (default)> 
```

查看MySQL数据库

hivedb库已成功创建， 表TBLS和DBS保存了hive表和相关的数据库信息

```sql
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| hivedb             |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.00 sec)
```

```sql
mysql> use hivedb;
mysql> select * from TBLS;
+--------+-------------+-------+------------------+-------+-----------+-------+----------+---------------+--------------------+--------------------+
| TBL_ID | CREATE_TIME | DB_ID | LAST_ACCESS_TIME | OWNER | RETENTION | SD_ID | TBL_NAME | TBL_TYPE      | VIEW_EXPANDED_TEXT | VIEW_ORIGINAL_TEXT |
+--------+-------------+-------+------------------+-------+-----------+-------+----------+---------------+--------------------+--------------------+
|      1 |  1611398538 |     1 |                0 | root  |         0 |     1 | hehe     | MANAGED_TABLE | NULL               | NULL               |
+--------+-------------+-------+------------------+-------+-----------+-------+----------+---------------+--------------------+--------------------+
1 row in set (0.00 sec)

mysql> select * from DBS;
+-------+-----------------------+----------------------------------------+---------+------------+------------+
| DB_ID | DESC                  | DB_LOCATION_URI                        | NAME    | OWNER_NAME | OWNER_TYPE |
+-------+-----------------------+----------------------------------------+---------+------------+------------+
|     1 | Default Hive database | hdfs://hacluster/user/hivedb/warehouse | default | public     | ROLE       |
+-------+-----------------------+----------------------------------------+---------+------------+------------+
1 row in set (0.00 sec)
```

### Remote方式

采取服务端和客户端分离的方式安装，按节点规划，服务端在node01，客户端在node02上. 客户端与服务端之间通过 thrift 协议通信，端口号9083

1. 分发node01上的hive目录到客户端节点

   ```shell
   [root@node01 stanlong]# pwd
   /opt/stanlong
   [root@node01 stanlong]# scp -r hive/ node02:`pwd`
   ```
   
2. 分发 node01 上的 /etc/profile 文件到客户端节点. 并使文件生效

   ```shell
   [root@node01 ~]# scp /etc/profile node02:/etc/profile
   [root@node02 stanlong]# source /etc/profile
   [root@node02 stanlong]# hi
   history         hive/           hive-config.sh  hiveserver2 
   ```
   
3. node01 后台启动

   ```shell
   [root@node01 ~]# nohup hive --service metastore > /dev/null 2>&1 & # 后台启动
   [1] 12109
   [root@node01 ~]# jobs # 查看后台启动任务，任务编号 1
   [1]+  Running                 nohup hive --service metastore &
   [root@node01 ~]# ss -nal | grep 9083 # 监听9083端口是否启用
   Netid  State      Recv-Q Send-Q                         Local Address:Port                                        Peer Address:Port
   tcp    LISTEN     0      50                                        *:9083                                                  *:*
   
   [root@node01 ~]# fg 1 后台任务挪到前台
   nohup hive --service metastore
   ctrl+c 退出后台任务
   ```

4. 编辑客户端节点的 hive-site.xml 文件

   ```xml
   <?xml version="1.0" encoding="UTF-8" standalone="no"?>
   <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
   <configuration>
       <!-- 配置与服务端的通信 -->
       <property>
   	    <name>hive.metastore.uris</name>
   	    <value>thrift://node01:9083</value>
   	</property>
   </configuration>
   ```
   
5. 客户端启动hive

   ```shell
   [root@node02 ~]# hive
   
   Logging initialized using configuration in jar:file:/opt/stanlong/hive/apache-hive-1.2.1-bin/lib/hive-common-1.2.1.jar!/hive-log4j.properties
   hive> 
   ```
   

## beeline访问hive

在服务端后台启动一个hiveserver进程

```shell
[root@node01 ~]# nohup hiveserver2 > /dev/null 2>&1 &
[root@node01 ~]# jobs
[1]-  Running                 nohup hive --service metastore &
[2]+  Running                 nohup hiveserver2 &
```

客户端执行beeline命令

```shell
[root@node02 conf]# beeline -u "jdbc:hive2://node01:10000"  -n root -p root
Connecting to jdbc:hive2://node01:10000
Connected to: Apache Hive (version 1.2.1)
Driver: Hive JDBC (version 1.2.1)
Transaction isolation: TRANSACTION_REPEATABLE_READ
Beeline version 1.2.1 by Apache Hive
0: jdbc:hive2://node01:10000> 
```

配置beeline别名

```shell
[root@node02 etc]# vi /etc/bashrc
alias beeline="beeline -u jdbc:hive2://node01:10000  -n root -p root"
```

启动脚本参考 23自定义集群脚本/Hive启停脚本.md

## 查看Hive日志

默认文件存放路径

```shell
[root@node01 conf]# pwd
/opt/stanlong/hive/apache-hive-1.2.1-bin/conf
[root@node01 conf]# vi hive-log4j.properties.template 
18 hive.log.threshold=ALL
19 hive.root.logger=INFO,DRFA
20 hive.log.dir=${java.io.tmpdir}/${user.name}
21 hive.log.file=hive.log
```

可知默认日志文件 /${java.io.tmpdir}/${user.name}/hive.log，在本例中也就是 /tmp/root/hive.log

```shell
[root@node01 conf]# cd /tmp/root/
[root@node01 root]# ll
-rw-r--r-- 1 root root 383198 Jan 25 06:06 hive.log
```
