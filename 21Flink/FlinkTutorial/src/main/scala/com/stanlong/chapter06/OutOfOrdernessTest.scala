package com.stanlong.chapter06

import java.time.Duration

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._

object OutOfOrdernessTest {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.addSource(new ClickSource)
                //插入水位线的逻辑
                .assignTimestampsAndWatermarks(
                //针对乱序流插入水位线，延迟时间设置为 5s
                WatermarkStrategy.forBoundedOutOfOrderness[Event](Duration.ofSeconds(5))
                        .withTimestampAssigner(
                            new SerializableTimestampAssigner[Event] {
                                // 指定数据中的哪一个字段是时间戳
                                override def extractTimestamp(element: Event, recordTimestamp: Long): Long = element.timestamp
                            }
                        )
                ).print()
        env.execute()
    }
}