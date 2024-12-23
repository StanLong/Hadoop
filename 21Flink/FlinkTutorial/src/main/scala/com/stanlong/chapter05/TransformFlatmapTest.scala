package com.stanlong.chapter05

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object TransformFlatmapTest {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        val stream = env.addSource(new ClickSource).flatMap(new UserFlatmap)
        stream.print()
        env.execute()
    }

}

class UserFlatmap extends FlatMapFunction[Event, String]{

    override def flatMap(t: Event, collector: Collector[String]): Unit = {
        if(t.user.equals("Mary")){
            collector.collect(t.user)
        }
        else if(t.user.equals("Bob")){
            collector.collect(t.user)
            collector.collect(t.url)
        }
    }
}