# Hadoop3.X HA搭建

## 一、节点规划

|        | NN     | DN   | ZK   | ZKFC | JNN  | RS   | NM   | JobHistory |
| ------ | ------ | ---- | ---- | ---- | ---- | ---- | ---- | ---------- |
| node01 | NN(主) |      |      | ZKFC | JNN  |      |      |            |
| node02 | NN(备) | DN   | ZK   | ZKFC | JNN  |      | NM   |            |
| node03 |        | DN   | ZK   |      | JNN  | RS   | NM   | JobHistory |
| node04 |        | DN   | ZK   |      |      | RS   | NM   |            |

**角色说明**

- JNN : 当客户端发送请求给主NameNode时，元数据写到一个共享的磁盘中（两个Namenode都可以访问），这样元数据就可以保持一致了. JNN 保证了NameNode的高可用性, NN依赖JNN同步edits.log
- ZKFC：作为一个ZK集群的客户端，用来监控NN的状态信息， 每个运行NN的节点必须要运行一个ZKFC。ZKFC跟着NN启动，不是人为规划的
- RS:ResourceManager把NameNode和ZKFC糅合成一个进程，开启HA后会自动到ZK争抢锁。争抢到的为Active， 没争抢到的为Standby
- NM: NodeManager与DataNode做一比一配置，实现计算向数据移动

## 二、环境检查

安装jdk， 检查 /etc/hosts/， 做ssh免密，检查主机名，检查时间是否同步

## 三、配置zookeeper集群

### 1、解压

```shell
[root@node02 ~]# tar -zxf apache-zookeeper-3.8.4-bin.tar.gz -C /opt/
[root@node02 ~]# mv /opt/apache-zookeeper-3.8.4-bin /opt/zookeeper-3.8.4
```

### 2、配置环境变量

```shell
[root@node02 ~]# vi ~/.bashrc
export ZOOKEEPER_HOME=/opt/zookeeper-3.8.4
export PATH=$PATH:$JAVA_HOME/bin:$ZOOKEEPER_HOME/bin

[root@node02 ~]# source ~/.bashrc 
[root@node02 ~]# zk # 命令能自动补全zookeeper环境变量生效
zkCleanup.sh            zkEnv.sh                zkSnapshotComparer.cmd  zkTxnLogToolkit.cmd
zkCli.cmd               zkServer.cmd            zkSnapshotComparer.sh   zkTxnLogToolkit.sh
zkCli.sh                zkServer-initialize.sh  zkSnapShotToolkit.cmd   
zkEnv.cmd               zkServer.sh             zkSnapShotToolkit.sh    
```

### 3、编辑配置文件

1. zoo.cfg

   ```shell
   [root@node02 ~]# cp $ZOOKEEPER_HOME/conf/zoo_sample.cfg $ZOOKEEPER_HOME/conf/zoo.cfg
   [root@node02 ~]# vi $ZOOKEEPER_HOME/conf/zoo.cfg
   ```

   ```properties
   # 定位到 dataDir, 这个键值对配置zookeeper数据存放目录
   dataDir=/var/data/zk
   
   # 在配置文件最后加上如下配置
   # zookeeper节点配置两个端口号是因为zookeeper运行时有两个状态：可用和不可用状态
   # zookeeper 也是主从模型 一个领导者 leader，多个跟随者follwoer组成的集群
   # 如果leader挂了， 可以根据server后的数字快速选取出下一个leader, 数字最大的当主
   server.1=192.168.235.12:2888:3888
   server.2=192.168.235.13:2888:3888
   server.3=192.168.235.14:2888:3888
   ```

2. myid

   ```shell
   [root@node02 ~]# mkdir -p /var/data/zk
   [root@node03 ~]# mkdir -p /var/data/zk
   [root@node04 ~]# mkdir -p /var/data/zk
   
   [root@node02 ~]# echo 1 > /var/data/zk/myid # 把配置的server数字覆盖到数据目录myid这个文件
   [root@node03 ~]# echo 2 > /var/data/zk/myid # 把配置的server数字覆盖到数据目录myid这个文件
   [root@node04 ~]# echo 3 > /var/data/zk/myid # 把配置的server数字覆盖到数据目录myid这个文件
   ```

### 4、分发

配置完成之后将node02上的  /opt/zookeeper-3.8.4 和 相关环境变量分发到 node03、node04

