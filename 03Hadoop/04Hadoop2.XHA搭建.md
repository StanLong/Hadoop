# Hadoop2.X HA搭建

## HA背景

- HDFS存在的问题
  - NN单点故障，难以应用于在线场景
  - NN压力过大，且内存受限，扩展性不好
- MapReduce存在的问题
  - JobTracker访问压力大，影响系统扩展
  - 难以支持除MapReduce之外的计算框架，比如Spark，Storm等

**HA架构图**

![](./doc/08.png)

## 节点规划

|        | NN     | DN   | ZK   | ZKFC | JNN  | RS   | NM   | JobHistory |
| ------ | ------ | ---- | ---- | ---- | ---- | ---- | ---- | ---------- |
| node01 | NN(主) | DN   | ZK   | ZKFC | JNN  | RS   | NM   |            |
| node02 | NN(备) | DN   | ZK   | ZKFC | JNN  | RS   | NM   |            |
| node03 |        | DN   | ZK   |      | JNN  |      | NM   | JobHistory |

### 角色说明

- JNN : 当客户端发送请求给主NameNode时，元数据写到一个共享的磁盘中（两个Namenode都可以访问），这样元数据就可以保持一致了. JNN 保证了NameNode的高可用性, NN依赖JNN同步edits.log
- ZKFC：作为一个ZK集群的客户端，用来监控NN的状态信息， 每个运行NN的节点必须要运行一个ZKFC。ZKFC跟着NN启动，不是人为规划的
- RS:ResourceManager把NameNode和ZKFC糅合成一个进程，开启HA后会自动到ZK争抢锁。争抢到的为Active， 没争抢到的为Standby
- NM: NodeManager与DataNode做一比一配置，实现计算向数据移动

## 环境检查

参考07Hadoop/02单节点伪分布式搭建。

此外node02被设置为NN，要做对自己的免密钥，对node01的免密钥

## 部署zookeeeper集群

参考 08Zookeeper

## 部署hadoop

### 解压hadoop到指定目录

```shell
[root@node01 ~]# tar -zxf hadoop-2.9.2.tar.gz -C /opt/stanlong/hadoop-ha/
```

### 配置hadoop环境变量

```shell
[root@node01 hadoop-2.9.2]# vi /etc/profile

export HADOOP_HOME=/opt/stanlong/hadoop/hadoop-2.9.2 # HADOOP-HA环境变量
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# 使环境变量生效
[root@node01 bin]# source /etc/profile

# 输入ha能看到命令提示说明环境变量配置成功
[root@node01 bin]# ha
hadoop             hadoop.cmd         hadoop-daemon.sh   hadoop-daemons.sh  halt               hardlink           hash      
```

### java环境变量二次配置

```shell
[root@node01 hadoop]# pwd
/opt/stanlong/hadoop-ha/hadoop-2.9.2/etc/hadoop
[root@node01 hadoop]# vi hadoop-env.sh 
  24 # The java implementation to use.
  25 export JAVA_HOME=/usr/java/jdk1.8.0_313
[root@node01 hadoop]# vi mapred-env.sh 
  16 export JAVA_HOME=/usr/java/jdk1.8.0_313
[root@node01 hadoop]# vi yarn-env.sh
  23 export JAVA_HOME=/usr/java/jdk1.8.0_313
```

### 编辑 core-site.xml(NN)

```shell
[root@node01 hadoop]# vi core-site.xml
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->

<!-- Put site-specific property overrides in this file. -->

<configuration>
    <!-- 规划了namenode在哪启动 -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hacluster</value>
    </property>
    <!-- 配置NN数据存放路径,目录必须为空 -->
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/var/data/hadoop/ha/data</value>
    </property>
    <!-- zookeeper集群信息 -->
    <property>
        <name>ha.zookeeper.quorum</name>
        <value>node01:2181,node02:2181,node03:2181</value>
    </property>

    <!--配置nn连接journal的重试次数和时间间隔 -->
    <property>
        <name>ipc.client.connect.max.retries</name>
        <value>100</value>
    </property>
    <property>
        <name>ipc.client.connect.retry.interval</name>
        <value>10000</value>
    </property>
    <property>
        <name>ipc.client.connect.timeout</name>
        <value>90000</value>
    </property>

    <!--配置HDFS网页登录使用的静态用户为root-->
    <property>
        <name>hadoop.http.staticuser.user</name>
        <value>root</value>
    </property>        
    
    <!--配置root(超级用户)允许通过代理访问的主机节点-->
    <property>
        <name>hadoop.proxyuser.root.hosts</name>
        <value>*</value>
    </property>    
    
    <!--配置root(超级用户)允许通过代理用户所属组-->
    <property>
        <name>hadoop.proxyuser.root.groups</name>
        <value>*</value>
    </property>    
            
    <!--配置root(超级用户)允许通过代理的用户-->
    <property>
        <name>hadoop.proxyuser.root.user</name>
        <value>*</value>
    </property>

</configuration>

```

