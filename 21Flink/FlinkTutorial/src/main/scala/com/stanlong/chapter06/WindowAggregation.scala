package com.stanlong.chapter06

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object WindowAggregation {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)


//        val stream = env.addSource(new ClickSource)
//          .assignTimestampsAndWatermarks(
//              WatermarkStrategy.forMonotonousTimestamps()
//                .withTimestampAssigner(
//                    new SerializableTimestampAssigner[Event] {
//                        override def extractTimestamp(t: Event, l: Long): Long = t.timestamp
//                    }
//                )
//          )

        // 跟assignTimestampsAndWatermarks相比，这是一种更简洁的写法
        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(data => data.timestamp)

        // 统计 pv/uv

        stream.keyBy( data => true)
          .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(2)))
          .aggregate(new PvUv)
          .print()

        env.execute()
    }

    private class PvUv extends AggregateFunction[Event, (Long, Set[String]), Double]{

        override def createAccumulator(): (Long, Set[String]) = (0L, Set[String]())

        override def add(in: Event, acc: (Long, Set[String])): (Long, Set[String]) = {
            (acc._1 + 1, acc._2 + in.user) // scala 里  set 集合的操作也可以直接用加号
        }

        override def getResult(acc: (Long, Set[String])): Double = {
            acc._1.toDouble / acc._2.size
        }

        override def merge(acc: (Long, Set[String]), acc1: (Long, Set[String])): (Long, Set[String]) = ???
    }
}
