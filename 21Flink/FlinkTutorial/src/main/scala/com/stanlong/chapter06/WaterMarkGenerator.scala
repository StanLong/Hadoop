package com.stanlong.chapter06

import com.stanlong.chapter05.{ClickSource, Event}
import java.time.Duration
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, TimestampAssigner, TimestampAssignerSupplier, Watermark, WatermarkGenerator, WatermarkGeneratorSupplier, WatermarkOutput, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._

object WaterMarkGenerator {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        // env.getConfig.setAutoWatermarkInterval() 设置默认的水位线生成时间间隔

        val stream = env.addSource(new ClickSource)

        // 有序流水位线的生成方法
        stream.assignTimestampsAndWatermarks(
            WatermarkStrategy.forMonotonousTimestamps[Event]()
              .withTimestampAssigner(
                  new SerializableTimestampAssigner[Event] {
                      override def extractTimestamp(t: Event, l: Long): Long = t.timestamp
                  }
              )
        )

        // 乱序流水位线的生成方法
        stream.assignTimestampsAndWatermarks(
            WatermarkStrategy.forBoundedOutOfOrderness[Event](Duration.ofSeconds(5))
              .withTimestampAssigner(
                  new SerializableTimestampAssigner[Event] {
                      override def extractTimestamp(t: Event, l: Long): Long = t.timestamp
                  }
              )
        )


        // 自定义水位线
        stream.assignTimestampsAndWatermarks(new WatermarkStrategy[Event] {
            override def createTimestampAssigner(context: TimestampAssignerSupplier.Context): TimestampAssigner[Event] = ???

            override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[Event] = {
                new WatermarkGenerator[Event] {
                    // 基于事件生成水位线
                    override def onEvent(t: Event, l: Long, watermarkOutput: WatermarkOutput): Unit = ???
                    // 基于周期生成水位线

                    override def onPeriodicEmit(watermarkOutput: WatermarkOutput): Unit = ???
                }
            }
        })
    }

}
