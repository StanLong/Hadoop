package com.stanlong.chapter06

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object UrlViewCount {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(data => data.timestamp)

        stream.keyBy(data => data.url)
          .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
          .aggregate(new UrlViewCountAgg, new UrlViewCountResult) // 统计窗口时间内 url 被点击的次数
          .print()

        env.execute()

    }

    class UrlViewCountAgg extends AggregateFunction[Event, Long, Long] {

        override def createAccumulator(): Long = 0L

        override def add(in: Event, acc: Long): Long = acc + 1 // 来一个数据加一个数据

        override def getResult(acc: Long): Long = acc

        override def merge(acc: Long, acc1: Long): Long = ???
    }

    class UrlViewCountResult extends ProcessWindowFunction[Long, UrlView, String, TimeWindow] {
        override def process(key: String, context: Context, elements: Iterable[Long], out: Collector[UrlView]): Unit = {
            val count = elements.iterator.next()
            val start = context.window.getStart
            val end = context.window.getEnd

            out.collect(UrlView(key, count, start, end))
        }
    }

}