### 编辑 hdfs-site.xml(NN)

1. 配置逻辑到物理的映射
2. 配置JNN
3. 故障切换实现代理的方法

```shell
[root@node01 hadoop]# vi hdfs-site.xml
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->

<!-- Put site-specific property overrides in this file. -->

<configuration>
    <!-- 设置了三个节点，副本数设为2 -->
    <property>
        <name>dfs.replication</name>
        <value>2</value>
    </property>
    <!-- HA模式不需要规划secondaryName -->
    <!-- <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>node02:50090</value>
    </property> -->
    
    <!-- 第一步：配置逻辑到物理的映射 -->
    <!-- namenode HA集群的别名 -->
    <property>
        <name>dfs.nameservices</name>
        <value>hacluster</value>
    </property>
    <!-- HA集群下的两个namenode的别名nn1,nn2 -->
    <property>
        <name>dfs.ha.namenodes.hacluster</name>
        <value>nn1,nn2</value>
    </property>
    <!-- RPC通信的地址 -->
    <property>
        <name>dfs.namenode.rpc-address.hacluster.nn1</name>
        <value>node01:8020</value>
    </property>
    <property>
        <name>dfs.namenode.rpc-address.hacluster.nn2</name>
        <value>node02:8020</value>
    </property>
    <!-- http通信地址 -->
    <property>
        <name>dfs.namenode.http-address.hacluster.nn1</name>
        <value>node01:50070</value>
    </property>
    <property>
        <name>dfs.namenode.http-address.hacluster.nn2</name>
        <value>node02:50070</value>
    </property>

    <!-- 第二步：配置JNN -->
    <property>
        <name>dfs.namenode.shared.edits.dir</name>
        <value>qjournal://node01:8485;node02:8485;node03:8485/hacluster</value>
    </property>
    <!-- journalNode 存放edit.log文件的路径 -->
    <property>
        <name>dfs.journalnode.edits.dir</name>
        <value>/var/data/hadoop/ha/jnn</value>
    </property>
    
    <!-- journalNode 存放edit.log文件的路径 -->
    <!-- 在hdfs-site.xml文件中配置多目录，最好提前配置好，否则更改目录需要重新启动集群 -->
    <!-- 
    <property>
    <name>dfs.datanode.data.dir</name>
<value>file:///${hadoop.tmp.dir}/dfs/data1,file:///hd2/dfs/data2,file:///hd3/dfs/data3,file:///hd4/dfs/data4</value>
</property> -->

    
    <!-- 第三步：故障切换实现代理的方法 -->
    <property>
        <name>dfs.client.failover.proxy.provider.hacluster</name>
    <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
    </property>
    <!-- zkfc进程进行回调需要免秘钥 -->
    <property>
        <name>dfs.ha.fencing.methods</name>
        <value>sshfence</value>
    </property>
    <!-- zkfc进程进行回调需要免秘钥，私钥路径 -->
    <property>
        <name>dfs.ha.fencing.ssh.private-key-files</name>
        <value>/root/.ssh/id_dsa</value>
    </property>
    <!-- 开启zookeeper的自动故障转移功能 -->
    <property>
        <name>dfs.ha.automatic-failover.enabled</name>
        <value>true</value>
    </property>
    
    <!-- 设置连接 JNN 的超时时间，默认是20S -->
    <property>
        <name>dfs.qjournal.write-txns.timeout.ms</name>
        <value>90000</value>
    </property>
    <property>
        <name>dfs.qjournal.start-segment.timeout.ms</name>
        <value>90000</value>
    </property>
    <property>
        <name>dfs.qjournal.select-input-streams.timeout.ms</name>
        <value>90000</value>
    </property>

</configuration>

```

### 编辑mapred-site.xml(MR)

```shell
[root@node01 hadoop]# cp mapred-site.xml.template mapred-site.xml
[root@node01 hadoop]# vi mapred-site.xml
```

