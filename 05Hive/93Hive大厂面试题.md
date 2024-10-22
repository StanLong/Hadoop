# Hive 大厂面试题

https://blog.csdn.net/SHWAITME/article/details/141179664?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-2-141179664-blog-120475123.235%5Ev43%5Epc_blog_bottom_relevance_base4&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-2-141179664-blog-120475123.235%5Ev43%5Epc_blog_bottom_relevance_base4&utm_relevant_index=5

一、字节跳动
最高峰同时直播人数
https://blog.csdn.net/SHWAITME/article/details/135918264

0 问题描述
   有如下数据记录直播平台主播上播及下播时间，根据该数据计算出平台最高峰同时直播人数。

+----------+----------------------+----------------------+
| user_id  |      start_time      |       end_time       |
+----------+----------------------+----------------------+
| 1        | 2024-04-29 01:00:00  | 2024-04-29 02:01:05  |
| 2        | 2024-04-29 01:05:00  | 2024-04-29 02:03:18  |
| 3        | 2024-04-29 02:00:00  | 2024-04-29 04:03:22  |
| 4        | 2024-04-29 03:15:07  | 2024-04-29 04:33:21  |
| 5        | 2024-04-29 03:34:16  | 2024-04-29 06:10:45  |
| 6        | 2024-04-29 05:22:00  | 2024-04-29 07:01:08  |
| 7        | 2024-04-29 06:11:03  | 2024-04-29 09:26:05  |
| 3        | 2024-04-29 08:00:00  | 2024-04-29 12:34:27  |
| 1        | 2024-04-29 11:00:00  | 2024-04-29 16:03:18  |
| 8        | 2024-04-29 15:00:00  | 2024-04-29 17:01:05  |
+----------+----------------------+----------------------+
1 数据准备
CREATE TABLE IF NOT EXISTS t1_livestream_log (
    user_id INT, -- 主播ID
    start_time STRING, -- 开始时间
    end_time STRING -- 结束时间
);


insert overwrite table  t1_livestream_log values
(1,'2024-04-29 01:00:00','2024-04-29 02:01:05'),
(2,'2024-04-29 01:05:00','2024-04-29 02:03:18'),
(3,'2024-04-29 02:00:00','2024-04-29 04:03:22'),
(4,'2024-04-29 03:15:07','2024-04-29 04:33:21'),
(5,'2024-04-29 03:34:16','2024-04-29 06:10:45'),
(6,'2024-04-29 05:22:00','2024-04-29 07:01:08'),
(7,'2024-04-29 06:11:03','2024-04-29 09:26:05'),
(3,'2024-04-29 08:00:00','2024-04-29 12:34:27'),
(1,'2024-04-29 11:00:00','2024-04-29 16:03:18'),
(8,'2024-04-29 15:00:00','2024-04-29 17:01:05');
2 数据分析
with t1 as(
        select
            user_id,
            start_time as action_time,
            --开播记录，标记 1
            1 as change_cnt
        from t1_livestream_log
        union all
        select
            user_id,
            end_time as action_time,
             --下播记录，标记 1
            -1 as change_cnt
        from t1_livestream_log
    )
select
     max(online_cnt) as max_online_cnt
from
   (select
      user_id,
      action_time,
      change_cnt,
      sum(change_cnt)over(order by action_time asc) as online_cnt
   from t1)t2;


思路分析：

step1: 首先对原始数据进行处理，生成主播上下播的日志数据，同时增加人数变化字段，主播上播为1，主播下播-1;
step2:对操作日志按照操作时间进行累积求和
step3:求取累计求和中的最大值，即为当天最高峰同时直播人数
3 小结
    该题的关键点在于：对每个用户进入/退出直播间的行为进行打标签，再利用sum()over  + max聚合函数计算最终的数值。

==========================*****==========================

股票波峰波谷
https://blog.csdn.net/SHWAITME/article/details/135902998

0 问题描述
    有如下数据，记录每天每只股票的收盘价格，请计算 每只股票的波峰和波谷的日期和价格；

波峰：当天的股票价格大于前一天和后一天
波谷：当天的股票价格小于前一天和后一天
1 数据准备
create table t3_stock_test(
ts_code string comment '股票代码',
trade_date string comment '交易日期',
close float comment '收盘价'
);

insert overwrite table `t3_stock_test` values
('000001.SZ','20220104',16.66),
('000002.SZ','20220104',20.49),
('000001.SZ','20220105',17.15),
('000002.SZ','20220105',21.17),
('000001.SZ','20220106',17.12),
('000002.SZ','20220106',21.05),
('000001.SZ','20220107',17.2),
('000002.SZ','20220107',21.89),
('000001.SZ','20220110',17.19),
('000002.SZ','20220110',22.16),
('000001.SZ','20220111',17.41),
('000002.SZ','20220111',22.3),
('000001.SZ','20220112',17),
('000002.SZ','20220112',22.05),
('000001.SZ','20220113',16.98),
('000002.SZ','20220113',21.53),
('000001.SZ','20220114',16.33),
('000002.SZ','20220114',20.7),
('000001.SZ','20220117',16.22),
('000002.SZ','20220117',20.87);
2 数据分析
      利用lag函数和lead函数，对每支股票分组，开窗计算出每天股票记录的前一天和后一天记录中的价格。

select 
    ts_code,
    trade_date,
    close,
    point_type
from 
    (
    select
        ts_code,
        trade_date,
        close,
        lastday_close,
        nextday_close,
        case when  close > lastday_close and close > nextday_close then '波峰'
            when  close < lastday_close and close < nextday_close then '波谷'
            else '其他' end as `point_type`
    from
        (
        select
            ts_code,
            trade_date,
            close,
            lag(close,1)over(partition by ts_code order by trade_date ) as lastday_close,
            lead(close,1)over(partition by ts_code order by trade_date ) as nextday_close
        from t3_stock_test
        ) t1
    ) t2
where t2.point_type in('波峰','波谷')
3 小结
   lead和lag函数一般用于计算当前行与上一行，或者当前行与下一行之间的差值。

-- 用于统计窗口内往上第n行。参数1为列名，参数2为往上第n行（可选，默认为1），参数3为默认值（当往上第n行为NULL时候，取默认值，如不指定，则为NULL）

lag(col,n,DEFAULT) over(partition by  xx order by xx) 

-- 用于统计窗口内往下第n行。参数1为列名，参数2为往下第n行（可选，默认为1），参数3为默认值（当往下第n行为NULL时候，取默认值，如不指定，则为NULL）

lead(col,n,DEFAULT) over(partition by  xx order by xx) 
==========================*****==========================

合并日期重叠的活动
0 问题描述
已知有表记录了每个大厅的活动开始日期和结束日期，每个大厅可以有多个活动。请编写一个SQL查询合并在同一个大厅举行的所有重叠的活动，如果两个活动至少有一天相同，那他们就是重叠的

样例数据
+----------+-------------+-------------+
| hall_id  | start_date  |  end_date   |
+----------+-------------+-------------+
| 1        | 2023-01-13  | 2023-01-20  |
| 1        | 2023-01-14  | 2023-01-17  |
| 1        | 2023-01-14  | 2023-01-16  |
| 1        | 2023-01-18  | 2023-01-25  |
| 1        | 2023-01-20  | 2023-01-26  |
| 2        | 2022-12-09  | 2022-12-23  |
| 2        | 2022-12-13  | 2022-12-17  |
| 2        | 2022-12-20  | 2022-12-24  |
| 2        | 2022-12-25  | 2022-12-30  |
| 3        | 2022-12-01  | 2023-01-30  |
+----------+-------------+-------------+

结果
+----------+-------------+-------------+
| hall_id  | start_date  |  end_date   |
+----------+-------------+-------------+
| 1        | 2023-01-13  | 2023-01-26  |
| 2        | 2022-12-09  | 2022-12-24  |
| 2        | 2022-12-25  | 2022-12-30  |
| 3        | 2022-12-01  | 2023-01-30  |
+----------+-------------+-------------+
解释：
  大厅 1:
     两个活动["2823-01-13"，"2023-01-20"]和[“2023-01-14"，"2023-01-17"]重叠，需求将它们合并到一个活动中[“2023-01-13"，"2023-01-20"]。

 

1 数据准备
CREATE TABLE IF NOT EXISTS t4_hall_event (
    hall_id STRING, --大厅ID
    start_date STRING, -- 营销活动开始日期
    end_date STRING -- 营销活动结束日期
);
--数据插入
insert overwrite table  t4_hall_event values
('1','2023-01-13','2023-01-20'),
('1','2023-01-14','2023-01-17'),
('1','2023-01-14','2023-01-16'),
('1','2023-01-18','2023-01-25'),
('1','2023-01-20','2023-01-26'),
('2','2022-12-09','2022-12-23'),
('2','2022-12-13','2022-12-17'),
('2','2022-12-20','2022-12-24'),
('2','2022-12-25','2022-12-30'),
('3','2022-12-01','2023-01-30');
2 数据分析
select
       hall_id,
       min(start_date) as start_date,
       max(end_date)   as end_date
from (select
             hall_id,
             start_date,
             end_date,
             max_end_date,
             is_merge,
             sum(is_merge) over (partition by hall_id order by start_date  ) as group_id
      from (select
                   hall_id,
                   start_date,
                   end_date,
                   max_end_date,
                   if(start_date <= max_end_date, 0, 1) as is_merge
            --0:日期重叠，需要合并，1:日期没重叠，不用合并
            from (select
                         hall_id,
                         start_date,
                         end_date,
-- step1: 1.使用max()函数开窗，获得截止到当前行之前的活动最后日期
                         max(end_date)
                             over (partition by hall_id order by start_date   rows between unbounded preceding and 1 preceding) as max_end_date
                  from t4_hall_event) t1
           ) t2
     ) t3
group by hall_id, group_id --注意这里的分组，有group_id
思路分析： 

step1: 使用max()函数开窗，获得截止到当前行之前的活动最后日期
step2:  对当前行的start_date 和截止到上一行的最大end_date进行比较，如果当前行的start_date 小于等于截止到前一行最大end_date 代表有交叉，可以合并，否则代表不可合并。 if(start_date <= max_end_date, 0, 1) as is_merge
step3：连续问题，使用sum()over()进行分组
step4：对hall_id+group_id分组，取每个组内的start_day 的最小值作为活动开始日期，end_day的最大值作为活动结束日期，得到最终结果。

 题意转换： 统计每个大厅开展的营销活动总天数（日期有重叠的地方需要去重）

HiveSQL题——炸裂函数(explode/posexplode)_hive explode-CSDN博客

select
       hall_id,
       sum( datediff(end_date,new_start_date)+ 1) as day_cnt
