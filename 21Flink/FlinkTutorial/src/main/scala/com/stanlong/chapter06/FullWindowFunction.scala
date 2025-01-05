package com.stanlong.chapter06

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object FullWindowFunction {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(data => data.timestamp)

        // 测试全容器函数，统计 uv
        stream.keyBy( data => "key")
          .window(TumblingEventTimeWindows.of(Time.seconds(10)))
          .process(new UvCountByWindow)
          .print()

        env.execute()
    }

    /**
     * 统计时间窗口内独立的 uv
     */
    private class UvCountByWindow extends ProcessWindowFunction[Event, String, String, TimeWindow]{
        override def process(key: String, context: Context, elements: Iterable[Event], out: Collector[String]): Unit = {
            var userSet = Set[String]()
            elements.foreach(userSet += _.user)
            val uv = userSet.size
            val windowStart = context.window.getStart
            val windowEnd = context.window.getEnd

            out.collect(s"窗口 $windowStart - $windowEnd 的 uv 值为: $uv")
        }

    }
}
