package com.stanlong.chapter11

import com.stanlong.chapter05.Event
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object TableAPI {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.fromElements(
            Event("Alice", "./home", 1000L),
            Event("Bob", "./cart", 1000L),
            Event("Alice", "./prod?id=1", 5 * 1000L),
            Event("Cary", "./home", 60 * 1000L),
            Event("Bob", "./prod?id=3", 90 * 1000L),
            Event("Alice", "./prod?id=7", 105 * 1000L)
        )

        val tableEnv = StreamTableEnvironment.create(env)
        val eventTable = tableEnv.fromDataStream(stream)
        val visitTable = tableEnv.sqlQuery("select url, user from " + eventTable)
        tableEnv.toDataStream(visitTable).print()

        env.execute()
    }
}
