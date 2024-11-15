# Hbase安装

本实例只搭建Hbase高可用

## 节点规划

|               | node01        | node02        | node03      |
| ------------- | ------------- | ------------- | ----------- |
| HMaster       | HMaster(主)   |               | HMaster(备) |
| HRegionServer | HRegionServer | HRegionServer |             |

### 角色说明

**HMaster**

1. 监控RegionServer
2. 处理RegionServer故障转移
3. 处理元数据变更
4. 处理region的分配或移除
5. 空闲时对数据进行负载均衡
6. 通过zookeeper发布自己的位置给客户端

**HRegionServer**

1. 负责存储Hbase的实际数据
2. 处理分配给它的region
3. 刷新缓存到HDFS上
4. 维护HLog
5. 执行压缩
6. 负责处理Region分片

## 解压

```shell
[root@node01 ~]# tar -zxf hbase-1.3.6-bin.tar.gz -C /opt/stanlong/hbase/
[root@node01 hbase-1.3.6]# pwd
/opt/stanlong/hbase/hbase-1.3.6
```

## 配置环境变量

```shell
[root@node01 hbase-1.3.6]# pwd
/opt/stanlong/hbase/hbase-1.3.6
[root@node01 hbase-1.3.6]# vi /etc/profile
export HBASE_HOME=/opt/stanlong/hbase/hbase-1.3.6 # Hbase 环境变量
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin:$HBASE_HOME/bin

[root@node01 hbase-1.3.6]# source /etc/profile # 使环境变量生效
[root@node01 hbase-1.3.6]# hbase
hbase             hbase-cleanup.sh  hbase-common.sh   hbase-config.sh   hbase-daemon.sh   hbase-daemons.sh  hbase-jruby 
```

## 改配置文件

```shell
[root@node01 conf]# pwd
/opt/stanlong/hbase/hbase-1.3.6/conf
[root@node01 conf]# ll
total 44
-rw-r--r-- 1 503 games 1811 Oct  5  2019 hadoop-metrics2-hbase.properties
-rw-r--r-- 1 503 games 7530 Oct 15  2019 hbase-env.sh
-rw-r--r-- 1 503 games 2257 Oct  5  2019 hbase-policy.xml
-rw-r--r-- 1 503 games  934 Oct  5  2019 hbase-site.xml
-rw-r--r-- 1 503 games 1169 Oct 11  2019 log4j-hbtop.properties
-rw-r--r-- 1 503 games 4722 Oct 15  2019 log4j.properties
-rw-r--r-- 1 503 games   10 Oct  5  2019 regionservers
```

### hbase-env.sh

```shell
# The java implementation to use.  Java 1.7+ required.
# 配置Java环境变量
export JAVA_HOME=/usr/java/jdk1.8.0_221-amd64

# Extra Java runtime options.
# Below are what we set by default.  May only work with SUN JVM.
# For more on why as well as other possible settings,
# see http://wiki.apache.org/hadoop/PerformanceTuning
export HBASE_OPTS="-XX:+UseConcMarkSweepGC"

# 注释掉 Configure PermSize 下面的两行，这两行在jdk1.7的版本才需要配置
# export HBASE_MASTER_OPTS="$HBASE_MASTER_OPTS -XX:PermSize=128m -XX:MaxPermSize=128m -XX:ReservedCodeCacheSize=256m"
# export HBASE_REGIONSERVER_OPTS="$HBASE_REGIONSERVER_OPTS -XX:PermSize=128m -XX:MaxPermSize=128m -XX:ReservedCodeCacheSize=256m"


# Where log files are stored.  $HBASE_HOME/logs by default.
# 配置Hbase日志路径
export HBASE_LOG_DIR=${HBASE_HOME}/logs

# Tell HBase whether it should manage it's own instance of Zookeeper or not.
# 不使用Hbase自带的zk
export HBASE_MANAGES_ZK=false
```

### hbase-site.xml

