package com.stanlong.chapter08

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object SideOutputStream {

    val maryTag = OutputTag[(String, String, Long)](id = "mary-tag")
    val bobTag = OutputTag[(String, String, Long)]("bob-tag")

    def main(args: Array[String]): Unit = {

        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)

        val elseStream = stream.process(
            new ProcessFunction[Event, Event] {
                override def processElement(i: Event, context: ProcessFunction[Event, Event]#Context, collector: Collector[Event]): Unit = {
                    if (i.user.equals("Mary")){
                        context.output(maryTag, (i.user, i.url, i.timestamp))
                    } else if(i.user.eq("Bob")){
                        context.output(bobTag,(i.user, i.url, i.timestamp))
                    } else{
                        collector.collect(i)
                    }
                }
            }
        )

        elseStream.print("else")
        elseStream.getSideOutput(maryTag).print("mary")
        elseStream.getSideOutput(bobTag).print("bob")

        env.execute()
    }
}
