# Sqoop

## 定义

Sqoop是一款开源的工具，主要用于在Hadoop(Hive)与传统的数据库(mysql、postgresql...)间进行数据的传递，可以将一个关系型数据库（例如 ： MySQL ,Oracle ,Postgres等）中的数据导进到Hadoop的HDFS中，也可以将HDFS的数据导进到关系型数据库中

## 节点规划

| node03                               |
| ------------------------------------ |
| sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz |

本实例中hadoop集群使用 hadoop-2.9.2的版本，所以 sqoop选用 hadoop-2.x的版本。下载地址

 http://mirrors.hust.edu.cn/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz

## 安装

```shell
# 下载
[root@node04 ~]# wget http://mirrors.hust.edu.cn/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz

# 解压
[root@node04 ~]# tar -zxf sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz -C /opt/stanlong/sqoop/

# 重命名
[root@node04 sqoop]# pwd
/opt/stanlong/sqoop
[root@node04 sqoop]# ll
total 0
drwxr-xr-x 9 root root 329 Aug  2  2014 sqoop-1.4.7.bin__hadoop-2.6.0
[root@node04 sqoop]# mv sqoop-1.4.7.bin__hadoop-2.6.0/ sqoop
[root@node04 sqoop]# ll
total 0
drwxr-xr-x 9 root root 329 Aug  2  2014 sqoop

```

## 修改配置文件

```shell
[root@node04 conf]# pwd
/opt/stanlong/sqoop/sqoop/conf
[root@node04 conf]# cp sqoop-env-template.sh sqoop-env.sh 
[root@node04 conf]# vi sqoop-env.sh 
```

```sh
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# included in all the hadoop scripts with source command
# should not be executable directly
# also should not be passed any arguments, since we need original $*

# Set Hadoop-specific environment variables here.

#Set path to where bin/hadoop is available
export HADOOP_COMMON_HOME=/opt/stanlong/hadoop-ha/hadoop-2.9.2

#Set path to where hadoop-*-core.jar is available
export HADOOP_MAPRED_HOME=/opt/stanlong/hadoop-ha/hadoop-2.9.2

#set the path to where bin/hbase is available
export HBASE_HOME=/opt/stanlong/hbase/hbase-1.3.6

#Set the path to where bin/hive is available
export HIVE_HOME=/opt/stanlong/hive/apache-hive-1.2.2-bin

#Set the path for where zookeper config dir is
export ZOOCFGDIR=/opt/stanlong/zookeeper-3.4.11
```

## 配置环境变量

```shell
[root@node04 sqoop]# pwd
/opt/stanlong/sqoop/sqoop
[root@node04 sqoop]# vi /etc/profile

export SQOOP_HOME=/opt/stanlong/sqoop/sqoop # Sqoop 环境变量
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin:$HBASE_HOME/bin:$SQOOP_HOME/bin

# 使环境变量生效
[root@node04 sqoop]# source /etc/profile
[root@node04 sqoop]# sq # 按tab命令可自动补全则环境变量配置成功
sqlite3                  sqoop-create-hive-table  sqoop-help               sqoop-job                sqoop-merge              
sqoop                    sqoop-eval               sqoop-import             sqoop-list-databases     sqoop-metastore          
sqoop-codegen            sqoop-export             sqoop-import-all-tables  sqoop-list-tables        sqoop-version            
```

## 拷贝mysql驱动

```shell
[root@node04 ~]# mv mysql-connector-java-5.1.37.jar /opt/stanlong/sqoop/sqoop/lib/
```

## 拷贝jar包

```shell
[root@node04 lib]# pwd
/opt/stanlong/hive/apache-hive-1.2.2-bin/lib

[root@node04 lib]# cp hive-common-1.2.2.jar /opt/stanlong/sqoop/sqoop/lib/
[root@node04 lib]# cp hive-shims* /opt/stanlong/sqoop/sqoop/lib/
```

## 测试数据库连接