from (select hall_id,
             start_date,
             end_date,
             max_end_date,
             new_start_date
      from (select hall_id,
                   start_date,
                   end_date,
                   max_end_date,
            if(max_end_date is null, start_date,
                         if(start_date > max_end_date, start_date, date_add(max_end_date, 1))) new_start_date
            from (select hall_id,
                         start_date,
                         end_date,
                         max(end_date)
                             over (partition by hall_id order by
     start_date ,end_date  rows between unbounded preceding and 1 preceding) as max_end_date
                  from t4_hall_event) t1) t2
     where new_start_date <= end_date) t3
group by hall_id;

3 小结
    处理的关键思路：当营销活动的上一个日期区间A 与 当前活动的日期区间B出现重叠（日期交叉，有重复数据）时，需要将区间B的起始时间改成区间A的结束时间。（注意：修改之后需要保证B区间的结束时间> 开始时间）。

==========================*****==========================

查询最近一笔有效订单
0 问题描述
现有订单表t5_order，包含订单ID，订单时间,下单用户，当前订单是否有效

+---------+----------------------+----------+-----------+
| ord_id  |       ord_time       | user_id  | is_valid  |
+---------+----------------------+----------+-----------+
| 1       | 2023-12-11 12:01:03  | a        | 1         |
| 2       | 2023-12-11 12:02:06  | a        | 0         |
| 3       | 2023-12-11 12:03:15  | a        | 0         |
| 4       | 2023-12-11 12:04:20  | a        | 1         |
| 5       | 2023-12-11 12:05:03  | a        | 1         |
| 6       | 2023-12-11 12:01:02  | b        | 1         |
| 7       | 2023-12-11 12:03:03  | b        | 0         |
| 8       | 2023-12-11 12:04:01  | b        | 1         |
| 9       | 2023-12-11 12:07:03  | b        | 1         |
+---------+----------------------+----------+-----------+

请查询出每笔订单的上一笔有效订单,期望查询结果如下：

+---------+----------------------+----------+-----------+--------------------+
| ord_id  |       ord_time       | user_id  | is_valid  | last_valid_ord_id  |
+---------+----------------------+----------+-----------+--------------------+
| 1       | 2023-12-11 12:01:03  | a        | 1         | NULL               |
| 2       | 2023-12-11 12:02:06  | a        | 0         | 1                  |
| 3       | 2023-12-11 12:03:15  | a        | 0         | 1                  |
| 4       | 2023-12-11 12:04:20  | a        | 1         | 1                  |
| 5       | 2023-12-11 12:05:03  | a        | 1         | 4                  |
| 6       | 2023-12-11 12:01:02  | b        | 1         | NULL               |
| 7       | 2023-12-11 12:03:03  | b        | 0         | 6                  |
| 8       | 2023-12-11 12:04:01  | b        | 1         | 6                  |
| 9       | 2023-12-11 12:07:03  | b        | 1         | 8                  |
+---------+----------------------+----------+-----------+--------------------+
1 数据准备
create table t5_order
(
ord_id bigint COMMENT '订单ID',
ord_time string COMMENT '订单时间',
user_id string COMMENT '用户',
is_valid bigint COMMENT '订单是否有效'
);
-- 数据插入
insert overwrite table t5_order values
    (1,'2023-12-11 12:01:03','a',1),
    (2,'2023-12-11 12:02:06','a',0),
    (3,'2023-12-11 12:03:15','a',0),
    (4,'2023-12-11 12:04:20','a',1),
    (5,'2023-12-11 12:05:03','a',1),
    (6,'2023-12-11 12:01:02','b',1),
    (7,'2023-12-11 12:03:03','b',0),
    (8,'2023-12-11 12:04:01','b',1),
    (9,'2023-12-11 12:07:03','b',1);
2 数据分析
with tmp as
   (select
      ord_id,
         ord_time,
         user_id,
         is_valid,
        lag (ord_id,1,null) over(partition by user_id order by ord_time) last_valid_ord_id
    from t5_order
    where is_valid = 1)

select
     ord_id,
     ord_time,
     user_id,
     is_valid,
     last_valid_ord_id
from
   (
   select
        t5.*,
        tmp.last_valid_ord_id,
        row_number()over(partition by t5.ord_id,t5.user_id  order by tmp.ord_time ) rn
   from t5_order t5
   left join tmp
   on   t5.user_id = tmp.user_id
   where tmp.ord_time >= t5.ord_time
   )t6
where rn = 1;

思路分析： 

step1: 查询出有效订单，以及 每笔有效订单的上一单有效订单
step2:  原始的明细数据与step1步的有效订单表按照用户user_id进行关联，筛选条件：有效订单表的订单时间大于等于原始订单表
step3：使用row_number，对原始订单记录表的user_id、ord_id进行分组，对有效订单表的时间排序
step4：筛选rn=1 的记录
3 小结
略

==========================*****==========================

共同使用ip用户检测
0 问题描述
现有用户登录日志表，记录了每个用户登录的IP地址，请查询共同使用过3个及以上IP的用户对；
+----------+-----------------+----------------------+
| user_id  |       ip        |      time_stamp      |
+----------+-----------------+----------------------+
| 2        | 223.104.41.101  | 2023-08-24 07:00:00  |
| 4        | 223.104.41.122  | 2023-08-24 10:00:00  |
| 5        | 223.104.41.126  | 2023-08-24 11:00:00  |
| 4        | 223.104.41.126  | 2023-08-24 13:00:00  |
| 1        | 223.104.41.101  | 2023-08-24 16:00:00  |
| 3        | 223.104.41.101  | 2023-08-24 16:02:00  |
| 2        | 223.104.41.104  | 2023-08-24 16:30:00  |
| 1        | 223.104.41.121  | 2023-08-24 17:00:00  |
| 2        | 223.104.41.122  | 2023-08-24 17:05:00  |
| 3        | 223.104.41.103  | 2023-08-24 18:11:00  |
| 2        | 223.104.41.103  | 2023-08-24 19:00:00  |
| 1        | 223.104.41.104  | 2023-08-24 19:00:00  |
| 3        | 223.104.41.122  | 2023-08-24 19:07:00  |
| 1        | 223.104.41.122  | 2023-08-24 21:00:00  |
+----------+-----------------+----------------------+

1 数据准备
CREATE TABLE t6_login_log (
   user_id bigint COMMENT '用户ID',
   ip string COMMENT '用户登录ip地址',
   time_stamp string COMMENT '登录时间'
) COMMENT '用户登录记录表';

 -- 插入数据
insert overwrite table t6_login_log
values
   (1,'223.104.41.101','2023-08-24 16:00:00'),
   (1,'223.104.41.121','2023-08-24 17:00:00'),
   (1,'223.104.41.104','2023-08-24 19:00:00'),
   (1,'223.104.41.122','2023-08-24 21:00:00'),
   (1,'223.104.41.122','2023-08-24 22:00:00'),
   (2,'223.104.41.101','2023-08-24 07:00:00'),
   (2,'223.104.41.103','2023-08-24 19:00:00'),
   (2,'223.104.41.104','2023-08-24 16:30:00'),
   (2,'223.104.41.122','2023-08-24 17:05:00'),
   (3,'223.104.41.103','2023-08-24 18:11:00'),
   (3,'223.104.41.122','2023-08-24 19:07:00'),
   (3,'223.104.41.101','2023-08-24 16:02:00'),
   (4,'223.104.41.126','2023-08-24 13:00:00'),
   (5,'223.104.41.126','2023-08-24 11:00:00'),
   (4,'223.104.41.122','2023-08-24 10:00:00');
2 数据分析
-- step1: 针对用户登录记录，按照用户ID和登录IP去重
with tmp as
 (
    select
         user_id,
         ip
    from t6_login_log
    group by user_id,ip
    )

select
     t2.user_id,
     t1.user_id
from tmp t1
join tmp t2
-- 自关联，会导致使用相同IP的用户，出现1-2、2-1、1-1、2-2记录（1,2 代表user_id），显然只需要保留 1-2的记录（2-1跟1-2意思一样，保留一个即可；但是1-1，2-2直接过滤掉）
on t1.ip = t2.ip
-- step2:  通过IP地址进行自关联，去重，剔除相同用户
where t2.user_id >  t1.user_id
group by t2.user_id,t1.user_id
having count(t1.ip) >= 3;

思路分析： 

step1: 针对用户登录记录，按照用户ID和登录IP去重
step2:  通过IP地址进行自关联，去重，剔除相同用户
step3：group by + having : 得到共同使用过3个以上IP的用户对
3 小结
略

==========================*****==========================

二、百度
合并用户浏览行为
0 问题描述
有一份用户访问记录表，记录用户id和访问时间，如果用户访问时间间隔小于60s则认为时一次浏览，请合并用户的浏览行为。

+----------+--------------+
| user_id  | access_time  |
+----------+--------------+
| 1        | 1736337600   |
| 1        | 1736337660   |
| 2        | 1736337670   |
| 1        | 1736337710   |
| 3        | 1736337715   |
| 2        | 1736337750   |
| 1        | 1736337760   |
| 3        | 1736337820   |
| 2        | 1736337850   |
| 1        | 1736337910   |
+----------+--------------+

1 数据准备
CREATE TABLE t2_user_access_log (
  user_id bigint COMMENT '用户ID',
  access_time bigint COMMENT '访问时间'
) COMMENT '用户访问记录表';

--插入数据
insert overwrite table t2_user_access_log
values
    (1,1736337600),
    (1,1736337660),
    (2,1736337670),
    (1,1736337710),
    (3,1736337715),
    (2,1736337750),
    (1,1736337760),
    (3,1736337820),
    (2,1736337850),
    (1,1736337910);
2 数据分析
select
        user_id,
        access_time,
        last_access_time,
        sum(is_new_group)over(partition by user_id order by access_time) as group_id
 from
(
   select
        user_id,
        access_time,
        last_access_time,
        -- 1736337710 (时间戳 10位，单位是秒)
        if(access_time - last_access_time >= 60,1,0)  as is_new_group
    from
   (
     select
         user_id,
          access_time,
          lag(access_time,1,null) over(partition by user_id order by access_time) as last_access_time
     from t2_user_access_log
   )t1
)t2

思路分析：

step1: 借助lag开窗函数，对每个用户分区，对访问时间排序，计算出时间差（用户访问时间间隔）
step2:   对访问时间间隔进行判断，如果大于等于60秒，则是新的访问
step3：sum() over (partition by order by )累加计算,得到合并结果
3 小结
  对上下两行数据进行比较，一般采用lag 、 lead这种开窗函数。

==========================*****==========================

连续签到领金币
0 问题描述
有用户签到记录表，t4_coin_signin,记录用户当天是否完成签到,请计算出每个用户的每个月获得的金币数量；
签到领金币规则如下：

