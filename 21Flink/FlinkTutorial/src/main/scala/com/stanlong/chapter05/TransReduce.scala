package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._
object TransReduce {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        env.addSource(new ClickSource)
                .map(r => (r.user, 1L))    //按照用户名进行分组
                .keyBy(_._1)    //计算每个用户的访问频次
                .reduce((r1, r2) => (r1._1, r1._2 + r2._2))    //将所有数据都分到同一个分区
                .keyBy(_ => true)    //通过 reduce 实现 max 功能，计算访问频次最高的用户
                .reduce((r1, r2) => if (r1._2 > r2._2) r1 else r2)
                .print()
        env.execute()
    }
}