```shell
[root@node04 lib]# sqoop list-databases --connect jdbc:mysql://node01:3306/ --username root --password root
Warning: /opt/stanlong/sqoop/sqoop/../hcatalog does not exist! HCatalog jobs will fail.
Please set $HCAT_HOME to the root of your HCatalog installation.
Warning: /opt/stanlong/sqoop/sqoop/../accumulo does not exist! Accumulo imports will fail.
Please set $ACCUMULO_HOME to the root of your Accumulo installation.
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/opt/stanlong/hbase/hbase-1.3.6/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
21/02/26 06:28:39 INFO sqoop.Sqoop: Running Sqoop version: 1.4.7
21/02/26 06:28:39 WARN tool.BaseSqoopTool: Setting your password on the command-line is insecure. Consider using -P instead.
21/02/26 06:28:40 INFO manager.MySQLManager: Preparing to use a MySQL streaming resultset.
information_schema
hivedb
mysql
performance_schema
sys
```

## 导入数据

在Sqoop中，“导入”概念指：从非大数据集群（RDBMS）向大数据集群（HDFS，HIVE，HBASE）中传输数据，叫做：导入，即使用import关键字

如：

```shell
sqoop import \ 
--connect jdbc:mysql://node01:3306/gmall \
--username root \
--password root \
--table ads_uv_count \
[--columns 列名, 不指定列名表示同步表里的全部数据]
[--where 'id>=1 and id<=20']
[--target-dir 如果没有指定hive表名，则需要指定hdfs路径]
--delete-target-dir \ 
--input-fields-terminated-by "\t" \
--num-mappers 1 \ 
--split-by id \
--hive-import   --hive-overwrite --hive-table ads_uv_count

-- sql语句的写法
sqoop import \ 
--connect jdbc:mysql://node01:3306/gmall \
--username root \
--password root \
--query 'select id login_name from ads_uv_count where id>=1 and id<=20 and $CONDITIONS' \
--delete-target-dir \ 
--input-fields-terminated-by "\t" \
--num-mappers 1 \ 
--split-by id \
--hive-import   --hive-overwrite --hive-table ads_uv_count
```

## 导出数据

在Sqoop中，“导出”概念指：从大数据集群（HDFS，HIVE，HBASE）向非大数据集群（RDBMS）中传输数据，叫做：导出，即使用export关键字

如：

```shell
sqoop export --connect jdbc:mysql://node01:3306/gmall --username root --password root --table ads_uv_count --num-mappers 1 --export-dir /warehouse/gmall/ads/ads_uv_count --input-fields-terminated-by "\t"
```

## 常用命令和参数

### 常用命令

| 序号 | **命令**          | **说明**                                                     |
| ---- | ----------------- | ------------------------------------------------------------ |
| 1    | import            | 将数据导入到集群                                             |
| 2    | export            | 将集群数据导出                                               |
| 3    | codegen           | 获取数据库中某张表数据生成Java并打包Jar                      |
| 4    | create-hive-table | 创建Hive表                                                   |
| 5    | eval              | 查看SQL执行结果                                              |
| 6    | import-all-tables | 导入某个数据库下所有表到HDFS中                               |
| 7    | job               | 用来生成一个sqoop的任务，生成后，该任务并不执行，除非使用命令执行该任务。 |
| 8    | list-databases    | 列出所有数据库名                                             |
| 9    | list-tables       | 列出某个数据库下所有表                                       |
| 10   | merge             | 将HDFS中不同目录下面的数据合在一起，并存放在指定的目录中     |
| 11   | metastore         | 记录sqoop job的元数据信息，如果不启动metastore实例，则默认的元数据存储目录为：~/.sqoop，如果要更改存储目录，可以在配置文件sqoop-site.xml中进行更改。 |
| 12   | help              | 打印sqoop帮助信息                                            |
| 13   | version           | 打印sqoop版本信息                                            |

### 公用参数

#### 数据库连接