（1）用户签到获得1金币；
（2）如果用户连续签到3天则第三天获得2金币，如果用户连续签到7天则第7天获得5金币；
（3）连续签到7天后连续天数重置，每月签到天数重置；


样例数据

+----------+--------------+----------+
| user_id  | signin_date  | is_sign  |
+----------+--------------+----------+
| 001      | 2024-01-01   | 1        |
| 001      | 2024-01-02   | 1        |
| 001      | 2024-01-03   | 1        |
| 001      | 2024-01-04   | 0        |
| 001      | 2024-01-05   | 1        |
| 001      | 2024-01-06   | 1        |
| 001      | 2024-01-07   | 1        |
| 001      | 2024-01-08   | 1        |
| 001      | 2024-01-09   | 1        |
| 001      | 2024-01-10   | 1        |
| 001      | 2024-01-11   | 1        |
| 001      | 2024-01-12   | 1        |
| 001      | 2024-01-13   | 1        |
| 001      | 2024-01-14   | 1        |
| 001      | 2024-01-15   | 1        |
| 001      | 2024-01-16   | 1        |
| 001      | 2024-01-17   | 1        |
| 001      | 2024-01-18   | 1        |
| 001      | 2024-01-19   | 1        |
| 001      | 2024-01-20   | 0        |
| 001      | 2024-01-21   | 1        |
| 001      | 2024-01-22   | 1        |
| 001      | 2024-01-23   | 1        |
| 001      | 2024-01-24   | 0        |
| 001      | 2024-01-25   | 1        |
| 001      | 2024-01-26   | 1        |
| 001      | 2024-01-27   | 1        |
| 001      | 2024-01-28   | 1        |
| 001      | 2024-01-29   | 0        |
| 001      | 2024-01-30   | 1        |
| 001      | 2024-01-31   | 1        |
| 001      | 2024-02-01   | 1        |
| 001      | 2024-02-02   | 1        |
| 001      | 2024-02-03   | 1        |
| 001      | 2024-02-04   | 1        |
| 001      | 2024-02-05   | 1        |
| 001      | 2024-02-06   | 1        |
| 001      | 2024-02-07   | 1        |
| 001      | 2024-02-08   | 1        |
| 001      | 2024-02-09   | 1        |
| 001      | 2024-02-10   | 1        |
+----------+--------------+----------+
1 数据准备
CREATE TABLE t4_coin_signin
(
    user_id     string COMMENT '用户ID',
    signin_date string COMMENT '日期',
    is_sign     bigint COMMENT '是否签到 1-签到，0-未签到'
) COMMENT '签到领金币记录表';

     -- 插入数据
insert overwrite tablet4_coin_signin
values ('001', '2024-01-01', 1),
       ('001', '2024-01-02', 1),
       ('001', '2024-01-03', 1),
       ('001', '2024-01-04', 0),
       ('001', '2024-01-05', 1),
       ('001', '2024-01-06', 1),
       ('001', '2024-01-07', 1),
       ('001', '2024-01-08', 1),
       ('001', '2024-01-09', 1),
       ('001', '2024-01-10', 1),
       ('001', '2024-01-11', 1),
       ('001', '2024-01-12', 1),
       ('001', '2024-01-13', 1),
       ('001', '2024-01-14', 1),
       ('001', '2024-01-15', 1),
       ('001', '2024-01-16', 1),
       ('001', '2024-01-17', 1),
       ('001', '2024-01-18', 1),
       ('001', '2024-01-19', 1),
       ('001', '2024-01-20', 0),
       ('001', '2024-01-21', 1),
       ('001', '2024-01-22', 1),
       ('001', '2024-01-23', 1),
       ('001', '2024-01-24', 0),
       ('001', '2024-01-25', 1),
       ('001', '2024-01-26', 1),
       ('001', '2024-01-27', 1),
       ('001', '2024-01-28', 1),
       ('001', '2024-01-29', 0),
       ('001', '2024-01-30', 1),
       ('001', '2024-01-31', 1),
       ('001', '2024-02-01', 1),
       ('001', '2024-02-02', 1),
       ('001', '2024-02-03', 1),
       ('001', '2024-02-04', 1),
       ('001', '2024-02-05', 1),
       ('001', '2024-02-06', 1),
       ('001', '2024-02-07', 1),
       ('001', '2024-02-08', 1),
       ('001', '2024-02-09', 1),
       ('001', '2024-02-10', 1);
2 数据分析
select
     user_id,
     signin_month,
     sum(coin_num) as month_coin_num
from
(
   select
           user_id,
           signin_month,
           signin_group,
           conn_sign_days,
           new2_conn_sign_days,
           -- case when,根据new2_conn_sign_days中签到第几天，得出每天应该得到多少金币。 计算每天得到的金币数量
           case when new2_conn_sign_days = 3 then 2
                when conn_sign_days = 7 then 5
                   else 1 end as coin_num
    from
   (
      select
           user_id,
           signin_month,
           signin_group,
           conn_sign_days,
           -- mod取模函数，处理7天重置问题,得到参与活动的实际连续第几天签到；
           mod(conn_sign_days,7) as new1_conn_sign_days,

 -- 使用mod函数，对conn_sign_days进行处理，每7天重置一次，其中0代表第7天，需要特殊处理一下。
           if(mod(conn_sign_days,7)  = 0,7,mod(conn_sign_days,7)) as new2_conn_sign_days
      from
      (
         select
                user_id,
                signin_date,
                signin_month,
                signin_group,
                -- 根据按照用户、月份、signin_group 进行分组，按照日期排序，使用count(signin_date) 计算出:每个用户每月的，截止到当前的连续签到天数。
                count(signin_date)over(partition by user_id,signin_month,signin_group  order by signin_date) as conn_sign_days
         from
          (
              select
                   user_id,
                   signin_date,
                   is_sign,
                   substr(signin_date,1,7)  as signin_month,
                   --  根据用户、月份进行分组,按照日期排序，得到一个用户连续签到的分组 signin_group
                   sum(if(is_sign = 1,0,1)) over(partition by user_id,substr(signin_date,1,7) order by signin_date)  as signin_group
              from t4_coin_signin
          )t1
         -- 筛选出：用户签到状态的数据
         where is_sign = 1
       )t2
    )t3
)t4
 group by user_id,
          signin_month;


思路分析：

step1: 判断用户是否连续签到：根据用户、月份进行分组,按照日期排序，得到一个用户连续签到的分组 
step2: 根据按照用户、月份、signin_group 进行分组，按照日期排序，使用count(signin_date) 计算出:每个用户每月的，截止到当前的连续签到天数。（用户当月实际是第几天连续签到，需要注意where的筛选条件：用户是签到状态）
step3：连续签到7天后连续天数重置。使用mod函数，对conn_sign_days进行处理，每7天重置一次，其中0代表第7天，需要特殊处理一下。
step4：case when,根据new2_conn_sign_days中签到第几天，得出每天应该得到多少金币。 计算出每个用户每天得到的金币数量。
step5：分组聚合得到每个用户每月得到的金币数
3 小结
 略

==========================*****==========================

三、阿里
用户行为轨迹
0 问题描述
现有一份用户在各个地铁站进出的时间表和一份用户商场扫码行为表，其中商场扫码会存在多次，可以取最新的数据。请查询并返回出用户整个行为轨迹列表.

样例数据
t_subway_station

+----------+-------------+----------------------+----------------------+
| user_id  | station_id  |       in_time        |       out_time       |
+----------+-------------+----------------------+----------------------+
| 001      | 1           | 2024-07-01 09:01:01  | NULL                 |
| 001      | 2           | NULL                 | 2024-07-01 09:21:08  |
| 001      | 3           | 2024-07-01 11:01:41  | NULL                 |
| 001      | 4           | NULL                 | 2024-07-01 11:23:12  |
| 001      | 4           | 2024-07-01 15:33:29  | NULL                 |
| 001      | 1           | NULL                 | 2024-07-01 15:45:41  |
+----------+-------------+----------------------+----------------------+


t_market
+----------+------------+----------------------+
| user_id  | market_id  |      check_time      |
+----------+------------+----------------------+
| 001      | 1001       | 2024-07-01 10:23:04  |
| 001      | 1001       | 2024-07-01 10:25:38  |
| 001      | 1002       | 2024-07-01 10:45:01  |
| 001      | 1004       | 2024-07-01 13:56:27  |
| 001      | 1003       | 2024-07-01 14:37:24  |
+----------+------------+----------------------+

期望结果
+----------+----------------------------------+
| user_id  |            place_list            |
+----------+----------------------------------+
| 001      | 1,2,1001,1002,3,4,1004,1003,4,1  |
+----------+----------------------------------+


1 数据准备
--建表语句
CREATE TABLE t1_subway_station (
  user_id string,
  station_id string,
  in_time string,
  out_time string
);
--插入数据
insert overwrite  table  t1_subway_station
values
   ('001','1','2024-07-01 09:01:01',null),
   ('001','2',null,'2024-07-01 09:21:08'),
   ('001','3','2024-07-01 11:01:41',null),
   ('001','4',null,'2024-07-01 11:23:12'),
   ('001','4','2024-07-01 15:33:29',null),
   ('001','1',null,'2024-07-01 15:45:41');

--建表语句
CREATE TABLE t1_market (
  user_id string,
  market_id string,
  check_time string
);
--插入数据
insert overwrite  table t1_market
values
('001','1001','2024-07-01 10:23:04'),
('001','1001','2024-07-01 10:25:38'),
('001','1002','2024-07-01 10:45:01'),
('001','1004','2024-07-01 13:56:27'),
('001','1003','2024-07-01 14:37:24');
2 数据分析
select
      user_id,
     -- 利用concat_ws函数拼接字符串,使用正则表达式regexp_replace将结果时间进行剔除，得到最终结果。
     --  \d表示匹配数字字符,因此，\d{4}可以匹配任意四个数字字符。
      regexp_replace(concat_ws(',',sort1_str) ,'\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}','') as place_list
from
(
    select
           user_id,
     -- 使用collect_list对con_str进行拼接，并使用sort_array进行排序，得到顺序的时间点位信息。
           sort_array(collect_list(concat(check_time,place))) as  sort1_str
    from
    (
        select
               user_id,
               station_id                  as place,
               coalesce(in_time, out_time) as check_time
        from t1_subway_station
        group by  user_id,station_id,coalesce(in_time, out_time)
        union all
        --商场时间
        select
               user_id,
               place,
               check_time
        from (
                 select user_id,
                        market_id       as place,
        -- 根据题目要求，取出每个商场最新的时间即可。
                        max(check_time) as check_time
                 from t1_market
                 group by user_id, market_id) t
    )t1
    group by user_id
)t2



思路分析：

