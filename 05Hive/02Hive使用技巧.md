# Hive使用技巧

## 一、Hive常用交互命令

```shell
[root@node02 ~]# hive -help
usage: hive
 -d,--define <key=value>          Variable subsitution to apply to hive
                                  commands. e.g. -d A=B or --define A=B
    --database <databasename>     Specify the database to use
 -e <quoted-query-string>         SQL from command line 
 -f <filename>                    SQL from files
 -H,--help                        Print help information
    --hiveconf <property=value>   Use value for given property
    --hivevar <key=value>         Variable subsitution to apply to hive
                                  commands. e.g. --hivevar A=B
 -i <filename>                    Initialization SQL file
 -S,--silent                      Silent mode in interactive shell
 -v,--verbose                     Verbose mode (echo executed SQL to the
                                  console)
```

### 1、-e 命令行交互

直接在命令行执行sql语句

```shell
[root@node02 ~]# hive -e "show databases;"

Logging initialized using configuration in jar:file:/opt/stanlong/hive/apache-hive-1.2.2-bin/lib/hive-common-1.2.2.jar!/hive-log4j.properties
OK
default
Time taken: 2.766 seconds, Fetched: 1 row(s)
```

### 2、-f 脚本交互

执行脚本中sql语句

```shell
[root@node02 ~]# vi show.sql
show databases;
[root@node02 ~]# hive -f show.sql
Logging initialized using configuration in jar:file:/opt/stanlong/hive/apache-hive-1.2.2-bin/lib/hive-common-1.2.2.jar!/hive-log4j.properties
OK
default
Time taken: 2.834 seconds, Fetched: 1 row(s)
```

## 二、Hive参数的配置方式

查看当前所有参数的配置信息

```
| system:sun.boot.library.path=/usr/lib/jvm/jdk-1.8.0_421-oracle-x64/jre/lib/amd64 |
+----------------------------------------------------+
|                        set                         |
+----------------------------------------------------+
| system:sun.cpu.endian=little                       |
| system:sun.cpu.isalist=                            |
| system:sun.io.unicode.encoding=UnicodeLittle       |
| system:sun.java.command=org.apache.hadoop.util.RunJar /opt/hive-3.1.3/lib/hive-service-3.1.3.jar org.apache.hive.service.server.HiveServer2 --hiveconf hive.aux.jars.path=file:///opt/hadoop-3.4.0/share/hadoop/common/hadoop-common-3.4.0.jar,file:///opt/hadoop-3.4.0/share/hadoop/common/hadoop-common-3.4.0-tests.jar,file:///opt/hadoop-3.4.0/share/hadoop/common/hadoop-kms-3.4.0.jar,file:///opt/hadoop-3.4.0/share/hadoop/common/hadoop-nfs-3.4.0.jar,file:///opt/hadoop-3.4.0/share/hadoop/common/hadoop-registry-3.4.0.jar |
| system:sun.java.launcher=SUN_STANDARD              |
| system:sun.jnu.encoding=UTF-8                      |
| system:sun.management.compiler=HotSpot 64-Bit Tiered Compilers |
| system:sun.os.patch.level=unknown                  |
| system:user.country=US                             |
| system:user.dir=/root                              |
| system:user.home=/root                             |
| system:user.language=en                            |
| system:user.name=root                              |
| system:user.timezone=Asia/Shanghai                 |
| system:yarn.home.dir=/opt/hadoop-3.4.0             |
| system:yarn.log.dir=/opt/hadoop-3.4.0/logs         |
| system:yarn.log.file=hadoop.log                    |
| system:yarn.root.logger=INFO,console               |
+----------------------------------------------------+
```

### 1、配置文件

- 默认配置文件 hive-default.xml 
- 用户自定义配置文件 hive-site.xml

用户自定义配置会覆盖默认配置。另外，Hive也会读入 Hadoop的配置，因为Hive是作为Hadoop的客户端启动的，Hive的配置会覆盖Hadoop的配置。配置文件里的设定对本机启动的所有Hive进程都有效

### 2、命令行传参

启动 Hive 时，可以在命令行添加-hiveconf param=value 来设定参数。(这种方式配置，仅对当前启动的 Hive 客户端窗口有效，这种配置的优先级会比 hive-site.xml 中配置的优先级高)

```sql
[root@node02 ~]# hive -hiveconf mapreduce.job.reduce=10;

hive (default)> set mapreduce.job.reduce;
mapreduce.job.reduce=10
```

### 3、参数声明

进入 Hive 客户端，通过 set hive.cli.print.header=false 方式，类似参数声明的方式来进行配置。这种方式的配置，只对声明后的语句有效

```shell
hive (default)> set mapreduce.job.reduce=5;
hive (default)> set mapreduce.job.reduce;
mapreduce.job.reduce=5
```

上述三种设定方式的优先级依次递增。即 **配置文件 < 命令行参数 < 参数声明**。注意某些系统级的参数，例如 log4j 相关的设定，必须用前两种方式设定，因为那些参数的读取在会话建立以前已经完成了。

## 三、Hive日志

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

## 四、Hive的JVM堆内存设置

新版本的Hive启动的时候，默认申请的JVM堆内存大小为256M，JVM堆内存申请的太小，导致后期开启本地模式，执行复杂的SQL时经常会报错：java.lang.OutOfMemoryError: Java heap space，因此最好提前调整一下HADOOP_HEAPSIZE这个参数。

### 1、修改 hive-env.sh

```shell
[root@node01 ~]# vi $HIVE_HOME/conf/hive-env.sh
export HADOOP_HEAPSIZE=2048 
```

### 2、关闭hadoop的yarn虚拟内存检查

```shell
[root@node01 ~]# vim $HADOOP_HOME/etc/hadoop/yarn-site.xml 
<!--关闭虚拟内存校验-->
<property>
	<name>yarn.nodemanager.vmem-check-enabled</name>
	<value>false</value>
</property>
```

## 五、Hive其他操作命令

### 1、执行HDFS命令

```shell
hive> dfs -ls /;
Found 4 items
-rw-r--r--   2 root supergroup         39 2021-01-22 05:13 /student.txt
drwx-w----   - root supergroup          0 2021-01-22 05:58 /tmp
drwxr-xr-x   - root supergroup          0 2021-01-23 18:42 /user
drwxr-xr-x   - root supergroup          0 2021-01-22 05:35 /wc
```

### 2、查看本地文件系统

```shell
hive> ! ls /opt/stanlong/;
hadoop-cluster
hadoop-ha
hive
tomcat
zookeeper-3.4.11
```

### 3、查看hive的历史命令

```shell
[root@node02 ~]# ll -a
total 72
dr-xr-x---.  5 root root   263 Jan 25 06:02 .
dr-xr-xr-x. 17 root root   244 Jan  1 20:39 ..
-rw-r--r--   1 root root   194 Jan 25 06:08 .hivehistory # hive历史命令
[root@node02 ~]# cat .hivehistory 
show databases;
use default;
show tables;
drop table hehe;
create table hehe(id String);
insert into hehe values ("Hello Hive");
select * from hehe;
quit;
dfs -ls /;
! ls /opt/stanlong/;
quit;
```



