# HiveSql初级题目

所有题目均在本地模式下完成：  set mapreduce.framework.name=local;

## 第一章 环境准备

### 1.1 建表语句

```mysql
-- 创建学生表
DROP TABLE IF EXISTS student;
create table if not exists student_info(
    stu_id string COMMENT '学生id',
    stu_name string COMMENT '学生姓名',
    birthday string COMMENT '出生日期',
    sex string COMMENT '性别'
) 
row format delimited fields terminated by ',' 
stored as textfile;

-- 创建课程表
DROP TABLE IF EXISTS course;
create table if not exists course_info(
    course_id string COMMENT '课程id',
    course_name string COMMENT '课程名',
    tea_id string COMMENT '任课老师id'
) 
row format delimited fields terminated by ',' 
stored as textfile;

-- 创建老师表
DROP TABLE IF EXISTS teacher;
create table if not exists teacher_info(
    tea_id string COMMENT '老师id',
    tea_name string COMMENT '学生姓名'
) 
row format delimited fields terminated by ',' 
stored as textfile;

-- 创建分数表
DROP TABLE IF EXISTS score;
create table if not exists score_info(
    stu_id string COMMENT '学生id',
    course_id string COMMENT '课程id',
    score int COMMENT '成绩'
) 
row format delimited fields terminated by ',' 
stored as textfile;
```

### 1.2 数据准备

`student_info.txt`文件内容如下：

```
001,彭于晏,1995-05-16,男
002,胡歌,1994-03-20,男
003,周杰伦,1995-04-30,男
004,刘德华,1998-08-28,男
005,唐国强,1993-09-10,男
006,陈道明,1992-11-12,男
007,陈坤,1999-04-09,男
008,吴京,1994-02-06,男
009,郭德纲,1992-12-05,男
010,于谦,1998-08-23,男
011,潘长江,1995-05-27,男
012,杨紫,1996-12-21,女
013,蒋欣,1997-11-08,女
014,赵丽颖,1990-01-09,女
015,刘亦菲,1993-01-14,女
016,周冬雨,1990-06-18,女
017,范冰冰,1992-07-04,女
018,李冰冰,1993-09-24,女
019,邓紫棋,1994-08-31,女
020,宋丹丹,1991-03-01,女
```

`course_info.txt`文件内容如下：

```
01,语文,1003
02,数学,1001
03,英语,1004
04,体育,1002
05,音乐,1002
```

`teacher_info.txt`文件内容如下：

```
1001,张高数
1002,李体音
1003,王子文
1004,刘丽英
```

`score_info.txt`文件内容如下

```
001,01,94
002,01,74
004,01,85
005,01,64
006,01,71
007,01,48
008,01,56
009,01,75
010,01,84
011,01,61
012,01,44
013,01,47
014,01,81
015,01,90
016,01,71
017,01,58
018,01,38
019,01,46
020,01,89
001,02,63
002,02,84
004,02,93
005,02,44
006,02,90
007,02,55
008,02,34
009,02,78
010,02,68
011,02,49
012,02,74
013,02,35
014,02,39
015,02,48
016,02,89
017,02,34
018,02,58
019,02,39
020,02,59
001,03,79
002,03,87
004,03,89
005,03,99
006,03,59
007,03,70
008,03,39
009,03,60
010,03,47
011,03,70
012,03,62
013,03,93
014,03,32
015,03,84
016,03,71
017,03,55
018,03,49
019,03,93
020,03,81
001,04,54
002,04,100
004,04,59
005,04,85
007,04,63
009,04,79
010,04,34
013,04,69
014,04,40
016,04,94
017,04,34
020,04,50
005,05,85
007,05,63
009,05,79
015,05,59
018,05,87
```

### 1.3 插入数据

```mysql
load data local inpath '/root/student_info.txt' into table student_info;
load data local inpath '/root/course_info.txt' into table course_info;
load data local inpath '/root/teacher_info.txt' into table teacher_info;
load data local inpath '/root/score_info.txt' into table score_info;
```

## 第二章 简单查询

### 2.1 查找特定条件

2.1.1 查询姓名中带“冰”的学生名单

```mysql
hive (default)> select * from student_info where stu_name like '%冰%';
```

2.1.2 查询姓“王”老师的个数

```mysql
hive (default)> select count(1) as c1 from teacher_info where tea_name like '王%';
```

2.1.3 检索课程编号为“04”且分数小于60的学生的课程信息，结果按分数降序排列

```mysql
select
    stu_id,
    course_id,
    score
from score_info
where course_id ='04' and score<60
order by score desc;
```

2.1.4 查询数学成绩不及格的学生和其对应的成绩，按照学号升序排序