step1: 地铁站进出数据和商场数据进行union all操作。
step2：需要根据时间保证结果有序，所以需要先对check_time 与place进行拼接。使用concat进行拼接
step3：使用collect_list对con_str进行拼接，并使用sort_array进行排序，得到顺序的时间点位信息。
step4：使用正则表达式将结果时间进行剔除，得到最终结果。
3 小结
HiveSQL题——collect_set()/collect_list()聚合函数_collect set-CSDN博客

上述用到的Hive SQL函数：

(1) collect_set()、collect_list()

     collect_set()函数与collect_list()函数属于高级聚合函数（行转列），将分组中的某列转换成一个数组返回，常与concat_ws()函数连用实现字段拼接效果。

collect_list：收集并形成list集合，结果不去重
collect_set：收集并形成set集合，结果去重
(2) sort_array : 数组排序函数

 语法：sort_array(array, [asc|desc]) ： 按照指定的排序规则对数组进行排序，并返回一个排好序的新数组。  第一个参数：array为需要排序的数组；第二个参数：asc为可选参数，如果设置为true则按升序排序；desc为可选参数，如果设置为true，则按降序排序。如果既不设置asc也不设置desc，则按升序排序
 举例：select sort_array(array(2, 5, 3, 1)) as sorted_array;  ====》  [1,2,3,5]
(3) concat_ws(带分隔符的字符串连接函数)

语法：concat_ws(string SEP, string A ,string B.......)
返回值：string
说明：返回输入字符串连接后的结果,SEP表示各个字符串的分隔符
举例：select  concat_ws('|','ad','cv','op') ;---> ad|cv|op 
(4) lpad / rpad：左/右补足函数

语法: lpad(string str, int len, string pad) 、rpad(string str, int len, string pad)
举例：select lpad('abc',7,'td'); ---> tdtdabc  ；  select rpad('abc',7,'td'); ---> abctdtd
==========================*****==========================

四、meta
计算每个用户的受欢迎程度
0 问题描述
有好友关系表t1_friend，记录了user1_id,user2_id的好友关系对。现定义用户受欢迎程度=用户拥有的朋友总数/平台上的用户总数,请计算出每个用户的受欢迎程度。

+-----------+-----------+
| user1_id  | user2_id  |
+-----------+-----------+
| 1         | 2         |
| 1         | 3         |
| 1         | 4         |
| 1         | 5         |
| 2         | 3         |
| 2         | 4         |
| 3         | 4         |
| 4         | 5         |
| 5         | 6         |
| 5         | 7         |
| 7         | 8         |
| 9         | 10        |
+-----------+-----------+
1 数据准备
--建表语句
CREATE TABLE t1_friend(
  user1_id bigint COMMENT '用户1ID',
  user2_id bigint COMMENT '用户2ID'
) COMMENT '好友关系表';

-- 插入数据
insert overwrite  table t1_friend
values
  (1,2),
  (1,3),
  (1,4),
  (1,5),
  (2,3),
  (2,4),
  (3,4),
  (4,5),
  (5,6),
  (5,7),
  (7,8),
  (9,10)
2 数据分析
with tmp as
         (select
              user1_id as  user1_id,
              user2_id as  user2_id
         from t1_friend
         union all
         select
              user2_id as  user1_id,
              user1_id as  user2_id
         from t1_friend)

select
     user1_id,
     friend_cnt / total_cnt as res
from (
     select 
            user1_id,
            count(user2_id) as friend_cnt,
            count( user1_id) over () as total_cnt
    from tmp
    group by user1_id
     )t1;


思路分析：

step1: user1_id,user2_id互换，然后进行union all。题中数据user1_id,user2_id为互为好友关系，形成 【关系对】，即1与2是好友关系，则1-2,2-1记录只会存在一条，为方便计算，需要有两条记录。所以将user2_id与user1_id 互换，然后与原表进行union all；
step2：按照user1_id分组，统计user2_id的个数，即user1_id 的好友数据；再使用开窗计算出所有的用户数；
step3：某个用户的好友数除以总用户数，计算最终结果；
3 小结
  略

==========================*****==========================

五、腾讯
向用户推荐好友喜欢的音乐
0 问题描述
现有三张表分别为：

用户关注表t1_follow(user_id,follower_id)记录用户ID及其关注的人ID
+----------+--------------+
| user_id  | follower_id  |
+----------+--------------+
| 1        | 2            |
| 1        | 4            |
| 1        | 5            |
+----------+--------------+

用户喜欢的音乐t1_music_likes(user_id,music_id)
+----------+-----------+
| user_id  | music_id  |
+----------+-----------+
| 1        | 10        |
| 2        | 20        |
| 2        | 30        |
| 3        | 20        |
| 3        | 30        |
| 4        | 40        |
| 4        | 50        |
+----------+-----------+

音乐名字表t1_music(music_id,music_name)
+-----------+-------------+
| music_id  | music_name  |
+-----------+-------------+
| 10        | a           |
| 20        | b           |
| 30        | c           |
| 40        | d           |
| 50        | e           |
+-----------+-------------+

需求：给用户1推荐他关注的用户喜欢的音乐名称

1 数据准备
--建表语句
CREATE TABLE t1_follow (
  user_id bigint COMMENT '用户ID',
  follower_id bigint COMMENT '其关注的用户ID'
) COMMENT '用户关注表';
     
-- 插入数据
insert overwrite table  t1_follow
values
   (1,2),
   (1,4),
   (1,5);

-- 建表语句
CREATE TABLE t1_music_likes (
  user_id bigint COMMENT '用户ID',
  music_id bigint COMMENT '音乐ID'
) COMMENT '用户喜欢音乐ID';

--插入语句
insert overwrite table t1_music_likes
values
  (1,10),
  (2,20),
  (2,30),
  (3,20),
  (3,30),
  (4,40),
  (4,50);
     
     
--建表语句
CREATE TABLE t1_music (
   music_id bigint COMMENT '音乐ID',
   music_name string COMMENT '音乐名称'
) COMMENT '音乐名字表';
     
-- 插入语句
insert overwrite table t1_music
values
   (10,'a'),
   (20,'b'),
   (30,'c'),
   (40,'d'),
   (50,'e');
2 数据分析
select
     t4.user_id,
     concat_ws(',', collect_set(t4.music_name)) as push_music
from
    (
        select
             t1.user_id,
             t1.follower_id,
             t2.music_id,
             t3.music_name
        from t1_follow t1
        left join t1_music_likes t2
         on t1.follower_id  = t2.user_id
         left join t1_music t3
        on  t2.music_id = t3.music_id
         where t1.user_id = 1
    )t4
group by t1.user_id;


思路分析：

step1: 根据用户关注表和用户喜欢的音乐表进行关联，查询出每个用户喜欢的音乐ID；再关联音乐名字表，关联出对应的音乐名称；
step2：行转列并对重复的音乐名称去重，得到最终结果；行转列使用聚合函数collect_set()函数，然后使用concat_ws转成字符串
3 小结
略

==========================*****==========================

连续登陆超过N天的用户
0 问题描述
现有用户登录日志表 t5_login_log,包含用户ID(user_id),登录日期(login_date)。
数据已经按照用户日期去重，请计算 连续登录超过4天的用户ID。

样例数据
+----------+-------------+
| user_id  | login_date  |
+----------+-------------+
| 0001     | 20220101    |
| 0001     | 20220102    |
| 0001     | 20220103    |
| 0001     | 20220104    |
| 0001     | 20220105    |
| 0001     | 20220107    |
| 0001     | 20220108    |
| 0001     | 20220109    |
| 0002     | 20220101    |
| 0002     | 20220102    |
| 0002     | 20220103    |
| 0002     | 20220107    |
| 0002     | 20220108    |
| 0003     | 20220107    |
| 0003     | 20220108    |
| 0003     | 20220109    |
+----------+-------------+
1 数据准备
-- 建表语句
create table t5_login_log
(
   user_id string comment '用户ID',
   login_date string comment '登录日期'
);

--数据插入语句
insert overwrite table  t5_login_log values
   ('0001','20220101'),
   ('0001','20220102'),
   ('0001','20220103'),
   ('0001','20220104'),
   ('0001','20220105'),
   ('0001','20220107'),
   ('0001','20220108'),
   ('0001','20220109'),
   ('0002','20220101'),
   ('0002','20220102'),
   ('0002','20220103'),
   ('0002','20220107'),
   ('0002','20220108'),
   ('0003','20220107'),
   ('0003','20220108'),
   ('0003','20220109');
2 数据分析

select
     user_id
from
  (
      select
           user_id,
           new_login_date,
           rn,
           date_sub(new_login_date,rn) as diff
      from
          (
            select
                user_id,
                login_date,
                unix_timestamp(login_date, 'yyyyMMdd') as login_ts,
                from_unixtime( unix_timestamp(login_date, 'yyyyMMdd'),'yyyy-MM-dd') as new_login_date,
                row_number() over(partition by user_id order by login_date ) as rn
            from t5_login_log
          )t1
  )t2
group by  user_id,diff
having count(1) >=4;


思路分析：

step1: 处理日期格式；
step2：row_number()开窗，计算每个用户每个登录日期的排名；
step3：计算用户登录日期与排名的差值，差值一样的，代表连续登陆；
step4：按照用户和差值分组，得出连续登录大于等于4天的用户；
3 小结
  上述代码用到的日期函数

(1) from_unixtime(时间戳转日期函数)

语法：from_unixtime(bigint unixtime , string format)
返回值：输入bigint的时间戳， 输出string格式化的时间
说明：转化 unix时间戳（从 1970-01-01 00:00:00 UTC 到指定时间的秒数）到当前时区的时间格式，默认的format是yyyy-MM-dd HH:mm:ss，可以指定别的。
举例：select from_unixtime(1323308943,'yyyyMMdd')  --> 20111208 
(2) unix_timestamp(日期转 时间戳函数)

语法：unix_timestamp(string date)  、unix_timestamp(string date,string pattern)
返回值：bigint
说明：将格式为"yyyy-MM-dd HH:mm:ss"的日期 转换成 unix的时间戳。如果转换失败，则返回值为0；
注意：输入时间必须是到秒级的时间，否则转换失败返回NULL
==========================*****==========================

微信运动步数在好友中的排名
0 问题描述
有两个表，朋友关系表t6_user_friend，用户步数表t6_user_steps。
朋友关系表包含两个字段，用户id，用户好友的id；
用户步数表包含两个字段，用户id，用户的步数

求出：用户在好友中的排名。

朋友关系表t6_user_friend
+----------+------------+
| user_id  | friend_id  |
+----------+------------+
| 1        | 2          |
| 1        | 3          |
| 2        | 1          |
| 2        | 3          |
| 2        | 4          |
| 2        | 5          |
| 3        | 1          |
| 3        | 4          |
| 3        | 5          |
| 4        | 2          |
| 4        | 3          |
| 4        | 5          |
| 5        | 2          |
| 5        | 3          |
| 5        | 4          |
+----------+------------+

