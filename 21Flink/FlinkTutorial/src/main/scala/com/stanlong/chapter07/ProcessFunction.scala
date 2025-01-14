package com.stanlong.chapter07

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object ProcessFunction {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(data => data.timestamp)

        stream.process(
            new ProcessFunction[Event, String] {
                override def processElement(i: Event, context: ProcessFunction[Event, String]#Context,
                                            collector: Collector[String]): Unit = {
                    if(i.user.equals("Mary")){
                        collector.collect(i.user)
                    }
                    else if(i.user.equals("Bob")){
                        collector.collect(i.user)
                        collector.collect(i.url)
                    }
                    println(getRuntimeContext.getIndexOfThisSubtask)
                    println(context.timerService().currentWatermark())
                }
            }
        ).print()

        env.execute()
    }

}
