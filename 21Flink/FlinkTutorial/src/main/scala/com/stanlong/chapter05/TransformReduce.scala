package com.stanlong.chapter05

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.scala._
object TransformReduceTest {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        env.addSource(new ClickSource)
                .map(r => (r.user, 1L))
                .keyBy(_._1)   //按照用户名进行分组
                // .reduce((r1, r2) => (r1._1, r1._2 + r2._2))  //计算每个用户的访问频次
                .reduce(new UserReduce) // 或者重写reduce方法
                .keyBy(_ => true)     //将所有数据都分到同一个分区
                .reduce((r1, r2) => if (r1._2 > r2._2) r1 else r2)    //通过 reduce 实现 max 功能，计算访问频次最高的用户
                .print()
        env.execute()
    }
}

class UserReduce extends ReduceFunction[(String, Long)]{

    override def reduce(t: (String, Long), t1: (String, Long)): (String, Long) = (t._1, t._2+t1._2)
}