用户步数表t6_user_steps
+---------------------+-------------------+
| t6_user_steps.user_id  | t6_user_steps.steps  |
+---------------------+-------------------+
| 1                   | 100               |
| 2                   | 95                |
| 3                   | 90                |
| 4                   | 80                |
| 5                   | 10                |
+---------------------+-------------------+
1 数据准备

create table t6_user_friend
(
    user_id   INT,
    friend_id INT
);

-- 插入数据
insert overwrite table  t6_user_friend
values (1, 2),
       (1, 3),
       (2, 1),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 1),
       (3, 4),
       (3, 5),
       (4, 2),
       (4, 3),
       (4, 5),
       (5, 2),
       (5, 3),
       (5, 4);

create table t6_user_steps
(
    user_id INT,
    steps   INT
);

insert overwrite table  t6_user_steps
values (1, 100),
       (2, 95),
       (3, 90),
       (4, 80),
       (5, 10);
2 数据分析
select
     user_id,
     rn
from
    (
      select
            user_id,
            friend_id,
            steps,
            row_number() over (partition by user_id order by steps desc) as rn
     from
        (
            --好友步数
            select
                 t1.user_id,
                 t1.friend_id,
                 t2.steps
            from t6_user_friend t1
            join t6_user_steps t2
            on t1.friend_id = t2.user_id
            union all
            -- 自己步数
            select
                 user_id,
                 user_id as friend_id,
                 steps
           from t6_user_steps
       )tmp1
   )tmp2
where user_id = friend_id

思路分析：

step1: 需要求出 自己在好友中的排名，由于好友关系表中只有“好友”，所以需要加入自己的数据；
step2：按照用户分组，给每个用户的“好友”进行排名；
step3：筛选出自己名次的那一行数据；
3 小结
略

==========================*****==========================

六、高频大数据面试SQL
每年成绩都有所提升的学生
0 问题描述
一张学生成绩表(student_scores)，有year-学年，subject-课程，student-学生，score-分数这四个字段，请完成如下问题：

问题1：每年每门学科排名第一的学生

问题2：每年总成绩都有所提升的学生

数据内容如下
+-------+----------+----------+--------+
| year  | subject  | student  | score  |
+-------+----------+----------+--------+
| 2018  | 语文       | A        | 84     |
| 2018  | 数学       | A        | 59     |
| 2018  | 英语       | A        | 30     |
| 2018  | 语文       | B        | 44     |
| 2018  | 数学       | B        | 76     |
| 2018  | 英语       | B        | 68     |
| 2019  | 语文       | A        | 51     |
| 2019  | 数学       | A        | 94     |
| 2019  | 英语       | A        | 71     |
| 2019  | 语文       | B        | 87     |
| 2019  | 数学       | B        | 44     |
| 2019  | 英语       | B        | 38     |
| 2020  | 语文       | A        | 91     |
| 2020  | 数学       | A        | 50     |
| 2020  | 英语       | A        | 89     |
| 2020  | 语文       | B        | 81     |
| 2020  | 数学       | B        | 84     |
| 2020  | 英语       | B        | 98     |
+-------+----------+----------+--------+
1 数据准备
CREATE TABLE t1_student_scores
(
    year STRING,
    subject STRING,
    student STRING,
    score INT );
-- 数据插入语句
insert overwrite  table t1_student_scores
      VALUES
    (2018, '语文', 'A', 84),
    (2018, '数学', 'A', 59),
    (2018, '英语', 'A', 30),
    (2018, '语文', 'B', 44),
    (2018, '数学', 'B', 76),
    (2018, '英语', 'B', 68),
    (2019, '语文', 'A', 51),
    (2019, '数学', 'A', 94),
    (2019, '英语', 'A', 71),
    (2019, '语文', 'B', 87),
    (2019, '数学', 'B', 44),
    (2019, '英语', 'B', 38),
    (2020, '语文', 'A', 91),
    (2020, '数学', 'A', 50),
    (2020, '英语', 'A', 89),
    (2020, '语文', 'B', 81),
    (2020, '数学', 'B', 84),
    (2020, '英语', 'B', 98);
2 数据分析
   问题1：每年每门学科排名第一的学生

 --计算排名第一的人
select year,
       subject,
       first_student
from (select year,
             subject,
             first_value(student) over (partition by year,subject order by score desc) as first_student
      from t1_student_scores) t
group by year, subject, first_student;


思路分析：

step1: 按照年份、学科分组，按照分数排序，计算出相同年份，相同学科排名第一的人
step2：去重，计算出最终结果。
   问题2：每年总成绩都有所提升的学生

select
      student
from
(
  select
         year,
         student,
         total_score,
         last_year_score,
         if(total_score >last_year_score,1,0 ) as improve_flag

  from
  (
     select
          year,
          student,
          total_score,
          lag(total_score,1,0) over(partition by student order by year ) last_year_score
     from
     (
         select
              year,
              student,
              sum(score) as total_score
         from t1_student_scores
         group by year,student
     )t1
   )t2
   where last_year_score is not null
)t3
  group by student
   having count(1) = sum(improve_flag);


思路分析：

step1: 计算每年每个学生的总成绩；
step2：结合lag开窗函数函数，在本行添加上一学年成绩；
step3：where last_year_score is not null，去掉lag()结果字段为空数据，并判断是否有进步
step4：根据学生分组，having count(1) = sum(improve_flag) 来获取每年进步的学生；
3 小结
略

==========================*****==========================

连续点击三次用户（**）
0 问题描述
有用户点击日志记录表 t2_click_log,  user_id(用户ID), click_time(点击时间)， 请查询出连续点击三次的用户数，连续点击三次：指点击记录中同一用户连续点击，中间无其他用户点击；

+----------+--------------+
| user_id  | click_time |
+----------+--------------+
| 1        | 1736337600   |
| 2        | 1736337670   |
| 1        | 1736337710   |
| 1        | 1736337715   |
| 1        | 1736337750   |
| 2        | 1736337760   |
| 3        | 1736337820   |
| 3        | 1736337840   |
| 3        | 1736337850   |
| 3        | 1736337910   |
| 4        | 1736337915   |
+----------+--------------+
1 数据准备
--建表语句
CREATE TABLE t2_click_log (
  user_id BIGINT,
  click_time BIGINT
);

--插入数据
insert overwrite table t2_click_log 
(1,1736337600),
(2,1736337670),
(1,1736337710),
(1,1736337715),
(1,1736337750),
(2,1736337760),
(3,1736337820),
(3,1736337840),
(3,1736337850),
(3,1736337910),
(4,1736337915)

2 数据分析
方式一：双重排序差值法（连续N天登陆）

select
     count(distinct user_id) as num
 from
  (
    select
         user_id,
         diff,
         count(1) as cnt
    from
    (
         select
             user_id,
             rn1 - rn2 as diff
         from (select user_id,
                     click_time,
                     row_number() over (order by click_time asc) as rn1,
                     row_number() over (partition by user_id order by click_time asc) as rn2
              from t2_click_log) t1
    )t2
         group by  user_id,diff
         having cnt >= 3
  )t3;


思路分析：

step1: 与连续登录天数类似。先按照点击时间(click_time) 进行全排序，再按照用户ID(user_id)分组，按照点击时间排序；
step2：对两次排序计算差值diff ，按照用户user_id 和差值diff 进行分组。对于相同用户，差值一样代表 连续；
step3：同一分组的数量>=3的user_id 则是连续点击三次用户
3 小结
略

==========================*****==========================

去掉最大最小值的部门平均薪水
0 问题描述
有员工薪资表t3_salary,包含员工ID(emp_id)，部门ID(depart_id)，薪水(salary),请计算去除最高最低薪资后的平均薪水；（每个部门员工数不少于3人）
+---------+------------+-----------+
| emp_id  | depart_id  |  salary   |
+---------+------------+-----------+
| 1001    | 1          | 5000.00   |
| 1002    | 1          | 10000.00  |
| 1003    | 1          | 20000.00  |
| 1004    | 1          | 30000.00  |
| 1005    | 1          | 6000.00   |
| 1006    | 1          | 10000.00  |
| 1007    | 1          | 11000.00  |
| 1008    | 2          | 3000.00   |
| 1009    | 2          | 7000.00   |
| 1010    | 2          | 9000.00   |
| 1011    | 2          | 30000.00  |
+---------+------------+-----------+
1 数据准备
CREATE TABLE t3_salary (
  emp_id bigint,
  depart_id bigint,
  salary decimal(16,2)
);
--插入数据
insert overwrite table t3_salary
values
    (1001,1,5000.00),
    (1002,1,10000.00),
    (1003,1,20000.00),
    (1004,1,30000.00),
    (1005,1,6000.00),
    (1006,1,10000.00),
    (1007,1,11000.00),
    (1008,2,3000.00),
    (1009,2,7000.00),
    (1010,2,9000.00),
    (1011,2,30000.00);
2 数据分析
select
     depart_id ,
     avg(salary)  as avg_salary
from
  (
    select
         emp_id    ,
         depart_id ,
         salary    ,
         row_number()over(partition by depart_id order by salary ) as rn1,
         row_number()over(partition by depart_id order by salary desc) as rn2
    from t3_salary
  )t1
 where rn1 > 1 and  rn2 > 1
group by depart_id


思路分析：

step1: 进行两次开窗，排序来去掉 部门内的最高最低薪资
step2：按照部门分组进行分组取平均即可；
3 小结
   需注意：本题的前置条件：每个部门员工数不少于3人，且部门的最高和最低薪资仅一人，所以直接利用row_number开窗函数就能处理。

==========================*****==========================

当前活跃用户连续活跃天数
0 问题描述
有用户登录日志表，包含日期、用户ID，当天是否登录，请查询出当天活跃的用户当前连续活跃天数；

+-------------+----------+-----------+
| login_date  | user_id  | is_login  |
+-------------+----------+-----------+
| 2023-08-01  | 1        | 1         |
| 2023-08-01  | 2        | 1         |
| 2023-08-01  | 3        | 1         |
| 2023-08-01  | 4        | 0         |
| 2023-08-02  | 1        | 1         |
| 2023-08-02  | 2        | 0         |
| 2023-08-02  | 3        | 1         |
| 2023-08-02  | 4        | 1         |
| 2023-08-03  | 1        | 1         |
| 2023-08-03  | 2        | 1         |
| 2023-08-03  | 3        | 0         |
| 2023-08-03  | 4        | 1         |
+-------------+----------+-----------+
1 数据准备
--建表语句
CREATE TABLE t4_login_log (
  login_date string COMMENT '日期',
  user_id bigint COMMENT '用户ID',
  is_login bigint COMMENT '是否登录'
) COMMENT '用户签到记录表';

