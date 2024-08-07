# zookeeper
为分布式应用提供协调服务的Apache项目

## 一、特点
1. zookeeper：一个领导者 leader，多个跟随者follwoer组成的集群
2. 集群中只要有半数以上的节点存活，zookeeper集群就能正常服务
3. 全局数据一致，每个server保存一份相同的数据副本，clinet无论连接到哪个server，数据都是一致的
4. 更新请求顺序进行，来自同一个clinet的更新请求按其发送顺序依次进行
5. 数据更新原子性，一次数据要么更新成功，要么更新失败
6. 实时性，在一定时间范围内，clinet能读到最新数据
7. 集群中只要有过半的机器是正常工作的，那么整个集群对外就是可用的。也就是说如果有2个zookeeper，那么只要有1个死了zookeeper就不能用了，因为1没有过半，所以2个zookeeper的死亡容忍度为0；同理，要是有3个zookeeper，一个死了，还剩下2个正常的，过半了，所以3个zookeeper的容忍度为1；同理多列举几个：2->0;3->1;4->1;5->2;6->2会发现一个规律，2n和2n-1的容忍度是一样的，都是n-1，所以为了更加高效，zookeeper集群节点配置个数应该是奇数

## 二、集群规划

| node01    | node02    | node03    |
| --------- | --------- | --------- |
| zookeeper | zookeeper | zookeeper |

## 三、部署zookeeper

### 1、解压

```shell
[root@node01 ~]# tar -zxf zookeeper-3.4.11.tar.gz -C /opt/stanlong/
```

### 2、配置环境变量

```shell
[root@node01 zookeeper-3.4.11]# pwd
/opt/stanlong/zookeeper-3.4.11
[root@node01 zookeeper-3.4.11]# vi /etc/profile

export ZOOKEEPER_HOME=/opt/stanlong/zookeeper-3.4.11
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin

[root@node01 zookeeper-3.4.11]# source /etc/profile
[root@node01 zookeeper-3.4.11]# zk # 命令能自动补全zookeeper环境变量生效
zkCleanup.sh  zkCli.cmd     zkCli.sh      zkEnv.cmd     zkEnv.sh      zkServer.cmd  zkServer.sh 
```

### 3、编辑配置文件

注意所有的注释都不要写在键值对后面

```shell
[root@node01 conf]# pwd
/opt/stanlong/zookeeper-3.4.11/conf
[root@node01 conf]# cp zoo_sample.cfg zoo.cfg
[root@node01 conf]# vi zoo.cfg 
```

```properties
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
# 配置zookeeper数据存放目录
dataDir=/var/data/zk
# the port at which the clients will connect
clientPort=2181
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1
# 配置zookeeper节点，两个端口号是因为zookeeper运行时有两个状态：可用和不可用状态
# zookeeper 也是主从模型 一个领导者 leader，多个跟随者follwoer组成的集群
# 如果leader挂了， 可以根据数字快速选取出下一个leader
server.1=192.168.235.11:2888:3888
server.2=192.168.235.12:2888:3888
server.3=192.168.235.13:2888:3888
```

### 4、配置myid文件

```shell
[root@node01 ~]# mkdir -p /var/data/zk
[root@node02 ~]# mkdir -p /var/data/zk
[root@node03 ~]# mkdir -p /var/data/zk

[root@node01 ~]# echo 1 > /var/data/zk/myid # 把配置的server数字覆盖到数据目录myid这个文件
[root@node02 ~]# echo 2 > /var/data/zk/myid # 把配置的server数字覆盖到数据目录myid这个文件
[root@node03 ~]# echo 3 > /var/data/zk/myid # 把配置的server数字覆盖到数据目录myid这个文件
```

### 5、配置日志路径

