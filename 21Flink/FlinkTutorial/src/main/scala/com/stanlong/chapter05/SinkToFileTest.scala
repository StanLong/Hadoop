package com.stanlong.chapter05

import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy


import org.apache.flink.streaming.api.scala._

import java.util.concurrent.TimeUnit

object SinkToFileTest {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(4)

        val stream = env.fromElements(
            Event("Mary", "./home", 1000L),
            Event("Bob", "./cart", 2000L),
            Event("Alice", "./prod?id=100", 3000L),
            Event("Alice", "./prod?id=200", 3500L),
            Event("Bob", "./prod?id=2", 2500L),
            Event("Alice", "./prod?id=300", 3600L),
            Event("Bob", "./home", 3000L),
            Event("Bob", "./prod?id=1", 2300L),
            Event("Bob", "./prod?id=3", 3300L)
        )

        val fileSink = StreamingFileSink
                .forRowFormat(new Path("./output"), new SimpleStringEncoder[String]("UTF-8"))
                .withRollingPolicy( // 通过.withRollingPolicy()方法指定“滚动策略”
                    DefaultRollingPolicy.builder()
                        .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
                        .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
                        .withMaxPartSize(1024 * 1024 * 1024)
                        .build()
                    ).build
        stream.map(_.toString).addSink(fileSink)
        env.execute()
    }
}