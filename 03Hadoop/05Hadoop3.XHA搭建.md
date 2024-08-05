# Hadoop3.X HA搭建

## 一、节点规划

|        | NN     | DN   | ZK   | ZKFC | JNN  | RS   | NM   | JobHistory |
| ------ | ------ | ---- | ---- | ---- | ---- | ---- | ---- | ---------- |
| node01 | NN(主) |      |      | ZKFC | JNN  | RS   | NM   |            |
| node02 | NN(备) | DN   | ZK   | ZKFC | JNN  | RS   | NM   |            |
| node03 |        | DN   | ZK   |      | JNN  |      | NM   | JobHistory |
| node04 |        | DN   | ZK   |      |      |      |      |            |

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

























































