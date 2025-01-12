package com.stanlong.chapter06

import com.stanlong.chapter05.Event
import java.time.Duration
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object WatermarkOutOfOrderness {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.socketTextStream("node02", 7777)
          .map(data => {
              val fields = data.split(",")
              Event(fields(0).trim, fields(1).trim, fields(2).trim.toLong)
          })

        stream.assignTimestampsAndWatermarks(
            WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(5))
              .withTimestampAssigner(
                  new SerializableTimestampAssigner[Event]{
                      override def extractTimestamp(t: Event, l: Long): Long = t.timestamp
                  }
              )
        ).keyBy(_.user)
          .window(TumblingEventTimeWindows.of(Time.seconds(10)))
          .process(new WatermarkWindowResult)
          .print()

        env.execute()

    }

    private class WatermarkWindowResult extends ProcessWindowFunction[Event, String, String, TimeWindow] {
        override def process(user: String, context: Context, elements: Iterable[Event], out: Collector[String]): Unit = {
            val start = context.window.getStart
            val end = context.window.getEnd
            val count = elements.size

            val currentWatermark = context.currentWatermark

            out.collect(s"窗口 $start - $end， 用户 $user 的活跃度为 $count, 水位线现在位于: $currentWatermark")
        }
    }
}