--插入数据
insert overwrite table  t4_login_log
values
  ('2023-08-01',1,1),
  ('2023-08-01',2,1),
  ('2023-08-01',3,1),
  ('2023-08-01',4,0),
  ('2023-08-02',1,1),
  ('2023-08-02',2,0),
  ('2023-08-02',3,1),
  ('2023-08-02',4,1),
  ('2023-08-03',1,1),
  ('2023-08-03',2,1),
  ('2023-08-03',3,0),
  ('2023-08-03',4,1);
2 数据分析
select
       t1.user_id,
       count(1) as login_days
from t4_login_log t1
 left join
  (
    select
         user_id,
         max(login_date)as latest_unlogin_date
    from t4_login_log
    where is_login = 0
    group by user_id
  )t2
 on t1.user_id = t2.user_id
  where t1.login_date > coalesce(t2.latest_unlogin_date,'1970-01-01')
     group by t1.user_id;

思路分析：

step1：找到所有用户最后未登录日期；
step2：筛选出最后未登录日期的数据，（如果不存在未登录数据，则代表该用户一直连续登录）
step3：对筛选结果进行分组统计；
3 小结
==========================*****==========================

销售额连续3天增长的商户
0 问题描述
有一张订单记录表 t5_order 包含 订单ID(order_id),商户ID(shop_id),订单时间(order_time)和订单金额(order_amt),请查询出过去至少存在3天销售额连续增长的商户

+-----------+----------+----------------------+------------+
| order_id  | shop_id  |      order_time      | order_amt  |
+-----------+----------+----------------------+------------+
| 1         | 1001     | 2023-08-21 09:01:00  | 9.99       |
| 2         | 1001     | 2023-08-22 10:00:00  | 19.99      |
| 3         | 1001     | 2023-08-22 13:00:00  | 8.88       |
| 4         | 1001     | 2023-08-23 08:00:00  | 29.99      |
| 5         | 1001     | 2023-08-23 09:00:00  | 19.99      |
| 6         | 1001     | 2023-08-24 11:00:00  | 99.99      |
| 7         | 1001     | 2023-08-25 15:00:00  | 1.99       |
| 8         | 1001     | 2023-08-26 16:00:00  | 2.99       |
| 9         | 1001     | 2023-08-26 17:00:00  | 95.99      |
| 10        | 1002     | 2023-08-21 09:00:00  | 9.99       |
| 11        | 1002     | 2023-08-22 11:00:00  | 1.99       |
| 12        | 1002     | 2023-08-22 11:01:00  | 19.99      |
| 13        | 1002     | 2023-08-22 12:05:00  | 14.99      |
| 14        | 1002     | 2023-08-22 13:00:00  | 6.99       |
| 15        | 1002     | 2023-08-23 14:00:00  | 99.99      |
| 16        | 1002     | 2023-08-24 13:00:00  | 19.99      |
| 17        | 1002     | 2023-08-25 09:00:00  | 19.99      |
| 18        | 1002     | 2023-08-25 11:00:00  | 5.99       |
| 19        | 1002     | 2023-08-25 13:00:00  | 6.99       |
| 20        | 1002     | 2023-08-25 13:07:00  | 7.0        |
| 21        | 1002     | 2023-08-25 15:00:00  | 10.0       |
| 22        | 1002     | 2023-08-26 07:00:00  | 9.99       |
| 23        | 1003     | 2023-08-21 13:07:00  | 7.0        |
| 24        | 1003     | 2023-08-22 15:00:00  | 8.0        |
| 25        | 1003     | 2023-08-23 07:00:00  | 9.99       |
| 26        | 1003     | 2023-08-25 13:07:00  | 10.0       |
| 27        | 1003     | 2023-08-26 15:00:00  | 11.0       |
+-----------+----------+----------------------+------------+
1 数据准备
--建表语句
CREATE TABLE t5_order (
order_id bigint COMMENT '订单ID',
shop_id bigint COMMENT '商户ID',
order_time string COMMENT '订单时间',
order_amt double COMMENT '订单金额'
) COMMENT '订单记录表';
-- 插入数据

insert overwrite table  t5_order
values
   (1,1001,'2023-08-21 09:01:00',9.99),
   (2,1001,'2023-08-22 10:00:00',19.99),
   (3,1001,'2023-08-22 13:00:00',8.88),
   (4,1001,'2023-08-23 08:00:00',29.99),
   (5,1001,'2023-08-23 09:00:00',19.99),
   (6,1001,'2023-08-24 11:00:00',99.99),
   (7,1001,'2023-08-25 15:00:00',1.99),
   (8,1001,'2023-08-26 16:00:00',2.99),
   (9,1001,'2023-08-26 17:00:00',95.99),
   (10,1002,'2023-08-21 09:00:00',9.99),
   (11,1002,'2023-08-22 11:00:00',1.99),
   (12,1002,'2023-08-22 11:01:00',19.99),
   (13,1002,'2023-08-22 12:05:00',14.99),
   (14,1002,'2023-08-22 13:00:00',6.99),
   (15,1002,'2023-08-23 14:00:00',99.99),
   (16,1002,'2023-08-24 13:00:00',19.99),
   (17,1002,'2023-08-25 09:00:00',19.99),
   (18,1002,'2023-08-25 11:00:00',5.99),
   (19,1002,'2023-08-25 13:00:00',6.99),
   (20,1002,'2023-08-25 13:07:00',7.00),
   (21,1002,'2023-08-25 15:00:00',10.00),
   (22,1002,'2023-08-26 07:00:00',9.99),
   (23,1003,'2023-08-21 13:07:00',7.00),
   (24,1003,'2023-08-22 15:00:00',8.00),
   (25,1003,'2023-08-23 07:00:00',9.99),
   (26,1003,'2023-08-25 13:07:00',10.00),
   (27,1003,'2023-08-26 15:00:00',11.00);
2 数据分析
with tmp1 as
   (
     select
        shop_id,
        to_date(order_time) as order_date,
        sum(order_amt) as order_amt
     from t5_order
     group by shop_id,to_date(order_time)
   ),
tmp2 as
  (
    select
        shop_id,
        order_date,
        order_amt,
        order_amt - lag(order_amt,1,null) over (partition by shop_id order by order_date) as order_amt_diff
    from tmp1
  )

select
     shop_id
from
  (
       select
             shop_id,
             order_date,
             date_sub(order_date,row_number() over(partition by shop_id order by order_date)) as diff2
       from tmp2
       where order_amt_diff >0
  )tmp3
 group by shop_id,diff2
  having count(1) >=3;

思路分析：

step1：计算出每个商户的每天销售额；
step2：针对每个商户，计算当日与上一日的销售差额，筛选出销售额增长的记录；
step3：date_sub+row_number 计算出差值diff2，差值相同代表 销售额连续增长；
step4：根据商户，差值diff2分组，得到销售额连续3天增长的商户；
3 小结
==========================*****==========================

不及格课程数大于2的学生的平均成绩及其排名
0 问题描述
有张 学生每科科目成绩表，求不及格课程数大于2的学生的平均成绩及其成绩平均值后所在的排名。

+------+------+--------+
| sid  | cid  | score  |
+------+------+--------+
| 1    | 1    | 90     |
| 1    | 2    | 50     |
| 1    | 3    | 72     |
| 2    | 1    | 40     |
| 2    | 2    | 50     |
| 2    | 3    | 22     |
| 3    | 1    | 30     |
| 3    | 2    | 50     |
| 3    | 3    | 52     |
| 4    | 1    | 90     |
| 4    | 2    | 90     |
| 4    | 3    | 72     |
+------+------+--------+
1 数据准备
--建表语句
CREATE TABLE t6_scores (
sid bigint COMMENT '学生ID',
cid bigint COMMENT '课程ID',
score bigint COMMENT '得分'
) COMMENT '用户课程分数';
-- 插入数据
insert overwrite table t6_scores
values
  (1,1,90),
  (1,2,50),
  (1,3,72),
  (2,1,40),
  (2,2,50),
  (2,3,22),
  (3,1,30),
  (3,2,50),
  (3,3,52),
  (4,1,90),
  (4,2,90),
  (4,3,72)
2 数据分析
select
       sid,
       avg_score,
       rn
from
    (
        select
             sid,
             avg_score,
             fail_num,
             dense_rank() over(order by avg_score desc) as rn
        from
        (
          select
               sid,
               avg(score) as avg_score,
               sum(case when score <60 then 1 else 0 end ) as fail_num
          from t6_scores
          group by sid
        )t1
    )t2
  where fail_num > 2;

思路分析：

step1：计算出每个学生的平均成绩、不及格的科目数；
step2：根据平均成绩计算排名；
3 小结
==========================*****==========================

计算次日留存率
0 问题描述
有一张 用户登录记录表，已经按照用户日期进行去重处理。以用户登录的最早日期作为当日的新增用户日期，请计算次日留存率是多少。

指标定义：

次日留存用户：基准日新增的用户在第二天又登录（活跃）；

次日留存率：t+1日留存用户数/t日新增用户；


+----------+-------------+
| user_id  | login_date  |
+----------+-------------+
| aaa      | 2023-12-01  |
| bbb      | 2023-12-01  |
| bbb      | 2023-12-02  |
| ccc      | 2023-12-02  |
| bbb      | 2023-12-03  |
| ccc      | 2023-12-03  |
| ddd      | 2023-12-03  |
| ccc      | 2023-12-04  |
| ddd      | 2023-12-04  |
+----------+-------------+
1 数据准备
create table t7_login
(
user_id string COMMENT '用户ID',
login_date string COMMENT '登录日期'
) COMMENT '用户登录记录表';

insert overwrite table t7_login
values
  ('aaa','2023-12-01'),
  ('bbb','2023-12-01'),
  ('bbb','2023-12-02'),
  ('ccc','2023-12-02'),
  ('bbb','2023-12-03'),
  ('ccc','2023-12-03'),
  ('ddd','2023-12-03'),
  ('ccc','2023-12-04'),
  ('ddd','2023-12-04');
2 数据分析
select
     first_day,
     concat(per * 100,'%') as next_act_per
from
   (
       select
             first_day,
            if(count(case when date_diff = 0 then user_id end) = 0, 0,
                   count(case when date_diff = 1 then user_id end) / count(case when date_diff = 0 then user_id end))  per
      from
       (
          select
              user_id,
              min(login_date)over(partition by user_id order by login_date) as first_day,
              datediff(login_date,min(login_date)over(partition by user_id order by login_date) ) as date_diff
          from t7_login
       )t1
      group by first_day
   )t2;

思路分析：

