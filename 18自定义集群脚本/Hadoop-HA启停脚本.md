# 启动Hadoop-HA

如果SSH 远程到别的机器上无法执行环境变量的命令，需要在家目录下的 .bashrc 上增加环境变量

```shell
[root@node01 ~]# vi .bashrc
```

```shell
# .bashrc

# User specific aliases and functions

alias rm='rm -i'
alias cp='cp -i'
alias mv='mv -i'

# Source global definitions
if [ -f /etc/bashrc ]; then
        . /etc/bashrc
fi
export JAVA_HOME=/usr/java/jdk1.8.0_65
# export HADOOP_HOME=/opt/stanlong/hadoop-single/hadoop-2.9.2 # HADOOP单节点环境变量
# export HADOOP_HOME=/opt/stanlong/hadoop-cluster/hadoop-2.9.2 # HADOOP完全分布式环境变量
export HADOOP_HOME=/opt/stanlong/hadoop-ha/hadoop-2.9.2 # HADOOP-HA环境变量
export ZOOKEEPER_HOME=/opt/stanlong/zookeeper-3.4.11
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin
```

使 .bashrc 生效

```shell
[root@node01 ~]# source .bashrc
```

**Hadoop-HA启停脚本**

```shell
#!/bin/bash
start() { 
    #启动hadoop ha集群
    echo ----------------启动zk集群------------------
    for i in node02 node03 node04
    do
            ssh $i zkServer.sh start
            sleep 1s
    done

    echo ----------------启动DFS------------------
    ssh node01 start-dfs.sh
    sleep 10s

    echo ----------------启动主YARN------------------
    ssh node01 start-yarn.sh
    sleep 4s

    echo ----------------启动RS------------------
    ssh node02 yarn-daemon.sh start resourcemanager
    sleep 1s
    ssh node03 yarn-daemon.sh start resourcemanager
    
	echo ----------------启动历史服务器------------------
	for i in node01 node02 node03 node04
	do
        ssh $i mr-jobhistory-daemon.sh start historyserver
        sleep 1s
	done

    #sleep 4s
    #echo ----------------启动服务端hive------------------
    #./hive-start.sh start

    #sleep 1s
    #echo ----------------启动Hbase------------------
    #start-hbase.sh
} 

stop() { 
    #关闭hadoop ha集群

    echo ----------------关闭DFS------------------
    ssh node01 stop-dfs.sh
    sleep 10s

    echo ----------------关闭YARN------------------
    ssh node01 stop-yarn.sh
    sleep 4s

    echo ----------------关闭RS------------------
    ssh node02 yarn-daemon.sh stop resourcemanager
    sleep 1s
    ssh node03 yarn-daemon.sh stop resourcemanager

	echo ----------------关闭历史服务器------------------
	for i in node01 node02 node03 node04
	do
        ssh $i  mr-jobhistory-daemon.sh stop historyserver
        sleep 1s
	done

    echo ----------------关闭zk集群------------------
    for i in node02 node03 node04
    do
            ssh $i zkServer.sh stop
            sleep 1s
    done

	echo ----------------关机------------------
    for i in node{02..04} node01
    do
            ssh $i poweroff
    done

} 

case "$1" in 
    start) 
        $1 
        ;; 
    stop) 
        $1 
        ;; 
    *)    
      echo $"Usage: $0 {start|stop}" 
        exit 2 
esac
```