1. 3.4.11 版本日志配置如下

   ```shell
   # 新建日志目录
   [root@node01 zookeeper-3.4.11]# pwd
   /opt/stanlong/zookeeper/zookeeper-3.4.11
   [root@node01 zookeeper-3.4.11]# mkdir logs
   
   ----------------------------------------------------------------------------------
   # 修改log4j.properties
   [root@node01 conf]# pwd
   /opt/stanlong/zookeeper/zookeeper-3.4.11/conf
   [root@node01 conf]# vi log4j.properties 
   
   # 原配置
   zookeeper.root.logger=INFO, CONSOLE
    
   # 修改后的配置
   zookeeper.root.logger=INFO, ROLLINGFILE
   ----------------------------------------------------------------------------------
   # 修改bin/zkEnv.sh
   [root@node01 bin]# pwd
   /opt/stanlong/zookeeper/zookeeper-3.4.11/bin
   [root@node01 bin]# vi zkEnv.sh 
   
   # 以下是原配置
   if [ "x${ZOO_LOG_DIR}" = "x" ]
   then
       ZOO_LOG_DIR="."
   fi
    
   if [ "x${ZOO_LOG4J_PROP}" = "x" ]
   then
       ZOO_LOG4J_PROP="INFO,CONSOLE"
   fi
    
    
   # 以下是修改后配置
   if [ "x${ZOO_LOG_DIR}" = "x" ]
   then
       ZOO_LOG_DIR="/opt/stanlong/zookeeper/zookeeper-3.4.11/logs"
   fi
    
   if [ "x${ZOO_LOG4J_PROP}" = "x" ]
   then
       ZOO_LOG4J_PROP="INFO,ROLLINGFILE"
   fi
   ```

2. 3.8.4 日志配置

   3.8.4 的日志文件改成了lockbak.xml 

### 6、分发zookeeper

将 /opt/stanlong/zookeeper-3.4.11/ 和相关环境变量分发到 node02、node03, 并配置环境变量生效

## 四、运行zookeeper

### 1、启动zookeeper

按node01,  node02，node03的顺序启动(其实顺序无所谓)

```shell
[root@node01 ~]# zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /opt/stanlong/zookeeper-3.4.11/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
[root@node01 ~]# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/stanlong/zookeeper-3.4.11/bin/../conf/zoo.cfg
Error contacting service. It is probably not running. # 说明zookeeper 启动数量未过半

[root@node02 ~]# zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /opt/stanlong/zookeeper-3.4.11/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
[root@node02 ~]# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/stanlong/zookeeper-3.4.11/bin/../conf/zoo.cfg
Mode: leader # zookeeper节点启动数量过半， node03 被选为leader

[root@node03 ~]# zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /opt/stanlong/zookeeper-3.4.11/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
[root@node03 ~]# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /opt/stanlong/zookeeper-3.4.11/bin/../conf/zoo.cfg
Mode: follower # 其他节点都被选为 follower
```

### 2、启动客户端

```shell
[root@node02 ~]# zkCli.sh
...
WATCHER::
WatchedEvent state:SyncConnected type:None path:null
[zk: localhost:2181(CONNECTED) 0]
```
### 3、创建节点

```shell
[zk: localhost:2181(CONNECTED) 1] get /
cZxid = 0x0
ctime = Thu Jan 01 08:00:00 CST 1970
mZxid = 0x0
mtime = Thu Jan 01 08:00:00 CST 1970
pZxid = 0x0
cversion = -1
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 1
[zk: localhost:2181(CONNECTED) 2] create -e /stanlong 123456 --根节点下创建一个stanlong节点，值为123456
Created /stanlong
[zk: localhost:2181(CONNECTED) 3] ls /
[stanlong, zookeeper]
[zk: localhost:2181(CONNECTED) 4] get /stanlong    -- 查看创建的节点
123456
cZxid = 0x4
ctime = Wed Feb 05 17:59:27 CST 2020
mZxid = 0x4
mtime = Wed Feb 05 17:59:27 CST 2020
pZxid = 0x4
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x1000057e5030000
dataLength = 6
numChildren = 0
```
### 4、删除节点

```shell
[zk: localhost:2181(CONNECTED) 3] delete /hadoop-ha
[zk: localhost:2181(CONNECTED) 3] rmr /hadoop-ha # 递归删除
```

### 5、退出客户端

```
[zk: localhost:2181(CONNECTED) 0] quit
```
### 6、停止zookeeper

```shell
[root@node02 ~]# zkServer.sh stop
```

## 附录：zookeeper默认配置参数解读

```shell
# The number of milliseconds of each tick
tickTime=2000  --2s心跳一次
# The number of ticks that the initial
# synchronization phase can take
initLimit=10   -- 10 个心跳桢，一桢2秒。即20s之后认为主从连不上了
# The number of ticks that can pass between
# sending a request and getting an acknowledgement
syncLimit=5    --5 个心跳桢， 
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just
# example sakes.
dataDir=/tmp/data -- 默认数据保存路径，这个需要手动修改
# the port at which the clients will connect
clientPort=2181 -- 客户端端口号
```
