### 5、运行

```shell
# 启动 node02 上的zookeeper
[root@node02 ~]# zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /opt/zookeeper-3.8.4/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED

# 查看状态提示说 zookeeper 可能没有正常运行，这是因为只启动了一台，启动的zookeeper没有过半数
[root@node02 ~]# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/zookeeper-3.8.4/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Error contacting service. It is probably not running.

-----------------------------------------------------------------------------------------------------

# 启动 node03 上的zookeeper
[root@node03 ~]# zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /opt/zookeeper-3.8.4/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED

# 启动数过半, node03 上的 zookeeper 是 leader
[root@node03 ~]# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/zookeeper-3.8.4/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: leader

# node02 会自动变成 follower
[root@node02 ~]# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/zookeeper-3.8.4/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: follower

-----------------------------------------------------------------------------------------------------
# 启动 node04 上的zookeeper
[root@node04 ~]# zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /opt/zookeeper-3.8.4/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED

# 仅管node04上的 server.id 最大，但是一个zookeeper集群中只有一个 leader, 所以 node04 也是 follower
[root@node04 ~]# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/zookeeper-3.8.4/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: follower

-----------------------------------------------------------------------
# 查看进程
[root@node02 ~]# for ip in node0{2..4};do echo $ip;ssh $ip "jps";done
node02
3242 QuorumPeerMain
4047 Jps
node03
3288 QuorumPeerMain
3950 Jps
node04
3936 Jps
3394 QuorumPeerMain
```

## 四、配置HDFS

### 1、环境检查

安装jdk， 检查 /etc/hosts/， 做ssh免密，检查主机名，检查时间是否同步

### 2、解压

```shell
[root@node01 ~]# tar -zxf hadoop-3.4.0.tar.gz -C /opt
```

### 3、配置hadoop环境变量

```shell
[root@node01 ~]# vi ~/.bashrc 
export HADOOP_HOME=/opt/hadoop-3.4.0
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# 使环境变量生效
[root@node01 ~]# source /etc/profile

# 输入ha能看到命令提示说明环境变量配置成功
[root@node01 ~]# ha
hadoop             hadoop.cmd         hadoop-daemon.sh   hadoop-daemons.sh  halt               hardlink           hash      
```

### 4、hadoop配置Java环境

```shell
[root@node01 ~]# cd $HADOOP_HOME/etc/hadoop
[root@node01 hadoop]# vi hadoop-env.sh 
# 配置JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/jdk-1.8.0_421-oracle-x64

# 在  # export HDFS_NAMENODE_USER=hdfs 添加如下几行， 通过使用export HDFS_NAMENODE_USER=hdfs来限制哪个用户可以执行namenode命令
export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root
export HDFS_JOURNALNODE_USER=root
export HDFS_ZKFC_USER=root
```

### 5、配置core-site.xml

```xml
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
</configuration>
```

### 6、配置hdfs-site.xml

```xml
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
        <value>node01:9000</value>
    </property>
    <property>
        <name>dfs.namenode.rpc-address.hacluster.nn2</name>
        <value>node02:9000</value>
    </property>
    <!-- http通信地址 -->
    <property>
        <name>dfs.namenode.http-address.hacluster.nn1</name>
        <value>node01:9870</value>
    </property>
    <property>
        <name>dfs.namenode.http-address.hacluster.nn2</name>
        <value>node02:9870</value>
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
        <value>/root/.ssh/id_rsa</value>
    </property>
    <!-- 开启zookeeper的自动故障转移功能 -->
    <property>
        <name>dfs.ha.automatic-failover.enabled</name>
        <value>true</value>
    </property>
</configuration>
```

### 7、配置worker

```shell
# node02 和 node03 做为datanode
sed -i '/localhost/d' $HADOOP_HOME/etc/hadoop/workers

cat > $HADOOP_HOME/etc/hadoop/workers << EOF
node02
node03
node04
EOF
```

### 8、分发

将node01上的 /opt/hadoop-3.4.0 和 ~/.bashrc里关于hadoop的环境配置都分发到node02，node03，node04上

### 9、运行

1. 先启动 journalnode

   ```shell
   for ip in node{01..03};do echo $ip;ssh $ip "hadoop-daemon.sh start journalnode";done
   ```

2. 格式化namenode

   ```shell
   [root@node01 ~]# hdfs namenode -format
   ```

