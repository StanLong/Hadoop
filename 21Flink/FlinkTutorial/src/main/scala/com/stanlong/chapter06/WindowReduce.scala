package com.stanlong.chapter06

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{EventTimeSessionWindows, SlidingEventTimeWindows, SlidingProcessingTimeWindows, TumblingEventTimeWindows, TumblingProcessingTimeWindows, WindowAssigner}
import org.apache.flink.streaming.api.windowing.time.Time

object WindowReduce {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource).assignTimestampsAndWatermarks(
            WatermarkStrategy.forMonotonousTimestamps()
              .withTimestampAssigner(
                  new SerializableTimestampAssigner[Event] {
                      override def extractTimestamp(t: Event, l: Long): Long = t.timestamp
                  }
              )
        )

        // 窗口介绍
        // stream.keyBy(_.user)
        // .window(TumblingEventTimeWindows.of(Time.hours(1), Time.minutes(10)) // 基于事件时间的滚动窗口，一小时统计一次，偏移量是10分钟。如 0：10-01：10
        // .window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8))) // 基于处理时间的滚动窗口， 从东8区开始计算
        // .window(SlidingEventTimeWindows.of(Time.hours(1), Time.minutes(10))) // 基于事件时间的滑动窗口，窗口大小1小时，滑动范围10分钟，没有偏移量
        // .window(SlidingProcessingTimeWindows.of(Time.days(1), Time.hours(-8))) // 基于处理时间的滑动容器，
        // .window(EventTimeSessionWindows.withGap(Time.seconds(10))) // 基于事件时间的会话窗口，窗口时间长度为10s
        // .countWindow(10) // 计数窗口
        // .countWindow(10, 2) // 滑动窗口，滑
        // 动步长为2

        stream.map(data => (data.user, 1))
          .keyBy(_._1)
          .window(TumblingEventTimeWindows.of(Time.seconds(5)))
          .reduce((r1, r2) => (r1._1, r1._2 + r2._2))
          .print()
        env.execute()
    }
}