| **序号** | **参数**             | **说明**               |
| -------- | -------------------- | ---------------------- |
| 1        | --connect            | 连接关系型数据库的URL  |
| 2        | --connection-manager | 指定要使用的连接管理类 |
| 3        | --driver             | Hadoop根目录           |
| 4        | --help               | 打印帮助信息           |
| 5        | --password           | 连接数据库的密码       |
| 6        | --username           | 连接数据库的用户名     |
| 7        | --verbose            | 在控制台打印出详细信息 |

#### import

| **序号** | **参数**                 | **说明**                                                     |
| -------- | ------------------------ | ------------------------------------------------------------ |
| 1        | --enclosed-by            | 给字段值前加上指定的字符                                     |
| 2        | --escaped-by             | 对字段中的双引号加转义符                                     |
| 3        | --fields-terminated-by   | 设定每个字段是以什么符号作为结束，默认为逗号                 |
| 4        | --lines-terminated-by    | 设定每行记录之间的分隔符，默认是\n                           |
| 5        | --mysql-delimiters       | Mysql默认的分隔符设置，字段之间以逗号分隔，行之间以\n分隔，默认转义符是\，字段值以单引号包裹。 |
| 6        | --optionally-enclosed-by | 给带有双引号或单引号的字段值前后加上指定字符。               |
| 7        | --delete-target-dir      | 如果指定目录存在，则先删除掉                                 |

#### export

| **序号** | **参数**                       | **说明**                                   |
| -------- | ------------------------------ | ------------------------------------------ |
| 1        | --input-enclosed-by            | 对字段值前后加上指定字符                   |
| 2        | --input-escaped-by             | 对含有转移符的字段做转义处理               |
| 3        | --input-fields-terminated-by   | 字段之间的分隔符                           |
| 4        | --input-lines-terminated-by    | 行之间的分隔符                             |
| 5        | --input-optionally-enclosed-by | 给带有双引号或单引号的字段前后加上指定字符 |

#### hive

| **序号** | **参数**                  | **说明**                                                  |
| -------- | ------------------------- | --------------------------------------------------------- |
| 1        | --hive-delims-replacement | 用自定义的字符串替换掉数据中的\r\n和\013 \010等字符       |
| 2        | --hive-drop-import-delims | 在导入数据到hive时，去掉数据中的\r\n\013\010这样的字符    |
| 3        | --map-column-hive         | 生成hive表时，可以更改生成字段的数据类型                  |
| 4        | --hive-partition-key      | 创建分区，后面直接跟分区名，分区字段的默认类型为string    |
| 5        | --hive-partition-value    | 导入数据时，指定某个分区的值                              |
| 6        | --hive-home               | hive的安装目录，可以通过该参数覆盖之前默认配置的目录      |
| 7        | --hive-import             | 将数据从关系数据库中导入到hive表中                        |
| 8        | --hive-overwrite          | 覆盖掉在hive表中已经存在的数据                            |
| 9        | --create-hive-table       | 默认是false，即，如果目标表已经存在了，那么创建任务失败。 |
| 10       | --hive-table              | 后面接要创建的hive表,默认使用MySQL的表名                  |
| 11       | --table                   | 指定关系数据库的表名                                      |

### 特有参数

#### import

将关系型数据库中的数据导入到HDFS（包括Hive，HBase）中，如果导入的是Hive，那么当Hive中没有对应表时，则自动创建