```xml
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->

<!-- Put site-specific property overrides in this file. -->

<configuration>
    <!-- mapreduce.framework.name 默认使local，可根据自己的配置选择yarn或者yarn-tez -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>

    <!-- 设置map和reduce内存 -->
    <property>
    　　<name>mapreduce.map.memory.mb</name>
    　　<value>2048</value>
    </property>
    <property>
    　　<name>mapreduce.map.java.opts</name>
    　　<value>-Xmx4096M</value>
    </property>
    <property>
    　　<name>mapreduce.reduce.memory.mb</name>
    　　<value>3072</value>
    </property>
    <property>
    　　<name>mapreduce.reduce.java.opts</name>
    　　<value>-Xmx4096M</value>
    </property>
    
    <!-- 配置 jobhistory -->
    <property>
        <name>mapreduce.jobhistory.address</name>
        <value>node03:10020</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>node03:19888</value>
    </property>
    
    <!-- 正在运行中的日志在hdfs上的存放路径 -->
    <property>
        <name>mapreduce.jobhistory.intermediate-done-dir</name>
        <value>/mr-history/jobhistory/done_intermediate</value>
    </property>

    <!-- 运行过的日志存放在hdfs上的存放路径 -->
    <property>
        <name>mapreduce.jobhistory.done-dir</name>
        <value>/mr-history/jobhistory/done</value>
    </property>
    
    <!-- 非必须 -->
    <!-- 中间阶段的压缩 -->
    <property>
        <name>mapreduce.map.output.compress</name>
        <value>true</value>
    </property>
    <property>
        <name>mapreduce.map.output.compress.codec</name>
        <value>org.apache.hadoop.io.compress.SnappyCodec</value>
    </property>
     
    <!-- 最终阶段的压缩 -->
    <property>
        <name>mapreduce.output.fileoutputformat.compress</name>
        <value>true</value>
    </property>
    <property>
        <name>mapreduce.output.fileoutputformat.compress.codec</name>
        <value>org.apache.hadoop.io.compress.BZip2Codec</value>
    </property>
</configuration>
```

### 编辑yarn-site.xml(YARN)

```shell
[root@node01 hadoop]# vi yarn-site.xml 
```

```xml
<?xml version="1.0"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->
<configuration>

    <!-- Site specific YARN configuration properties -->
    <!-- reduce阶段拉取数据 -->
    <property>
　　　　<name>yarn.nodemanager.aux-services</name>
　　　　<value>mapreduce_shuffle</value>
　　</property>
    <!-- 开启resourcemanager自动故障转移 -->
　　<property>
   　　<name>yarn.resourcemanager.ha.enabled</name>
   　　<value>true</value>
　　</property>
    <!-- yarn.resourcemanager.zk-address is deprecated. Instead, use hadoop.zk.address -->
    <property>
   　　<name>hadoop.zk.address</name>
   　　<value>node01:2181,node02:2181,node03:2181</value>
　　</property>

   <!-- 配置RS集群标识 -->
　　<property>
   　　<name>yarn.resourcemanager.cluster-id</name>
   　　<value>cluster1</value>
　　</property>
    
    <!-- 逻辑节点到物理节点的映射 -->
    <!-- 根据规划resourcemanager配置到node01和node02上 -->
　　<property>
   　　<name>yarn.resourcemanager.ha.rm-ids</name>
   　　<value>rm1,rm2</value>
　　</property>
　　<property>
   　　<name>yarn.resourcemanager.hostname.rm1</name>
   　　<value>node01</value>
　　</property>
　　<property>
   　　<name>yarn.resourcemanager.hostname.rm2</name>
   　　<value>node02</value>
　　</property>

    <!--配置rm1-->
    <property>
        <name>yarn.resourcemanager.address.rm1</name>
        <value>node01:8032</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address.rm1</name>
        <value>node01:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address.rm1</name>
        <value>node01:8088</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address.rm1</name>
        <value>node01:8031</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address.rm1</name>
        <value>node01:8033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.admin.address.rm1</name>
        <value>node01:23142</value>
    </property>
    <!--配置rm2-->
    <property>
        <name>yarn.resourcemanager.address.rm2</name>
        <value>node02:8032</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address.rm2</name>
        <value>node02:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address.rm2</name>
        <value>node02:8088</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address.rm2</name>
        <value>node02:8031</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address.rm2</name>
        <value>node02:8033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.admin.address.rm2</name>
        <value>node02:23142</value>
    </property> 
    <!-- 关闭虚拟内存检查 -->
    <property>
        <name>yarn.nodemanager.vmem-check-enabled</name>
        <value>false</value>
    </property>
    
    <!-- 为每个容器请求分配的最小内存限制资源管理器（512M） -->
    <property>
        <name>yarn.scheduler.minimum-allocation-mb</name>
        <value>512</value>
    </property>
    <!-- 为每个容器请求分配的最大内存限制资源管理器（4G） -->
    <property>
        <name>yarn.scheduler.maximum-allocation-mb</name>
        <value>4096</value>
    </property>
    <!-- 虚拟内存比例，默认为2.1，此处设置为4倍 -->
    <property>
        <name>yarn.nodemanager.vmem-pmem-ratio</name>
        <value>4</value>
    </property>
    
    <!-- 开启日志聚合 -->
    <property>
        <name>yarn.log-aggregation-enable</name>
        <value>true</value>
    </property>

</configuration>
```

