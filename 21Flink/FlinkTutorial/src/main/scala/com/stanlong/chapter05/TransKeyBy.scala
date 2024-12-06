package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._

object TransKeyBy {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.fromElements(
            Event("Mary", "./home", 1000L),
            Event("Bob", "./cart", 2000L),
            Event("Bob", "./home", 2000L)
        )  //指定 Event 的 user 属性作为 key

        val keyedStream = stream.keyBy(_.user)
        keyedStream.print()
        env.execute()

    }
}
