package com.stanlong.chapter11

import com.stanlong.chapter05.Event
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object TableToStream {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val eventStream = env.fromElements(
            Event("Alice", "./home", 1000L),
            Event("Bob", "./cart", 1000L),
            Event("Alice", "./prod?id=1", 5 * 1000L),
            Event("Cary", "./home", 60 * 1000L),
            Event("Bob", "./prod?id=3", 90 * 1000L),
            Event("Alice", "./prod?id=7", 105 * 1000L)
        )

        val tabEnv = StreamTableEnvironment.create(env)

        tabEnv.createTemporaryView("EventTable", eventStream)

        val aliceViewUrl = tabEnv.sqlQuery(s"select user, url from EventTable where user='Alice' ")
        val urlCountTable = tabEnv.sqlQuery(s"select user,count(url) from EventTable group by user ")


        tabEnv.toDataStream(aliceViewUrl).print("alice visit")

        // 如果sql中有聚合函数，那tableStream转dataStream时需要使用  toChangelogStream
        tabEnv.toChangelogStream(urlCountTable).print("url count")
        env.execute()
    }

}