### 编辑 slaves（DN）(NM)

```shell
[root@node01 hadoop]vi slaves # slaves 规划了DataNode 在哪启动
# 把文件中的 localhost 替换成 node02 node03
node01
node02
node03
```

### 配置PID路径（可选）

配置这个主要是为了解决停止haddop集群时提示的报错`nodemanager did not stop gracefully after 5 seconds: killing with kill -9`

参考： https://blog.csdn.net/xiami010704/article/details/120716644

### 分发hadoop

把 hadoop 配置分发到 node02，node03上去

分发脚本参考 23自定义集群脚本/分发脚本

```shell
[root@node01 myshell]# ./rsyncd.sh /opt/stanlong/hadoop-ha/hadoop-2.9.2
```

### 分发/etc/profile文件

```shell
[root@node01 myshell]# ./rsyncd.sh /etc/profile
```

使node02，node03，node04上的profile文件生效

```shell
[root@node02 ~]# source /etc/profile
```

## 首次启动Hadoop-HA

**按顺序依次启动**

### 启动zookeeper

部署完zookeeper集群后，启动zookeeper

### 启动JNN

现在有三个NN，需要通过JNN来传递数据，所以在格式化之前先启动JNN

```shell
[root@node01 ~]# hadoop-daemon.sh start journalnode
starting journalnode, logging to /opt/stanlong/hadoop-ha/hadoop-2.9.2/logs/hadoop-root-journalnode-node01.out

[root@node02 ~]# hadoop-daemon.sh start journalnode
starting journalnode, logging to /opt/stanlong/hadoop-ha/hadoop-2.9.2/logs/hadoop-root-journalnode-node02.out

[root@node03 ~]# hadoop-daemon.sh start journalnode
starting journalnode, logging to /opt/stanlong/hadoop-ha/hadoop-2.9.2/logs/hadoop-root-journalnode-node03.out
```

### 格式化主namenode

```shell
[root@node01 hadoop]# hdfs namenode -format
```

### 启动主namenode

```shell
[root@node01 ~]# hadoop-daemon.sh start namenode
```

### 启动备namenode

```shell
[root@node02 stanlong]# hdfs namenode -bootstrapStandby
```

备namenode启动提示