| **序号** | **参数**                      | **说明**                                                     |
| -------- | ----------------------------- | ------------------------------------------------------------ |
| 1        | --append                      | 将数据追加到HDFS中已经存在的DataSet中，如果使用该参数，sqoop会把数据先导入到临时文件目录，再合并。 |
| 2        | --as-avrodatafile             | 将数据导入到一个Avro数据文件中                               |
| 3        | --as-sequencefile             | 将数据导入到一个sequence文件中                               |
| 4        | --as-textfile                 | 将数据导入到一个普通文本文件中                               |
| 5        | --boundary-query              | 边界查询，导入的数据为该参数的值（一条sql语句）所执行的结果区间内的数据。 |
| 6        | --columns  <col1, col2, col3> | 指定要导入的字段                                             |
| 7        | --direct                      | 直接导入模式，使用的是关系数据库自带的导入导出工具，以便加快导入导出过程。 |
| 8        | --direct-split-size           | 在使用上面direct直接导入的基础上，对导入的流按字节分块，即达到该阈值就产生一个新的文件 |
| 9        | --inline-lob-limit            | 设定大对象数据类型的最大值                                   |
| 10       | --m或–num-mappers             | 启动N个map来并行导入数据，默认4个。当-m 设置的值大于1时，split-by 为必须设置字段（需要是 int 类型的字段），如果不是 int类型的字段，则需要加上参数 |
| 11       | --query或--e                  | 将查询结果的数据导入，使用时必须伴随参--target-dir，--hive-table，如果查询中有where条件，则条件后必须加上$CONDITIONS关键字 |
| 12       | --split-by                    | 按照某一列来切分表的工作单元，不能与--autoreset-to-one-mapper连用（请参考官方文档） |
| 13       | --table                       | 关系数据库的表名                                             |
| 14       | --target-dir                  | 指定HDFS路径                                                 |
| 15       | --warehouse-dir               | 与14参数不能同时使用，导入数据到HDFS时指定的目录             |
| 16       | --where                       | 从关系数据库导入数据时的查询条件                             |
| 17       | --z或--compress               | 允许压缩                                                     |
| 18       | --compression-codec           | 指定hadoop压缩编码类，默认为gzip(Use Hadoop codec default gzip) |
| 19       | --null-string                 | string类型的列如果null，替换为指定字符串                     |
| 20       | --null-non-string             | 非string类型的列如果null，替换为指定字符串                   |
| 21       | --check-column                | 作为增量导入判断的列名                                       |
| 22       | --incremental                 | mode：append或lastmodified，增量导入数据到hdfs中，mode=lastmodified。使用lastmodified方式导入数据要指定增量数据是要--append（追加）还是要--merge-key（合并） |
| 23       | --last-value                  | 指定某一个值，用于标记增量导入的位置 。last-value指定的值是会包含于增量导入的数据中 |

#### export

从HDFS（包括Hive和HBase）中将数据导出到关系型数据库中

| **序号** | **参数**                | **说明**                                                     |
| -------- | ----------------------- | ------------------------------------------------------------ |
| 1        | --direct                | 利用数据库自带的导入导出工具，以便于提高效率                 |
| 2        | --export-dir            | 存放数据的HDFS的源目录                                       |
| 3        | -m或--num-mappers       | 启动N个map来并行导入数据，默认4个                            |
| 4        | --table                 | 指定导出到哪个RDBMS中的表                                    |
| 5        | --update-key            | 对某一列的字段进行更新操作                                   |
| 6        | --update-mode           | updateonly  allowinsert(默认)                                |
| 7        | --input-null-string     | 请参考import该类似参数说明                                   |
| 8        | --input-null-non-string | 请参考import该类似参数说明                                   |
| 9        | --staging-table         | 创建一张临时表，用于存放所有事务的结果，然后将所有事务结果一次性导入到目标表中，防止错误。 |
| 10       | --clear-staging-table   | 如果第9个参数非空，则可以在导出操作执行前，清空临时事务结果表 |

#### codegen

将关系型数据库中的表映射为一个Java类，在该类中有各列对应的各个字段

| **序号** | **参数**                | **说明**                                                     |
| -------- | ----------------------- | ------------------------------------------------------------ |
| 1        | --bindir                | 指定生成的Java文件、编译成的class文件及将生成文件打包为jar的文件输出路径 |
| 2        | --class-name            | 设定生成的Java文件指定的名称                                 |
| 3        | --outdir                | 生成Java文件存放的路径                                       |
| 4        | --package-name          | 包名，如com.z，就会生成com和z两级目录                        |
| 5        | --input-null-non-string | 在生成的Java文件中，可以将null字符串或者不存在的字符串设置为想要设定的值（例如空字符串） |
| 6        | --input-null-string     | 将null字符串替换成想要替换的值（一般与5同时使用）            |
| 7        | --map-column-java       | 数据库字段在生成的Java文件中会映射成各种属性，且默认的数据类型与数据库类型保持对应关系。该参数可以改变默认类型，例如：--map-column-java id=long, name=String |
| 8        | --null-non-string       | 在生成Java文件时，可以将不存在或者null的字符串设置为其他值   |
| 9        | --null-string           | 在生成Java文件时，将null字符串设置为其他值（一般与8同时使用） |
| 10       | --table                 | 对应关系数据库中的表名，生成的Java文件中的各个属性与该表的各个字段一一对应 |

