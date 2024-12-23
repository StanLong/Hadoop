package com.stanlong.chapter05

import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.streaming.api.scala._

object TransformKeyBy {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(4)

        val stream = env.fromElements(
            Event("Mary", "./home", 1000L),
            Event("Bob", "./cart", 2000L),
            Event("Bob", "./home", 2000L)
        )  //指定 Event 的 user 属性作为 key

        // val keyedStream1 = stream.keyBy(_.user)
        val keyedStream2 = stream.keyBy(new UserKeySelector)
        // keyedStream1.print()
        keyedStream2.print()
        env.execute()

    }
}

class UserKeySelector extends KeySelector[Event, String]{
    
    override def getKey(in: Event): String = in.user
}