```xml
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
-->
<configuration>
    <!--配置为core-site.xml 中的fs.defaultFS -->
	<property>
	  <name>hbase.rootdir</name>
	  <value>hdfs://hacluster/hbase</value>
	</property>
	<!-- 分布式集群 -->
    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>
	<!-- zookeeper集群 -->
    <property>
        <name>hbase.zookeeper.quorum</name>
        <value>node01,node02,node03</value>
    </property>

	<!-- Hbase 在zookeeper 上数据的根目录znode节点 -->
    <property>
        <name>hbase.znode.parent</name>
        <value>/hbase</value>
    </property>
	<!-- 本地文件系统 tmp 目录 -->
    <property>
        <name>hbase.tmp.dir</name>
        <value>/var/data/hbase/tmp</value>
    </property>


	<!-- hbae master 节点默认端口16000， 可不配置 -->
    <property>
        <name>hbase.master.port</name>
        <value>16000</value>
    </property>
	<!-- hbae master 的webui页面默认绑定的地址， 可不配置 -->
    <property>
        <name>hbase.master.info.port</name>
        <value>16010</value>
    </property>
	<!-- 配置zookeeper的dataDir路径(会自动创建)  -->
	<property>
		<name>hbase.zookeeper.property.dataDir</name>
		<value>/var/data/zk</value>
	</property>
</configuration>
```

### regionservers

```pro
node01
node02
```

### 软连接hadoop的配置文件

```shell
[root@node01 conf]# pwd
/opt/stanlong/hbase/hbase-1.3.6/conf
[root@node01 conf]# n -s /opt/stanlong/hadoop-2.10.2/etc/hadoop/core-site.xml /opt/stanlong/hbase-1.3.6/conf/core-site.xml
[root@node01 conf]# ln -s /opt/stanlong/hadoop-2.10.2/etc/hadoop/hdfs-site.xml /opt/stanlong/hbase-1.3.6/conf/hdfs-site.xml
```

## 分发Hbase

分发脚本参考 23自定义集群脚本/分发脚本

```shell
[root@node01 myshell]#  ./rsyncd.sh /opt/stanlong/hbase/hbase-1.3.6/
```

## 修改node02,03的环境变量

```shell
[root@node02 hbase-1.3.6]# vi /etc/profile
export HBASE_HOME=/opt/stanlong/hbase/hbase-1.3.6 # Hbase 环境变量
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin:$HBASE_HOME/bin

[root@node02 hbase-1.3.6]# source /etc/profile # 使环境变量生效
[root@node02 hbase-1.3.6]# hbase
hbase             hbase-cleanup.sh  hbase-common.sh   hbase-config.sh   hbase-daemon.sh   hbase-daemons.sh  hbase-jruby 
```

## 启动Hbase集群

1. 先启动Hadoop-ha，启动脚本见 Hadoop/18自定义集群脚本

```shell
[root@node01 myshell]# ./hadoop-ha-start.sh
```

2. 启动Hbase集群

```shell
[root@node01 ~]# start-hbase.sh 
starting master, logging to /opt/stanlong/hbase/hbase-1.3.6/logs/hbase-root-master-node01.out
node03: starting regionserver, logging to /opt/stanlong/hbase/hbase-1.3.6/bin/../logs/hbase-root-regionserver-node03.out
node02: starting regionserver, logging to /opt/stanlong/hbase/hbase-1.3.6/bin/../logs/hbase-root-regionserver-node02.out
node04: starting master, logging to /opt/stanlong/hbase/hbase-1.3.6/bin/../logs/hbase-root-master-node04.out

[root@node01 myshell]# ./cluster-jps.sh 
--------- node01 ----------
9845 Jps
8615 DFSZKFailoverController
9625 HMaster # 主master
8970 RunJar
8286 NameNode
8862 RunJar
--------- node02 ----------
7968 QuorumPeerMain
8016 JournalNode
9380 Jps
8151 DataNode
8327 NodeManager
9209 HRegionServer # HRegionServer
8924 RunJar
8398 ResourceManager
--------- node03 ----------
9220 Jps
9047 HRegionServer # HRegionServer
7945 QuorumPeerMain
8377 ResourceManager
8745 RunJar
7997 JournalNode
8301 NodeManager
8126 DataNode
--------- node04 ----------
8194 DataNode
8386 DFSZKFailoverController
8483 NodeManager
7942 QuorumPeerMain
8998 HMaster # 备master
8123 NameNode
9182 Jps
8015 JournalNode
```

## 页面访问

http://node01:16010/master-status

![](./doc/04.png)

## 补充：Hbase启停单个服务

```shell
# 启停 HMaster 服务
hbase-daemon.sh start master
hbase-daemon.sh stop master

# 启停 HRegionServer
hbase-daemon.sh start regionserver
hbase-daemon.sh stop regionserver
```