#### create-hive-table

生成与关系数据库表结构对应的hive表结构

| **序号** | **参数**            | **说明**                                              |
| -------- | ------------------- | ----------------------------------------------------- |
| 1        | --hive-home         | Hive的安装目录，可以通过该参数覆盖掉默认的Hive目录    |
| 2        | --hive-overwrite    | 覆盖掉在Hive表中已经存在的数据                        |
| 3        | --create-hive-table | 默认是false，如果目标表已经存在了，那么创建任务会失败 |
| 4        | --hive-table        | 后面接要创建的hive表                                  |
| 5        | --table             | 指定关系数据库的表名                                  |

#### eval

可以快速的使用SQL语句对关系型数据库进行操作，经常用于在import数据之前，了解一下SQL语句是否正确，数据是否正常，并可以将结果显示在控制台

| **序号** | **参数**     | **说明**          |
| -------- | ------------ | ----------------- |
| 1        | --query或--e | 后跟查询的SQL语句 |

#### import-all-tables

可以将RDBMS中的所有表导入到HDFS中，每一个表都对应一个HDFS目录 。

注意`import-all-tables`和它左边的`--`之间有一个空格

| **序号** | **参数**            | **说明**                               |
| -------- | ------------------- | -------------------------------------- |
| 1        | --as-avrodatafile   | 这些参数的含义均和import对应的含义一致 |
| 2        | --as-sequencefile   |                                        |
| 3        | --as-textfile       |                                        |
| 4        | --direct            |                                        |
| 5        | --direct-split-size |                                        |
| 6        | --inline-lob-limit  |                                        |
| 7        | --m或—num-mappers   |                                        |
| 8        | --warehouse-dir     |                                        |
| 9        | -z或--compress      |                                        |
| 10       | --compression-codec |                                        |

#### job

用来生成一个sqoop任务，生成后不会立即执行，需要手动执行

| **序号** | **参数**       | **说明**                 |
| -------- | -------------- | ------------------------ |
| 1        | --create       | 创建job参数              |
| 2        | --delete       | 删除一个job              |
| 3        | --exec         | 执行一个job              |
| 4        | --help         | 显示job帮助              |
| 5        | --list         | 显示job列表              |
| 6        | --meta-connect | 用来连接metastore服务    |
| 7        | --show         | 显示一个job的信息        |
| 8        | --verbose      | 打印命令运行时的详细信息 |

在执行一个job时，如果需要手动输入数据库密码，可以做如下优化

```xml
<property>
	<name>sqoop.metastore.client.record.password</name>
	<value>true</value>
	<description>If true, allow saved passwords in the metastore.</description>
</property>
```

#### merge

将HDFS中不同目录下面的数据合并在一起并放入指定目录中

| **序号** | **参数**     | **说明**                                               |
| -------- | ------------ | ------------------------------------------------------ |
| 1        | --new-data   | HDFS 待合并的数据目录，合并后在新的数据集中保留        |
| 2        | --onto       | HDFS合并后，重复的部分在新的数据集中被覆盖             |
| 3        | --merge-key  | 合并键，一般是主键ID                                   |
| 4        | --jar-file   | 合并时引入的jar包，该jar包是通过Codegen工具生成的jar包 |
| 5        | --class-name | 对应的表名或对象名，该class类是包含在jar包中的         |
| 6        | --target-dir | 合并后的数据在HDFS里存放的目录                         |