3. 启动namenode， 让NN和JNN同步数据

   ```shell
   [root@node01 ~]# hadoop-daemon.sh start namenode  # 主
   [root@node02 ~]# hdfs namenode -bootstrapStandby  # 备
   ```

4. 格式化zookeeper

   ```shell
   [root@node01 ~]# hdfs zkfc -formatZK
   
   # 格式化的日志里会有这样一行提示
   2024-08-05 23:20:30,981 INFO ha.ActiveStandbyElector: Successfully created /hadoop-ha/hacluster in ZK.
   
   # 进入到zk的客户端可以看到更详细的信息
   [root@node03 ~]# zkCli.sh
   [zk: localhost:2181(CONNECTED) 0] ls /
   [hadoop-ha, zookeeper]
   [zk: localhost:2181(CONNECTED) 2] ls /hadoop-ha
   [hacluster]
   [zk: localhost:2181(CONNECTED) 3] ls /hadoop-ha/hacluster
   []
   ```

5. 启动集群

   ```shell
   [root@node01 ~]# start-dfs.sh
   
   # 各节点进程如下， 与节点规划一致
   [root@node01 ~]# for ip in node{01..04};do echo $ip;ssh $ip jps;done
   node01
   5700 NameNode
   5256 JournalNode
   8009 DFSZKFailoverController
   8348 Jps
   node02
   7091 DFSZKFailoverController
   1332 QuorumPeerMain
   5388 JournalNode
   6252 DataNode
   6175 NameNode
   7407 Jps
   node03
   6116 DataNode
   1320 QuorumPeerMain
   6968 Jps
   5373 JournalNode
   node04
   6650 Jps
   5902 DataNode
   1327 QuorumPeerMain
   ```

   网页访问 node01:9870 ， node02:9870 观察现象

6. 测试

   在avtice节点上分别kill掉 namenode 和 zkfc，观察主备切换情况

   ```shell
   # namenode 和 zkf 的启停方式
   hadoop-daemon.sh start namenode
   hadoop-daemon.sh stop namenode
   hadoop-daemon.sh start zkfc
   hadoop-daemon.sh stop zkfc
   ```


## 五、配置MR和YARN

### 1、配置mapred-site.xml

```xml
<configuration>
    
    <!-- mapreduce.framework.name 默认使local，可根据自己的配置选择yarn或者yarn-tez -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
   
    
    <!-- 3.X版本新特性，不加会报错 -->
    <!-- 为 MR 程序主进程添加环境变量 -->
    <property>
        <name>yarn.app.mapreduce.am.env</name>
        <value>${HADOOP_HOME}</value>
    </property>
    <!-- 为 Map 添加环境变量 -->
    <property>
        <name>mapreduce.map.env</name>
        <value>${HADOOP_HOME}</value>
    </property>
    <!-- 为 Reduce 添加环境变量 -->
    <property>
        <name>mapreduce.reduce.env</name>
        <value>${HADOOP_HOME}</value>
    </property>
    <!-- ################################### 以上配置可以完整的跑一个hadoop3.X 自带的 wordcount 程序 ################################### -->
</configuration>
```

### 2、配置yarn-site.xml

```xml
<configuration>
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
       <value>node02:2181,node03:2181,node04:2181</value>
    </property>

   <!-- 配置RS集群标识 -->
    <property>
       <name>yarn.resourcemanager.cluster-id</name>
       <value>cluster1</value>
    </property>
    
    <!-- 逻辑节点到物理节点的映射 -->
    <!-- 根据规划RS配置到node03和node04上 -->
    <property>
       <name>yarn.resourcemanager.ha.rm-ids</name>
       <value>rm1,rm2</value>
    </property>
    <property>
       <name>yarn.resourcemanager.hostname.rm1</name>
       <value>node03</value>
    </property>
    <property>
       <name>yarn.resourcemanager.hostname.rm2</name>
       <value>node04</value>
    </property>
    
    <!--配置rm1-->
    <property>
        <name>yarn.resourcemanager.address.rm1</name>
        <value>node03:8032</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address.rm1</name>
        <value>node03:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address.rm1</name>
        <value>node03:8088</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address.rm1</name>
        <value>node03:8031</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address.rm1</name>
        <value>node03:8033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.admin.address.rm1</name>
        <value>node03:23142</value>
    </property>
    <!--配置rm2-->
    <property>
        <name>yarn.resourcemanager.address.rm2</name>
        <value>node04:8032</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address.rm2</name>
        <value>node04:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address.rm2</name>
        <value>node04:8088</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address.rm2</name>
        <value>node04:8031</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address.rm2</name>
        <value>node04:8033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.ha.admin.address.rm2</name>
        <value>node04:23142</value>
    </property> 

	<!-- 3.X版本新特性，不加会报错 -->
    <!-- 环境变量名单 -->
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
    
    <!-- ################################### 以上配置可以完整的跑一个hadoop3.X 自带的 wordcount 程序 ################################### -->

</configuration>
```

