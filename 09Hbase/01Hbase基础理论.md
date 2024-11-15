# Hbase

## 定义

Hbase 是一种分布式、可扩展、支持海量数据存贮的NoSQL数据库

## 特点

1. **海量存储**

   Hbase适合存储PB级别的海量数据，在PB级别的数据以及采用廉价PC存储的情况下，能在几十到百毫秒内返回数据。这与Hbase的极易扩展性息息相关。正式因为Hbase良好的扩展性，才为海量数据的存储提供了便利

2. **列式存储**

   这里的列式存储其实说的是列族（ColumnFamily）存储，Hbase是根据列族来存储数据的。列族下面可以有非常多的列，列族在创建表的时候就必须指定

3. **极易扩展**

   Hbase的扩展性主要体现在两个方面，一个是基于上层处理能力（RegionServer）的扩展，一个是基于存储的扩展（HDFS）。
    通过横向添加RegionSever的机器，进行水平扩展，提升Hbase上层的处理能力，提升Hbsae服务更多Region的能力。

   备注：RegionServer的作用是管理region、承接业务的访问，这个后面会详细的介绍通过横向添加Datanode的机器，进行存储层扩容，提升Hbase的数据存储能力和提升后端存储的读写能力

4. **高并发（多核）**

   由于目前大部分使用Hbase的架构，都是采用的廉价PC，因此单个IO的延迟其实并不小，一般在几十到上百ms之间。这里说的高并发，主要是在并发的情况下，Hbase的单个IO延迟下降并不多。能获得高并发、低延迟的服务

5. **稀疏**

   稀疏主要是针对Hbase列的灵活性，在列族中，你可以指定任意多的列，在列数据为空的情况下，是不会占用存储空间的。

## 逻辑结构

![](./doc/02.png)

- row key ：行id， 图中有4个row key
- region：行的集合， 图中有两个 region
- 列族：列的集合， 图中有两个列族
- store：region和列族的交集，图中有四个 stroe

## 存储结构

![](./doc/03.png)

## 数据模型

逻辑上，Hbase的数据模型同关系型数据库很类似，有行有列，数据存储在一张表中。但从Hbase的底层物理存储结构（K-V）来看，Hbase更向是一个 multi-dimensional map

**1. Name Space**

命名空间，类似于关系型数据库的 DataBase 概念，每个命名空间下有多个表。HBase有两个自带的命名空间，分别是"hbase"和"default"，"hbase"中存放的是HBase内置的表，"default"表是用户默认使用的命名空间。

**2. Region**

Region 的概念和关系型数据库的分区或分片类似。

Region 是 HBase中 分布式存储 和 负载均衡 的最小单元，Region 包括完整的行，所以 Region 是以行为单位 表的一个子集。不同的 Region 可以分别在不同的 RegionServer 上。

每个表一开始只有一个 Region，每个 Region 会保存一个表里 某段连续的数据，随着数据不断插 入表，Region不断增大，当增大到一个阀值的时候，Region就会二等分，生成两个新的Region；

Table 中的所有行都按照 RowKsey 的字典序排列，Table 在行的方向上分割为多个 Region，基于 Rowkey 的不同范围分配到不通的 Region 中（Rowkey的范围，第一个Rowkey的起始索引 和 最后一Rowkey的结束索引为空串`" "`，每个Region是前闭后开`[起始索引, 结束索引)` ）

**3. Row**

HBase 表中的每行数据都由 一个 RowKey 和 一个或多个 Column（列）组成，数据是按照 RowKey 的字典顺序存储的，并且查询数据时只能根据 RowKey 进行检索，所以 RowKey 的设计十分重要。

**4. Row Key**

Rowkey 的概念和 mysql 中的主键类似，Hbase 使用 Rowkey 来唯一的区分某一行的数据。Hbase只支持3种查询方式： 1、基于Rowkey的单行查询，2、基于Rowkey的范围扫描 ，3、全表扫描

因此，Rowkey对Hbase的性能影响非常大。设计的时候要兼顾基于Rowkey的单行查询也要键入Rowkey的范围扫描。

Rowkey 行键可以是任意字符串(最大长度是64KB，实际应用中长度一般为 10-100bytes)，最好是16。在HBase 内部，Rowkey 保存为字节数组。HBase会对表中的数据按照 Rowkey 字典序排序

**5. Column Family（列簇）**

Hbase 通过列簇划分数据的存储，列簇下面可以包含任意多的列，实现灵活的数据存取。列簇是由一个一个的列组成（任意多），在列数据为空的情况下，不会占用存储空间。

Hbase 创建表的时候必须指定列簇。就像关系型数据库创建的时候必须指定具体的列是一样的。

Hbase的列簇不是越多越好，官方推荐的是列簇最好小于或者等于3。一般是1个列簇。

新的列簇成员（列）可以随后动态加入，Family下面可以有多个Qualifier，所以可以简单的理解为，HBase中的列是二级列，也就是说Family是第一级列，Qualifier是第二级列。

权限控制、存储以及调优都是在列簇层面进行的；

HBase把同一列簇里面的数据存储在同一目录下，由几个文件保存。

**6. Column**

