# 数据导入导出

## 一、Load

Load 语句可将文件导入到Hive表中

### 1、语法

```mysql
LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)];
```

**关键字说明**：

- `local`：表示从本地加载数据到Hive表，相当于put；否则从HDFS移动数据到Hive表，相当于move。
- `overwrite`：表示覆盖表中已有数据，否则表示追加。
- `partition`：表示上传到指定分区，若目标是分区表，需指定分区。

### 2、案例

测试数据（默认都是用tab键分隔）

```shell
[root@node01 ~]# vi student.txt
1001	沈万三
1002	朱元璋
1003	陆春香
```

测试表，指定分隔符 \t, 存储格式为 TEXTFILE

```mysql
create table student 
(
    id string
   ,name string
)row format delimited fields terminated by '\t' 
STORED AS TEXTFILE;
```

- **load 本地数据到hive表**

  ```mysql
  hive> load data local inpath '/root/student.txt' into table student;
  
  ----------------------------------------------------------------------------------
  hive> select * from student;
  OK
  1001	沈万三
  1002	朱元璋
  1003	陆春香
  Time taken: 0.659 seconds, Fetched: 3 row(s)
  ```

  这种方式导数据到hive集群多半会报错： Invalid path： No files matching path file：这是因为 hive导入数据语句  load data [local] inpath ，是一个服务器端的指令，它是在服务器端执行。因此指定local时表明加载的文件为本地文件，但是这里的local，在hive中指的是 hiveserver 服务所在的机器，而不是hivecli 或 beeline客户端所在的机器（生产环境大都是 hiveserver 和 hivecli不在同一个机器）

- **load HDFS上的数据到hive表**

  先将数据传到HDFS上的任意目录

  ```shell
  hdfs dfs -put student.txt /
  ```

  再将数据load进表里， 注意这种方式相当于一个移动操作， 将 /student.txt 移动到  student 表所在路径

  ```shell
  hive> load data inpath '/student.txt' into table student;
  ```

- **load HDFS数据到分区表**

  创建一张分区表

  ```mysql
  create table stu_pt 
  (
      id String
     ,name String
  )
  partitioned by (pt_d String)
  row format delimited fields terminated by '\t' 
  STORED AS TEXTFILE;
  ```

  load 数据到表分区里, 注意这种方式相当于一个移动操作， 将 /student.txt 移动到  student 表分区所在路径

  ```shell
  hive> load data inpath '/student.txt' into table stu_pt partition (pt_d = '20210129');
  ```

## 二、Insert

### 1、将查询结果插入表中

#### (1)、语法

```mysql
INSERT (INTO | OVERWRITE) TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)] select_statement;
```

**关键字说明**

- `INTO`：将结果追加到目标表
- `OVERWRITE`：用结果覆盖原有数据

#### (2)、案例

- 将查询结果导出到一张新的表，新的表要先准备好

  ```mysql
  create table student1
  (
      id string
     ,name string
  )row format delimited fields terminated by '\t' ;
  
  insert overwrite table student1
  select * from student;
  ```

- 将查询结果导出到分区表

  准备一张分区表

  ```mysql
  create table stu_pt 
  (
      id String
   ,name String
  )
partitioned by (pt_d String)
  row format delimited fields terminated by '\t' 
  STORED AS TEXTFILE;
  ```
  
  单分区导入
  
  ```mysql
  insert overwrite table stu_pt partition(pt_d='20210129')
  select id, name from student where id='1001';
  ```
  
  多分区导入
  
  ```mysql
  from student
  insert overwrite table stu_pt partition(pt_d='20210127')
  select id, name where id='1002'
  insert overwrite table stu_pt partition(pt_d='20210128')
  select id, name where id='1003';
  ```

### 2、将给定values插入表中

#### (1)、语法

```mysql
INSERT (INTO | OVERWRITE) TABLE tablename [PARTITION (partcol1[=val1], partcol2[=val2] ...)] 
VALUES values_row [, values_row ...]
```

#### (2)、案例

```mysql
insert into student1 values(1004, "南海财神"), (1005, "漠北富婆");
```

### 3、将查询结果写入目标路径

#### (1)、语法

这种语法只能用override

```mysql
INSERT OVERWRITE [LOCAL] DIRECTORY directory
[ROW FORMAT row_format] [STORED AS file_format] select_statement;
```

#### (2)、案例

- 将查询的结果导出到本地

  ```mysql
  insert overwrite local directory '/opt/export/student1' 
  select * from student;
  ```

- 将查询结果按json格式导出到本地

  ```mysql
  insert overwrite local directory '/opt/export/student2'
  row format serde 'org.apache.hadoop.hive.serde2.JsonSerDe'
  select * from student;
  ```

- 将查询的结果格式化导出到本地

  ```mysql
  insert overwrite local directory '/opt/export/student3'
  ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'             
  select * from student;
  ```

- 将查询的结果导出到HDFS上(不用指定local)

  ```mysql
  insert overwrite directory '/student4'
  ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' 
  select * from student;
  ```

## 三、Export & Import

Export导出语句可将表的数据和元数据信息一并到处的HDFS路径，Import可将Export导出的内容导入Hive，表的数据和元数据信息都会恢复。Export和Import可用于两个Hive实例之间的数据迁移

- 语法

  ```mysql
  --导出
  EXPORT TABLE tablename TO 'export_target_path'
  
  --导入
  IMPORT [EXTERNAL] TABLE new_or_original_tablename FROM 'source_path' [LOCATION 'import_target_path']
  ```

- 案例

  ```mysql
  --导出
  export table default.student to '/user/hive/warehouse/export/student';
  
  --导入
  import table student2 from '/user/hive/warehouse/export/student'
  ```

## 四、其他导出方式

### 1、执行hadoop命令导出

方式一：

```mysql
hive (default)> dfs -get /user/hive/warehouse/student/month=201709/000000_0
/opt/module/datas/export/student3.txt;
```

方式二：

```shell
[root@node02 ~]# hdfs dfs -getmerge /user/hivedb/warehouse/city_info /root/city_info.csv
[root@node02 ~]# hdfs dfs -getmerge /user/hivedb/warehouse/city_info /root/city_info.txt
```

当导出的数据文件格式为 csv 时，可能会出现数据乱码

原因有下面两个：

1.乱码的原因是用excel打开csv时格式默认为gbk，但是从hive中导出来的是utf8的

2.格式不对的原因是csv文件的列分隔符是逗号或者\t，而hive中默认使用\001

需要使用 ` iconv -f UTF-8 -c  -t GBK testaa.csv > testbb.csv` 转换编码

### 2、执行beeline命令导出

```sql
[atguigu@hadoop102 hive]$ beeline -e 'select * from default.student;' >
 /opt/module/datas/export/student4.txt;
```

```shell
# 如果查询的数据量特别大，在执行查询前可以先指定内存
export HADOOP_CLIENT_OPTS="-Xmx5120M"
# 指定导出文件的分隔符
beeline --outputformat=dsv --delimiterForDsv="|" -f query.sql > result.csv
```
