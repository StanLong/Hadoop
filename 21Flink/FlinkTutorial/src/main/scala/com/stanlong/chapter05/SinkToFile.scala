package com.stanlong.chapter05

import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy
import org.apache.flink.streaming.api.scala._

import java.util.concurrent.TimeUnit

object SinkToFile {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.fromElements(
            Event("zhangsan", "/portal", 1L),
            Event("lisi", "/fav", 2L),
            Event("wangwu", "/cart", 3L),
            Event("zhangsan", "/prod?id=1", 4L)
        )

        val fileSink = StreamingFileSink.forRowFormat(
            new Path("./output"),
            new SimpleStringEncoder[String]("UTF-8")
        ).withRollingPolicy(
            DefaultRollingPolicy.builder()
              .withRolloverInterval(TimeUnit.MINUTES.toMillis(15)) // 至少包含15分钟的数据
              .withInactivityInterval(TimeUnit.MINUTES.toMillis(5)) // 最近5分钟没有收到新的数据
              .withMaxPartSize(1024 * 1024 * 1024) // 文件大小已达到1 GB
              .build()
        ).build()

        stream.map(_.toString).addSink(fileSink)
        env.execute()
    }


}