```mysql
select
    s.stu_id,
    s.stu_name,
    t1.score
from student_info s
join (
    select
        *
    from score_info
    where course_id=(select course_id from course_info where course_name='数学') and score < 60
    ) t1 on s.stu_id = t1.stu_id
order by s.stu_id;
```

## 第三章 汇总分析

### 3.1 汇总分析

3.1.1 查询编号为“02”的课程的总成绩

```mysql
select 
    t1.course_id
   ,sum(t1.score) as score 
from score_info t1 where t1.course_id="02"
group by t1.course_id;
```

3.1.2 查询参加考试的学生个数

```mysql
select count(1) as c1
from
(
    select stu_id as stu_id 
    from score_info group 
    by stu_id
)t1;

-- 或者用这种写法
select
    count(distinct stu_id) stu_num
from score_info;

-- 补充说明：在大型数据集上使用 DISTINCT 可能涉及到数据的全局排序和去重，可能会导致性能开销较大。
```

### 3.2 分组

3.2.1 查询各科成绩最高和最低的分，以如下的形式显示：课程号，最高分，最低分

```mysql
select 
    course_id
   ,max(score) as max_score
   ,min(score) as min_score
from score_info
group by course_id
```

3.2.2 查询每门课程有多少学生参加了考试（有考试成绩）

```mysql
select
    course_id
   ,count(stu_id)
from score_info
group by course_id
```

3.2.3 查询男生、女生人数

```mysql
select 
    sex
   ,count(1)
from student_info
group by sex;
```

### 3.3 分组结果的条件

3.3.1 查询平均成绩大于60分的学生的学号和平均成绩

```mysql
select
    stu_id
   ,avg(score) as t1
from score_info
group by stu_id
having t1 > 60
```

3.3.2 查询至少选修四门课程的学生学号

```mysql
select 
    stu_id
   ,count(course_id) as c1
from score_info
group by stu_id
having c1 >= 4
```

3.3.3 查询同姓（假设每个学生姓名的第一个字为姓）的学生名单并统计同姓人数大于2的姓

```sql
select
    t1.first_name,
    count(*) count_first_name
from (
         select
             stu_id,
             substr(stu_name,0,1) first_name
         from student_info
) t1
group by t1.first_name
having count_first_name >= 2;
```

3.3.4 查询每门课程的平均成绩，结果按平均成绩升序排序，平均成绩相同时，按课程号降序排列

```mysql
select 
    course_id
   ,avg(score) as c1
from score_info
group by course_id
order by c1, course_id desc
```

3.3.5 统计参加考试人数大于等于15的学科

```mysql
select
    course_id
   ,count(stu_id) c1
from score_info
group by course_id
having c1>=15
```

### 3.4 查询结果排序&分组指定条件

3.4.1 查询学生的总成绩并按照总成绩降序排序

```mysql
select
    stu_id
   ,sum(score) c1
from score_info
group by stu_id
order by c1 desc;
```

3.4.2 按照如下格式显示学生的语文、数学、英语三科成绩，没有成绩的输出为0，按照学生的有效平均成绩降序显示

```mysql

```

3.4.3 查询一共参加三门课程且其中一门为语文课程的学生的id和姓名

## 第四章 复杂查询

4.1 子查询
4.1.1 查询所有课程成绩均小于60分的学生的学号、姓名
4.1.2 查询没有学全所有课的学生的学号、姓名
4.1.3 查询出只选修了三门课程的全部学生的学号和姓名

## 第五章 多表查询

5.1 表联结
5.1.1 查询有两门以上的课程不及格的同学的学号及其平均成绩
5.1.2 查询所有学生的学号、姓名、选课数、总成绩
5.1.3 查询平均成绩大于85的所有学生的学号、姓名和平均成绩
5.1.4 查询学生的选课情况：学号，姓名，课程号，课程名称
5.1.5 查询出每门课程的及格人数和不及格人数
5.1.6 查询课程编号为03且课程成绩在80分以上的学生的学号和姓名及课程信息
5.2 多表连接
5.2.1 课程编号为"01"且课程分数小于60，按分数降序排列的学生信息
5.2.2 查询所有课程成绩在70分以上的学生的姓名、课程名称和分数，按分数升序排列
5.2.3 查询该学生不同课程的成绩相同的学生编号、课程编号、学生成绩
5.2.4 查询课程编号为“01”的课程比“02”的课程成绩高的所有学生的学号
5.2.5 查询学过编号为“01”的课程并且也学过编号为“02”的课程的学生的学号、姓名
5.2.6 查询学过“李体音”老师所教的所有课的同学的学号、姓名
5.2.7 查询学过“李体音”老师所讲授的任意一门课程的学生的学号、姓名
5.2.8 查询没学过"李体音"老师讲授的任一门课程的学生姓名
5.2.9 查询至少有一门课与学号为“001”的学生所学课程相同的学生的学号和姓名
5.2.10 按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