step1：使用开窗函数计算出用户的最小登录时间作为新增日期first_day， 再计算当天日期和新增日期的时间差；
step2：根据first_day进行分组，date_diff=0的为当天新增用户，date_diff=1的为次日登录的用户；
step3：用次日留存数/新增用户数据即为留存率，因为新增可能为0，需要先判断；
3 小结
==========================*****==========================

按照顺序进行行转列拼接
0 问题描述
已知有表中含有两列数据id，val,数据内容如下，请按照id的大小将val进行拼接。

+-----+------+
| id  | val  |
+-----+------+
| 1   | 20   |
| 2   | 10   |
| 8   | 120  |
| 9   | 30   |
| 11  | 50   |
| 22  | 40   |
+-----+------+
1 数据准备
--建表语句
create table t8_concat_ordered
(
id bigint COMMENT '用户ID',
val string COMMENT '登录日期'
) COMMENT '用户登录记录表';
--插入数据
insert overwrite table  t8_concat_ordered
values
  (1,'20'),
  (2,'10'),
  (8,'120'),
  (9,'30'),
  (11,'50'),
  (22,'40')
2 数据分析
  collect_list 拼接字符串是无序的，因此即使按照顺序将原始数据排好，也不能保证结果有序。所以可以将id和val 进行拼接，这样对整个字符串进行排序就会按照id的顺序排序。

 此外需要注意，id是数字类型，直接拼接会导致按照字符顺序，即11在2前面，为解决这个问题，需要左补零。总结：使用字符串拼接以后，使用sort_array()函数，保证结果有序，再转化成字符串，最后将拼接上的id替换掉。

  collect_list不保证有序，用 lpad、concat_ws、 sort_array、regexp_replace 等函数替换。

HiveSQL题——collect_set()/collect_list()聚合函数_collect set-CSDN博客

select regexp_replace(
   concat_ws(',' , sort_array( collect_list( concat_ws(':',lpad(id,5,0),val)))),'\\d+\:',''
   )
from t8_concat_ordered;


思路分析：

step1：将ID进行左补0保证所有数据结果位数相同，然后与和val进行拼接； concat_ws(':', lpad(id, 5, 0), val)
step2：将数据进行聚合，并将结果进行排序；sort_array(collect_list(concat_ws(':',lpad(id,5,0),val)))
step3：将结果进行字符串替换，将补的ID去掉，得到最终结果；
3 小结
==========================*****==========================

所有考试科目的成绩都大于对应学科平均成绩的学生
0 问题描述
有学生每科科目成绩，找出所有科目成绩都大于对应学科的平均成绩的学生

+------+------+--------+
| sid  | cid  | score  |
+------+------+--------+
| 1    | 1    | 90     |
| 1    | 2    | 50     |
| 1    | 3    | 72     |
| 2    | 1    | 40     |
| 2    | 2    | 50     |
| 2    | 3    | 22     |
| 3    | 1    | 30     |
| 3    | 2    | 50     |
| 3    | 3    | 52     |
| 4    | 1    | 90     |
| 4    | 2    | 90     |
| 4    | 3    | 72     |
+------+------+--------+
1 数据准备
CREATE TABLE t9_scores (
  sid bigint COMMENT '学生ID',
  cid bigint COMMENT '课程ID',
  score bigint COMMENT '得分'
) COMMENT '用户课程分数';
-- 插入数据
insert overwrite table  t9_scores
values
   (1,1,90),
   (1,2,50),
   (1,3,72),
   (2,1,40),
   (2,2,50),
   (2,3,22),
   (3,1,30),
   (3,2,50),
   (3,3,52),
   (4,1,90),
   (4,2,90),
   (4,3,72)
2 数据分析
select 
     sid
from (select
             sid,
             cid,
             score,
             avg_score,
             if(score > avg_score, 0, 1) as flag
      from (select 
                   sid,
                   cid,
                   score,
                   avg(score) over (partition by cid) as avg_score
            from t9_scores) t1) t2
group by sid
having sum(flag) = 0;

3 小结
==========================*****==========================

用户行为路径分析
0 问题描述
有一张用户操作行为记录表 t10_act_log 包含用户ID(user_id),操作编号(op_id),操作时间(op_time)

要求:
 1.统计每天符合以下条件的用户数：A操作之后是B操作，AB操作必须相邻;
 2.统计每天用户行为序列为A-B-D的用户数;其中:A-B之间可以有任何其他浏览记录(如C,E等),B-D之间除了C记录可以有任何其他浏览记录(如A,E等)

+----------+--------+----------------------+
| user_id  | op_id  |       op_time        |
+----------+--------+----------------------+
| 1        | A      | 2023-10-18 12:01:03  |
| 2        | A      | 2023-10-18 12:01:04  |
| 3        | A      | 2023-10-18 12:01:05  |
| 1        | B      | 2023-10-18 12:03:03  |
| 1        | A      | 2023-10-18 12:04:03  |
| 1        | C      | 2023-10-18 12:06:03  |
| 1        | D      | 2023-10-18 12:11:03  |
| 2        | A      | 2023-10-18 12:07:04  |
| 3        | C      | 2023-10-18 12:02:05  |
| 2        | C      | 2023-10-18 12:09:03  |
| 2        | A      | 2023-10-18 12:10:03  |
| 4        | A      | 2023-10-18 12:01:03  |
| 4        | C      | 2023-10-18 12:11:05  |
| 4        | D      | 2023-10-18 12:15:05  |
| 1        | A      | 2023-10-19 12:01:03  |
| 2        | A      | 2023-10-19 12:01:04  |
| 3        | A      | 2023-10-19 12:01:05  |
| 1        | B      | 2023-10-19 12:03:03  |
| 1        | A      | 2023-10-19 12:04:03  |
| 1        | C      | 2023-10-19 12:06:03  |
| 2        | A      | 2023-10-19 12:07:04  |
| 3        | B      | 2023-10-19 12:08:05  |
| 3        | E      | 2023-10-19 12:09:05  |
| 3        | D      | 2023-10-19 12:11:05  |
| 2        | C      | 2023-10-19 12:09:03  |
| 4        | E      | 2023-10-19 12:05:03  |
| 4        | B      | 2023-10-19 12:06:03  |
| 4        | E      | 2023-10-19 12:07:03  |
| 2        | A      | 2023-10-19 12:10:03  |
+----------+--------+----------------------+
1 数据准备
create table t10_act_log(
    user_id bigint  comment'用户ID',
    op_id   string  comment'操作编号',
    op_time string  comment'操作时间'
  );

insert overwrite table  t10_act_log
values
   (1, 'A', '2023-10-18 12:01:03'),
   (2, 'A', '2023-10-18 12:01:04'),
   (3, 'A', '2023-10-18 12:01:05'),
   (1, 'B', '2023-10-18 12:03:03'),
   (1, 'A', '2023-10-18 12:04:03'),
   (1, 'C', '2023-10-18 12:06:03'),
   (1, 'D', '2023-10-18 12:11:03'),
   (2, 'A', '2023-10-18 12:07:04'),
   (3, 'C', '2023-10-18 12:02:05'),
   (2, 'C', '2023-10-18 12:09:03'),
   (2, 'A', '2023-10-18 12:10:03'),
   (4, 'A', '2023-10-18 12:01:03'),
   (4, 'C', '2023-10-18 12:11:05'),
   (4, 'D', '2023-10-18 12:15:05'),
   (1, 'A', '2023-10-19 12:01:03'),
   (2, 'A', '2023-10-19 12:01:04'),
   (3, 'A', '2023-10-19 12:01:05'),
   (1, 'B', '2023-10-19 12:03:03'),
   (1, 'A', '2023-10-19 12:04:03'),
   (1, 'C', '2023-10-19 12:06:03'),
   (2, 'A', '2023-10-19 12:07:04'),
   (3, 'B', '2023-10-19 12:08:05'),
   (3, 'E', '2023-10-19 12:09:05'),
   (3, 'D', '2023-10-19 12:11:05'),
   (2, 'C', '2023-10-19 12:09:03'),
   (4, 'E', '2023-10-19 12:05:03'),
   (4, 'B', '2023-10-19 12:06:03'),
   (4, 'E', '2023-10-19 12:07:03'),
   (2, 'A', '2023-10-19 12:10:03');
2 数据分析
问题一：统计每天符合以下条件的用户数：A操作之后是B操作，AB操作必须相邻;


select
     dt,
     count(1) as cnt
from(
       select
             user_id,
             dt,
             op_sort2
       from
          (
             select
                   user_id,
                   dt,
                   regexp_replace(op_sort1,
                                    '(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\|)', '') as op_sort2
              from
                 (
                     select
                          user_id,
                          dt,
                          sort_array(collect_list(op_str)) as sort1,
                          concat_ws(',', sort_array(collect_list(op_str))) as op_sort1
                     from
                      (
                           select
                             user_id ,
                             op_id    ,
                             op_time  ,
                             to_date(op_time)  as dt,
                             concat_ws('|',op_time, op_id) as op_str
                           from t10_act_log
                      )t1
                     group by user_id, dt
                 )t2
          )t3
      where op_sort2 like '%A,B%'
     )t4
 group by dt;




思路分析：

   collect_list拼接字符串是无序的，即便按照顺序将原始数据排好，也不能保证结果有序

step1：拼接op_time和op_id, 然后根据用户和日期进行分组，collect_list聚合出每天用户的行为，使用sort_array保证拼接后的字符串有序。
concat_ws(',', sort_array(collect_list(op_str))) as op_sort
step2：regexp_replace进行字符串替换，去掉op_time及添加的| 
step3：使用like查询包含'A,B'的记录；
step4：按照日期分组，计算每天符合条件的用户数量

问题二：统计每天用户行为序列为A-B-D的用户数 ;  其中:A-B之间可以有任何其他浏览记录(如C,E等),B-D之间除了C记录可以有任何其他浏览记录(如A,E等)


select
     dt,
     count(1) as cnt
from(
       select
             user_id,
             dt,
             op_sort2
       from
          (
             select
                   user_id,
                   dt,
                   regexp_replace(op_sort1,
                                    '(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\|)', '') as op_sort2
              from
                 (
                     select
                          user_id,
                          dt,
                          sort_array(collect_list(op_str)) as sort1,
                          concat_ws(',', sort_array(collect_list(op_str))) as op_sort1
                     from
                      (
                           select
                             user_id ,
                             op_id    ,
                             op_time  ,
                             to_date(op_time)  as dt,
                             concat_ws('|',op_time, op_id) as op_str
                           from t10_act_log
                      )t1
                     group by user_id, dt
                 )t2
          )t3
       where op_sort2 like '%A%B%D%' and op_sort2 not like '%A%B%C%D%'
     )t4
 group by dt;


