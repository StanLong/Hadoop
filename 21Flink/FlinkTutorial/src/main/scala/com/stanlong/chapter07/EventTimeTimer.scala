package com.stanlong.chapter07

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object EventTimeTimer {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(data => data.timestamp)
          .keyBy(data => true)
          .process(
              new KeyedProcessFunction[Boolean, Event, String] {
                  override def processElement(i: Event,
                                              context: KeyedProcessFunction[Boolean, Event, String]#Context,
                                              collector: Collector[String]): Unit = {
                      val currentTime = context.timerService().currentWatermark()
                      collector.collect(s"数据到达，当前的处理时间: $currentTime, 当前数据时间戳是: ${i.timestamp}")

                      context.timerService().registerEventTimeTimer(currentTime + 5 * 1000)
                  }

                  override def onTimer(timestamp: Long,
                                       ctx: KeyedProcessFunction[Boolean, Event, String]#OnTimerContext,
                                       out: Collector[String]): Unit = {
                      out.collect("定时器触发，触发时间为: " + timestamp)
                  }
              }
          ).print()

        env.execute()
    }

}