```
STARTUP_MSG:   build = https://git-wip-us.apache.org/repos/asf/hadoop.git -r 826afbeae31ca687bc2f8471dc841b66ed2c6704; compiled by 'ajisaka' on 2018-11-13T12:42Z
STARTUP_MSG:   java = 1.8.0_65
************************************************************/
21/01/17 09:03:52 INFO namenode.NameNode: registered UNIX signal handlers for [TERM, HUP, INT]
21/01/17 09:03:53 INFO namenode.NameNode: createNameNode [-bootstrapStandby]
=====================================================
About to bootstrap Standby ID nn2 from:
           Nameservice ID: hacluster
        Other Namenode ID: nn1
  Other NN's HTTP address: http://node01:50070
  Other NN's IPC  address: node01/192.168.235.11:8020
             Namespace ID: 2138035154
            Block pool ID: BP-1557456169-192.168.235.11-1610844803058
               Cluster ID: CID-c4c8dcb2-8f46-40d7-8757-26ab1583c999
           Layout version: -63
       isUpgradeFinalized: true
=====================================================
21/01/17 09:03:55 INFO common.Storage: Storage directory /var/data/hadoop/ha/data/dfs/name has been successfully formatted.
21/01/17 09:03:55 INFO namenode.FSEditLog: Edit logging is async:true
21/01/17 09:03:55 WARN client.QuorumJournalManager: Quorum journal URI 'qjournal://node01:8485;node04:8485/hacluster' has an even number of Journal Nodes specified. This is not recommended!
21/01/17 09:03:56 INFO namenode.TransferFsImage: Opening connection to http://node01:50070/imagetransfer?getimage=1&txid=0&storageInfo=-63:2138035154:1610844803058:CID-c4c8dcb2-8f46-40d7-8757-26ab1583c999&bootstrapstandby=true
21/01/17 09:03:56 INFO namenode.TransferFsImage: Image Transfer timeout configured to 60000 milliseconds
21/01/17 09:03:57 INFO namenode.TransferFsImage: Combined time for fsimage download and fsync to all disks took 0.03s. The fsimage download took 0.02s at 0.00 KB/s. Synchronous (fsync) write to disk of /var/data/hadoop/ha/data/dfs/name/current/fsimage.ckpt_0000000000000000000 took 0.00s.
21/01/17 09:03:57 INFO namenode.TransferFsImage: Downloaded file fsimage.ckpt_0000000000000000000 size 323 bytes.
21/01/17 09:03:57 INFO namenode.NameNode: SHUTDOWN_MSG: 
/************************************************************
SHUTDOWN_MSG: Shutting down NameNode at node04/192.168.235.14
************************************************************/
```

### 格式化zookeeper

```shell
[root@node01 ~]# hdfs zkfc -formatZK

日志提示
 Successfully created /hadoop-ha/hacluster in ZK
```

**验证zk**

```shell
[root@node01 ~]# zkCli.sh

[zk: localhost:2181(CONNECTED) 0] ls / # 查看zk根目录的内容
[zookeeper, hadoop-ha]
[zk: localhost:2181(CONNECTED) 2] ls /hadoop-ha
[hacluster]
[zk: localhost:2181(CONNECTED) 3] ls /hadoop-ha/hacluster
[]
```

### 启动dfs

```shell
[root@node01 hadoop]# start-dfs.sh 
```

### 启动YARN

```shell
[root@node01 ~]# start-yarn.sh 
```

### 启动备RS

```shell
[root@node01 ~]# yarn-daemon.sh start resourcemanager
[root@node02 ~]# yarn-daemon.sh start resourcemanager
```

## 查看节点

```shell
[root@node01 myshell]# ./cluster.sh jps
--------- node01 ----------
13504 DataNode
13266 QuorumPeerMain
14275 Jps
14119 NodeManager
13688 JournalNode
13851 DFSZKFailoverController
14012 ResourceManager
13407 NameNode
--------- node02 ----------
12529 JournalNode
13138 Jps
12309 QuorumPeerMain
12891 ResourceManager
12428 DataNode
12365 NameNode
12605 DFSZKFailoverController
12735 NodeManager
--------- node03 ----------
11766 JournalNode
12102 Jps
11642 QuorumPeerMain
11995 JobHistoryServer
11692 DataNode
11887 NodeManager
```

## 页面访问(NN)

http://node01:50070/dfshealth.html#tab-overview

![](./doc/09.png)



![](./doc/10.png)



## 页面访问(RS)

http://node01:8088/cluster

![](./doc/11.png)

http://node02:8088/cluster/cluster （直接访问根目录跳转到node01，需要填具体的访问路径）

![](./doc/12.png)

## 验证MapReduce

1. 准备数据文件

```
[root@node01 ~]# vi student.txt
1001	zhangshan
1002	lishi
1003	zhaoliu
```

2. 上传数据到HDFS

```shell
[root@node01 ~]# hdfs dfs -put student.txt /
```

![](./doc/13.png)

3. 执行wordcount

```shell
[root@node01 mapreduce]# pwd
/opt/stanlong/hadoop/hadoop-2.9.2/share/hadoop/mapreduce
```