3 小结
按照顺序拼接字符串；
包含'A,B';
包含’A%B%D'并且不能是‘A%B%C%D’
=================*****==========================

查询前2大和前2小用户并有序拼接
0 问题描述
有一张 用户账户表，包含年份，用户id和值,请按照年份分组，取出值前两小和前两大对应的用户id。
注意：需要保持值最小和最大的用户id排首位。

样例数据
+-------+----------+--------+
| year  | user_id  | value  |
+-------+----------+--------+
| 2022  | A        | 30     |
| 2022  | B        | 10     |
| 2022  | C        | 20     |
| 2023  | A        | 40     |
| 2023  | B        | 50     |
| 2023  | C        | 20     |
| 2023  | D        | 30     |
+-------+----------+--------+

期望结果
+-------+-----------------+-----------------+
| year  | max2_user_list  | min2_user_list  |
+-------+-----------------+-----------------+
| 2022  | A,C             | B,C             |
| 2023  | B,A             | C,D             |
+-------+-----------------+-----------------+
1 数据准备
--建表语句
create table if not exists t12_amount
(
    year    string comment '年份',
    user_id string comment '用户id',
    value   bigint comment '数值'
);

--插入数据
insert  overwrite table t12_amount
values ('2022', 'A', 30),
       ('2022', 'B', 10),
       ('2022', 'C', 20),
       ('2023', 'A', 40),
       ('2023', 'B', 50),
       ('2023', 'C', 20),
       ('2023', 'D', 30)
2 数据分析
select
     concat(max1_user_id,',',max2_user_id) as max_user_list,
     concat(min1_user_id,',',min2_user_id) as min_user_id
from
  (
      select
            year,
           max(if(rn1 = 1,user_id,null)) as max1_user_id,
           max(if(rn1 = 2,user_id,null)) as max2_user_id,
           max(if(rn2 = 1,user_id,null)) as min1_user_id,
           max(if(rn2 = 2,user_id,null)) as min2_user_id
       from
      (
          select
               year,
               user_id,
               value,
               row_number()over(partition by year order by value desc) as rn1,
               row_number()over(partition by year order by value ) as rn2
          from t12_amount
      )t1
      group by year
  )t2


思路分析：

   collect_list拼接字符串是无序的，即便按照顺序将原始数据排好，也不能保证结果有序

step1：使用row_number函数根据年份分组，对value排序得到 值前两小和前两大
step2：按照顺序拼接，得到最终结果
3 小结
==========================*****==========================

查询每个学科第三名的学生的学科成绩总成绩及总排名
0 问题描述
有一张学生成绩表，包含学生姓名、学科、成绩三个字段，请用一条SQL查询出每个学科排名第三名的学生，他的学科成绩、总成绩、以及总排名。

样例数据
+----------+----------+--------+
| student  | subject  | score  |
+----------+----------+--------+
| 张三       | 语文       | 95     |
| 李四       | 语文       | 90     |
| 王五       | 语文       | 88     |
| 赵六       | 语文       | 77     |
| 张三       | 数学       | 80     |
| 李四       | 数学       | 90     |
| 王五       | 数学       | 92     |
| 赵六       | 数学       | 84     |
| 张三       | 英语       | 82     |
| 李四       | 英语       | 93     |
| 王五       | 英语       | 88     |
| 赵六       | 英语       | 68     |
+----------+----------+--------+
1 数据准备
--建表语句
create table if not exists t13_student_score
(
    student string,
    subject string,
    score   bigint
);

--插入数据

insert overwrite table t13_student_score
values ('张三', '语文', 95),
       ('李四', '语文', 90),
       ('王五', '语文', 88),
       ('赵六', '语文', 77),
       ('张三', '数学', 80),
       ('李四', '数学', 90),
       ('王五', '数学', 92),
       ('赵六', '数学', 84),
       ('张三', '英语', 82),
       ('李四', '英语', 93),
       ('王五', '英语', 88),
       ('赵六', '英语', 68);
2 数据分析

select
      student,
      subject,
      score,
      total_score,
      total_rn
from
   (
      select
           student,
           subject,
           score  ,
           subject_rn,
           total_score,
           row_number() over(partition by subject order by total_score desc) as total_rn
      from
         (
          select
               student,
               subject,
               score  ,
               row_number() over (partition by subject order by score desc) as subject_rn,
               sum(score) over (partition by student) as total_score
          from t13_student_score
         )t1
   )t2
   where subject_rn = 3;

思路分析：

step1：每个学科内的成绩排名、每个学生总成绩;
step2：利用row_number()函数，根据学科分组，按照总分排序来计算学生总排名；
step3：限定subject_rn = 3得到每个学科排名第三的同学记录；
3 小结
==========================*****==========================

各用户最长的连续登录天数-可间断
0 问题描述
现有各用户的登录记录表t14_login_events如下，表中每行数据表达的信息是一个用户何时登录了平台。 
现要求统计【各用户最长的连续登录天数，间断一天也算作连续】，例如：一个用户在1,3,5,6登录，则视为连续6天登录。

样例数据
+----------+----------------------+
| user_id  |    login_datetime    |
+----------+----------------------+
| 100      | 2021-12-01 19:00:00  |
| 100      | 2021-12-01 19:30:00  |
| 100      | 2021-12-02 21:01:00  |
| 100      | 2021-12-03 11:01:00  |
| 101      | 2021-12-01 19:05:00  |
| 101      | 2021-12-01 21:05:00  |
| 101      | 2021-12-03 21:05:00  |
| 101      | 2021-12-05 15:05:00  |
| 101      | 2021-12-06 19:05:00  |
| 102      | 2021-12-01 19:55:00  |
| 102      | 2021-12-01 21:05:00  |
| 102      | 2021-12-02 21:57:00  |
| 102      | 2021-12-03 19:10:00  |
| 104      | 2021-12-04 21:57:00  |
| 104      | 2021-12-02 22:57:00  |
| 105      | 2021-12-01 10:01:00  |
+----------+----------------------+
期望结果
+----------+---------------+
| user_id  | max_log_days  |
+----------+---------------+
| 100      | 3             |
| 101      | 6             |
| 102      | 3             |
| 104      | 3             |
| 105      | 1             |
+----------+---------------+
1 数据准备
create table if not exists t14_login_events
(
    user_id        int comment '用户id',
    login_datetime string comment '登录时间'
)
    comment '直播间访问记录';
--数据插入
insert  overwrite table t14_login_events
values (100, '2021-12-01 19:00:00'),
       (100, '2021-12-01 19:30:00'),
       (100, '2021-12-02 21:01:00'),
       (100, '2021-12-03 11:01:00'),
       (101, '2021-12-01 19:05:00'),
       (101, '2021-12-01 21:05:00'),
       (101, '2021-12-03 21:05:00'),
       (101, '2021-12-05 15:05:00'),
       (101, '2021-12-06 19:05:00'),
       (102, '2021-12-01 19:55:00'),
       (102, '2021-12-01 21:05:00'),
       (102, '2021-12-02 21:57:00'),
       (102, '2021-12-03 19:10:00'),
       (104, '2021-12-04 21:57:00'),
       (104, '2021-12-02 22:57:00'),
       (105, '2021-12-01 10:01:00');
2 数据分析
select
     user_id,
     max(log_days) as max_log_days
from
   (
      select
           user_id,
           group_id,
           datediff(max(login_date),min(login_date)) +1 as log_days
      from
          (
            select
                  user_id,
                  login_date,
                  lag_log_date,
                  date_diff,
                  sum(if(date_diff <=2 ,0,1) )over(partition by  user_id order by login_date) as group_id
            from
               (
                 select
                      user_id,
                      login_date,
                      lag(login_date,1,null) over (partition by user_id order by login_date ) as lag_log_date,
                      datediff(login_date,lag(login_date,1,'1970-01-01') over (partition by user_id order by login_date)) as date_diff
                 from
                     (
                         select
                              user_id,
                              to_date(login_datetime)  as login_date
                         from t14_login_events
                         group by user_id,to_date(login_datetime)
                     )t1
               )t2
          )t3
        group by user_id,
             group_id
    )t4
group by user_id;

思路分析：

step1：使用to_date函数，得到登陆日期，去重处理;
step2：根据用户分组，使用lag函数获得当前行的上一行数据中的日期，使用datediff函数判断日期当期日期与上一行日期的时间差；
step3：根据date_diff结果判断 是否连续，如果date_diff <= 2，认为连续 ，赋值0，不连续则赋值为1；
step4：按照用户和group_id 分组，计算每次连续登陆的天数，再根据用户分组计算最大连续天数
3 小结
==========================*****==========================

奖金瓜分问题
0 问题描述
在活动大促中，有玩游戏瓜分奖金环节。现有奖金池为 10000元，代表奖金池中的初始额度。用户的分数信息如下表。表中的数据代表每一个用户和其对应的得分，user_id 和 score 都不会有重复值。瓜分奖金的规则如下：按照 score 从高到低依次瓜分，每个人都能分走当前奖金池里面剩余奖金的一半，当奖金池里面剩余的奖金少于 250 时（不含），则停止瓜分奖金。 现在需要查询出所有分到奖金的 user_id 和其对应的奖金。

样例数据
+----------+--------+
| user_id  | score  |
+----------+--------+
| 100      | 60     |
| 101      | 45     |
| 102      | 45     |
| 103      | 35     |
| 104      | 30     |
| 105      | 25     |
| 106      | 15     |
| 107      | 10     |
+----------+--------+
1 数据准备
--建表语句
create table if not exists t15_user_score
(
    user_id string,
    score   bigint
);

--插入数据
insert overwrite table  t15_user_score
values
   ('100',60),
   ('101',45),
   ('102',45),
   ('103',35),
   ('104',30),
   ('105',25),
   ('106',15),
   ('107',10)
2 数据分析
select
     user_id,
     score,
     prize1
from
   (
      select
            user_id,
            score,
            power(0.5, rn) * 10000 as prize1,
            power(0.5, rn - 1) * 10000 as prize2
       from
           (
              select
                   user_id,
                   score,
                   row_number() over(order by score desc) as rn
               from t15_user_score
           )t1
   )t2
 where prize2 >=250;

思路分析：

step1：使用row_number开窗，得到用户排名rn;
step2：每个人得到当前奖池的1/2，排名rn的用户得到的为 （1/2 ）^ rn * 10000，奖池剩余的也是（1/2 ）^ rn * 10000；利用hive中的幂运算函数power，例如power(2,3) = 2^ 3 =8
step3：限制瓜分条件，题目中要求当奖金池里面剩余的奖金少于 250 时（不含），则停止瓜分奖金
3 小结
参考文章：

https://www.dwsql.com/interview
————————————————

                            版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。

原文链接：https://blog.csdn.net/SHWAITME/article/details/141179664