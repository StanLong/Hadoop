package com.stanlong.chapter06

import com.stanlong.chapter05.{ClickSource, Event}
import java.lang
import java.time.Duration
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.{SlidingEventTimeWindows, TumblingEventTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object ProcessLateData {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.socketTextStream("node02", 7777).map(
            data => {
                val fields = data.split(",")
                Event(fields(0).trim, fields(1).trim, fields(2).trim.toLong)
            }
          )
          .assignTimestampsAndWatermarks(
              WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner(
                    new SerializableTimestampAssigner[Event] {
                        override def extractTimestamp(t: Event, l: Long): Long = t.timestamp
                    }
                )
          )

        val outputTag = OutputTag[Event]("late-data")
        val result = stream.keyBy(data => data.url)
          .window(TumblingEventTimeWindows.of(Time.seconds(10)))
          .allowedLateness(Time.minutes(1)) // 指定窗口等待的时间
          .sideOutputLateData(outputTag) // 将迟到数据输出到侧输出流
          .aggregate(new UrlViewCountAgg, new UrlViewCountResult)

        result.print("result")


        stream.print("input")

        result.getSideOutput(outputTag).print("late data")



        env.execute()
    }

    private class UrlViewCountAgg extends AggregateFunction[Event, Long, Long] {
        override def createAccumulator(): Long = 0L

        override def add(in: Event, acc: Long): Long = acc + 1

        override def getResult(acc: Long): Long = acc

        override def merge(acc: Long, acc1: Long): Long = ???
    }

    private class UrlViewCountResult extends ProcessWindowFunction[Long, UrlView, String, TimeWindow] {
        override def process(key: String, context: Context, elements: Iterable[Long], out: Collector[UrlView]): Unit = {
            val start = context.window.getStart
            val end = context.window.getEnd
            val count = elements.iterator.next()
            out.collect(UrlView(key, start, end, count))
        }
    }
}

case class UrlView(key:String, start : Long, end : Long, count : Long)