### 3、分发

将修改后的 mapred-site.xml 和 yarn-site.xml 分发到集群的节点节点上

### 4、运行

1. 运行

   ```shell
   [root@node01 ~]# start-yarn.sh
   ```

   网页访问 http://node03:8088/cluster ， http://node04:8088/cluster  观察现象

2. 测试

   ```shell
   #准备测试数据
   [root@node01 ~]# cat test.txt 
   the quick brown fox jumps over the lazy dog
   the quick brown fox jumps over the lazy dog
   the quick brown fox jumps over the lazy dog
   the quick brown fox jumps over the lazy cat
   the quick brown fox jumps over the lazy dog
   
   # 测试数据上传到hdfs
   [root@node01 ~]# hdfs dfs -mkdir -p /user/root
   [root@node01 ~]# hdfs dfs -put test.txt /user/root
   
   # 执行hadoop自带的 wordcount
   [root@node01 ~]# cd /opt/hadoop-3.4.0/share/hadoop/mapreduce
   [root@node01 mapreduce]# hadoop jar hadoop-mapreduce-examples-3.4.0.jar wordcount /user/root/test.txt /tmp/wc/output
   
   # 查看执行结果 part-r-00000： r 代表reduce生成的文件 00000 是指这个文件是第一个reduce执行完成后生成的
   [root@node01 mapreduce]# hdfs dfs -ls /tmp/wc/output
Found 2 items
   -rw-r--r--   2 root supergroup          0 2024-08-06 22:00 /tmp/wc/output/_SUCCESS
   -rw-r--r--   2 root supergroup         63 2024-08-06 22:00 /tmp/wc/output/part-r-00000
   [root@node01 mapreduce]# hdfs dfs -cat /tmp/wc/output/part-r-00000
   brown	5
   cat	1
   dog	4
   fox	5
   jumps	5
   lazy	5
   over	5
   quick	5
   the	10
   ```
   
   如果有报错到 http://node03:8042/logs/userlogs 查看报错日志

## 六、配置历史日志

### 1、配置mapred-site.xml

```xml
    <!-- 配置历史服务的内部通讯地址和web访问地址 -->
    <property>
        <name>mapreduce.jobhistory.address</name>
        <value>node03:10020</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>node03:19888</value>
    </property>
    <!-- 作业运行过程中产生的日志保存路径 -->
    <property>
        <name>mapreduce.jobhistory.intermediate-done-dir</name>
        <value>/mr-history/tmp</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.done-dir</name>
        <value>/mr-history/done</value>
    </property>
    <!-- 日志保存24小时 -->
    <property>
        <name>mapreduce.job.userlog.retain.hours</name>
        <value>24</value>
    </property>
```

### 2、配置yarn-site.xml

```xml
    <!-- 开启日志聚合 -->
    <property>
        <name>yarn.log-aggregation-enable</name>
        <value>true</value>
    </property>
    <!-- 日志保存一天 -->
    <property>
        <name>yarn.log-aggregation.retain-seconds</name>
        <value>86400</value>
    </property>
    <!-- 日志聚合目录, 默认这个保存hdfs目录是 /tmp/logs  -->
    <property>
        <name>yarn.nodemanager.remote-app-log-dir</name>
        <value>/tmp/app-logs</value>
    </property>
```

### 3、运行

1. 运行

   ```shell
   # 按节点规划历史服务器配置在了 node03 节点， 所以只能在 node03 上启动 historyserver， 其他节点启不来
   [root@node03 ~]# mapred --daemon start historyserver
   ```

2. 测试

   重新执行一下wordcount测试程序，然后在网页上能正常访问  http://node03:19888/jobhistory ，则历史日志服务配置成功

   











