package com.stanlong.chapter08

import java.lang
import org.apache.flink.api.common.functions.{CoGroupFunction, JoinFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object CoGroup {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream1 = env.fromElements(
            ("a", 1000L),
            ("b", 1000L),
            ("a", 2000L),
            ("b", 2000L)
        ).assignAscendingTimestamps(_._2)

        val stream2 = env.fromElements(
            ("a", 3000L),
            ("b", 3000L),
            ("a", 4000L),
            ("b", 4000L)
        ).assignAscendingTimestamps(_._2)

        stream1.coGroup(stream2) // 同组联结
          .where(_._1)
          .equalTo(_._1)
          .window(TumblingEventTimeWindows.of(Time.seconds(5)))
          .apply(new CoGroupFunction[(String, Long), (String, Long), String] {
              override def coGroup(iterable: lang.Iterable[(String, Long)], iterable1: lang.Iterable[(String, Long)], collector: Collector[String]): Unit = {
                  collector.collect(iterable + "=>" + iterable1)
              }
          }).print()

        env.execute()
    }
}