```shell
[root@node01 mapreduce]# hadoop jar hadoop-mapreduce-examples-2.9.2.jar wordcount /student.txt /wc/output
21/01/22 05:13:53 INFO input.FileInputFormat: Total input files to process : 1
21/01/22 05:13:54 INFO mapreduce.JobSubmitter: number of splits:1
21/01/22 05:13:54 INFO Configuration.deprecation: yarn.resourcemanager.zk-address is deprecated. Instead, use hadoop.zk.address
21/01/22 05:13:54 INFO Configuration.deprecation: yarn.resourcemanager.system-metrics-publisher.enabled is deprecated. Instead, use yarn.system-metrics-publisher.enabled
21/01/22 05:13:55 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1611262751690_0001
21/01/22 05:13:58 INFO impl.YarnClientImpl: Submitted application application_1611262751690_0001
21/01/22 05:13:58 INFO mapreduce.Job: The url to track the job: http://node02:8088/proxy/application_1611262751690_0001/
21/01/22 05:13:58 INFO mapreduce.Job: Running job: job_1611262751690_0001
21/01/22 05:14:25 INFO mapreduce.Job: Job job_1611262751690_0001 running in uber mode : false
21/01/22 05:14:25 INFO mapreduce.Job:  map 0% reduce 0%
21/01/22 05:14:53 INFO mapreduce.Job:  map 100% reduce 0%
21/01/22 05:15:15 INFO mapreduce.Job:  map 100% reduce 100%
21/01/22 05:15:16 INFO mapreduce.Job: Job job_1611262751690_0001 completed successfully
21/01/22 05:15:16 INFO mapreduce.Job: Counters: 49
	File System Counters
		FILE: Number of bytes read=81
		FILE: Number of bytes written=402749
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=132
		HDFS: Number of bytes written=51
		HDFS: Number of read operations=6
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=2
	Job Counters 
		Launched map tasks=1
		Launched reduce tasks=1
		Data-local map tasks=1
		Total time spent by all maps in occupied slots (ms)=24886
		Total time spent by all reduces in occupied slots (ms)=17579
		Total time spent by all map tasks (ms)=24886
		Total time spent by all reduce tasks (ms)=17579
		Total vcore-milliseconds taken by all map tasks=24886
		Total vcore-milliseconds taken by all reduce tasks=17579
		Total megabyte-milliseconds taken by all map tasks=25483264
		Total megabyte-milliseconds taken by all reduce tasks=18000896
	Map-Reduce Framework
		Map input records=3
		Map output records=6
		Map output bytes=63
		Map output materialized bytes=81
		Input split bytes=93
		Combine input records=6
		Combine output records=6
		Reduce input groups=6
		Reduce shuffle bytes=81
		Reduce input records=6
		Reduce output records=6
		Spilled Records=12
		Shuffled Maps =1
		Failed Shuffles=0
		Merged Map outputs=1
		GC time elapsed (ms)=1007
		CPU time spent (ms)=8040
		Physical memory (bytes) snapshot=318140416
		Virtual memory (bytes) snapshot=4163219456
		Total committed heap usage (bytes)=165810176
	Shuffle Errors
		BAD_ID=0
		CONNECTION=0
		IO_ERROR=0
		WRONG_LENGTH=0
		WRONG_MAP=0
		WRONG_REDUCE=0
	File Input Format Counters 
		Bytes Read=39
	File Output Format Counters 
		Bytes Written=51
```

4. 查看命令执行结果

```shell
[root@node01 myshell]# hdfs dfs -ls /wc/output
Found 2 items
-rw-r--r--   2 root supergroup          0 2021-01-22 05:15 /wc/output/_SUCCESS
-rw-r--r--   2 root supergroup         51 2021-01-22 05:15 /wc/output/part-r-00000
[root@node01 myshell]# hdfs dfs -cat /wc/output/part-r-00000
1001	1
1002	1
1003	1
lishi	1
zhangshan	1
zhaoliu	1
```

## 日常启动

脚本参考 23自定义集群脚本/启动Hadoop-HA.md

## 查看zookeeper锁

```shell
[zk: localhost:2181(CONNECTED) 0] ls /
[zookeeper, hadoop-ha]
[zk: localhost:2181(CONNECTED) 1] ls /hadoop-ha
[hacluster]
[zk: localhost:2181(CONNECTED) 2] ls /hadoop-ha/hacluster
[ActiveBreadCrumb, ActiveStandbyElectorLock]
[zk: localhost:2181(CONNECTED) 3] get /hadoop-ha/hacluster/ActiveStandbyElectorLock

	haclusternn1node01 �>(�> # 可见此时node01抢到了锁
cZxid = 0x100000010
ctime = Sun Jan 17 09:20:07 CST 2021
mZxid = 0x100000010
mtime = Sun Jan 17 09:20:07 CST 2021
pZxid = 0x100000010
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x400008891f20000
dataLength = 30
numChildren = 0
```