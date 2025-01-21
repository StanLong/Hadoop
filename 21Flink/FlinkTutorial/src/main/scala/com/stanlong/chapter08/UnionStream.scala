package com.stanlong.chapter08

import com.stanlong.chapter05.Event
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object UnionStream {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream1 = env.socketTextStream("node02", 7777)
          .map(data => Event(data.split(",")(0).trim, data.split(",")(1).trim, data.split(",")(2).trim.toLong))
          .assignAscendingTimestamps(data => data.timestamp)

        val stream2 = env.socketTextStream("node02", 8888)
          .map(data => Event(data.split(",")(0).trim, data.split(",")(1).trim, data.split(",")(2).trim.toLong))
          .assignAscendingTimestamps(data => data.timestamp)

        stream1.union(stream2)
          .process(new ProcessFunction[Event, String] {
              override def processElement(i: Event, context: ProcessFunction[Event, String]#Context, collector: Collector[String]): Unit = {
                  collector.collect(s"当前水位线: ${context.timerService().currentWatermark()}")
              }
          }).print()

        env.execute()
    }
}