HBase中的每个列，由列簇加上列限定符组成，一般是“列簇：列标识（column family : column qualifier）”，例如 info : name，info : age。创建表的时候只需指明列簇，不用指定列标识。

列限定符属于数据的一部分，每条数据的列限定符都可以不一样

**7. Time Stamp（version）**

用于标识数据的不同版本（version），针对每个列簇进行设置（若列簇的version=3，那它下面的所有列的version都有3个版本），若建表时没有指定version，默认值version=1

在写入数据的时候，如果没有指定相应的timestamp，HBase会自动添加一个timestamp（默认与服务器时间保持一致）。timestamp也可以由客户显式赋值，如果应用程序要避免数据版本冲突，就必须自己生成具有唯一性的时间戳。

每个cell中，不同版本的数据按照时间倒序排序，即最新的数据排在最前面。

为了避免数据存在过多版本造成的的管理(包括存贮和索引)负担，hbase 提供了两种数据版本回收方式： 

- 保存数据的最后 n 个版本 ； 
- 保存最近一段时间内的版本（设置数据的生命周期 TTL）。

**8. Cell**

由{Rowkey，ColumnFamily : ColumnQualifier, TimeStamp} 确定唯一的cell。

cell 中的所有字段数据 都没有数据类型，全部是字节码形式存储。被视为字节数组`byte[]`

## 架构

![](./doc/01.png)

## 写流程

![](./doc/06.png)

1. Client 向ZK集群发送请求，获取hbase:meta表所在的RS

   ```shell
   [zk: localhost:2181(CONNECTED) 4] get /hbase/meta-region-server
   �regionserver:16020q���]>�"PBUF
    
   node02�}�Ŝ��. # RS在node02上 
   cZxid = 0x280000003c
   ctime = Sun Jan 31 00:37:04 CST 2021
   mZxid = 0x280000003c
   mtime = Sun Jan 31 00:37:04 CST 2021
   pZxid = 0x280000003c
   cversion = 0
   dataVersion = 0
   aclVersion = 0
   ephemeralOwner = 0x0
   dataLength = 59
   numChildren = 0
   ```

2. ZK集群返回meta所在的RS信息

   ```shell
   hbase(main):001:0> scan 'hbase:meta'
   ROW                                              COLUMN+CELL                                                                                                                                   
    hbase:namespace,,1611617957334.1e05edc11286e62e column=info:regioninfo, timestamp=1612024631312, value={ENCODED => 1e05edc11286e62e4d41282bda935a58, NAME => 'hbase:namespace,,1611617957334.1
    4d41282bda935a58.                               e05edc11286e62e4d41282bda935a58.', STARTKEY => '', ENDKEY => ''}                                                                              
    hbase:namespace,,1611617957334.1e05edc11286e62e column=info:seqnumDuringOpen, timestamp=1612024631312, value=\x00\x00\x00\x00\x00\x00\x00\x16                                                 
    4d41282bda935a58.                                                                                                                                                                             
    hbase:namespace,,1611617957334.1e05edc11286e62e column=info:server, timestamp=1612024631312, value=node02:16020                                                                               
    4d41282bda935a58.                                                                                                                                                                             
    hbase:namespace,,1611617957334.1e05edc11286e62e column=info:serverstartcode, timestamp=1612024631312, value=1612024586931                                                                     
    4d41282bda935a58.                                                                                                                                                                             
    person,,1611787567290.e44c249012cb98443bb76576f column=info:regioninfo, timestamp=1612024632023, value={ENCODED => e44c249012cb98443bb76576f6d5143f, NAME => 'person,,1611787567290.e44c249012
    6d5143f.                                        cb98443bb76576f6d5143f.', STARTKEY => '', ENDKEY => ''}                                                                                       
    person,,1611787567290.e44c249012cb98443bb76576f column=info:seqnumDuringOpen, timestamp=1612024632023, value=\x00\x00\x00\x00\x00\x00\x00\x06                                                 
    6d5143f.                                                                                                                                                                                      
    person,,1611787567290.e44c249012cb98443bb76576f column=info:server, timestamp=1612024632023, value=node03:16020                                                                               
    6d5143f.                                                                                                                                                                                      
    person,,1611787567290.e44c249012cb98443bb76576f column=info:serverstartcode, timestamp=1612024632023, value=1612024587625                                                                     
    6d5143f.                                                                                                                                                                                      
   2 row(s) in 1.0630 seconds
   ```

3. Client向HregionServer发送写请求；

4. HregionServer将数据写到HLog（write ahead log）。为了数据的持久化和恢复；

5. HregionServer将数据写到内存（MemStore）；

6. 反馈Client写成功。

## 读流程

![](./doc/07.png)

1）Client先访问zookeeper，从meta表读取region的位置，然后读取meta表中的数据。meta中又存储了用户表的region信息；

2）根据namespace、表名和rowkey在meta表中找到对应的region信息；

3）找到这个region对应的regionserver；

4）查找对应的region；

5）先从MemStore找数据，如果没有，再到BlockCache里面读；

6）BlockCache还没有，再到StoreFile上读(为了读取的效率)；

7）如果是从StoreFile里面读取的数据，不是直接返回给客户端，而是先写入BlockCache，再返回给客户端。










