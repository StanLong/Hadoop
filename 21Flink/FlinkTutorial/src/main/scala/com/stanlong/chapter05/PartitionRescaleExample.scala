package com.stanlong.chapter05

import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala._

object PartitionRescaleExample {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        // 使用匿名类的方式自定义数据源，这里使用了并行数据源函数的富函数版本
        env.addSource(new RichParallelSourceFunction[Int] {
            override def run(sourceContext: SourceFunction.SourceContext[Int]): Unit = {
                for (i <- 0 to 7) {
                    // 将偶数发送到下游索引为 0 的并行子任务中去
                    // 将奇数发送到下游索引为 1 的并行子任务中去
                    if ((i + 1) % 2 == getRuntimeContext.getIndexOfThisSubtask) {
                        sourceContext.collect(i + 1)
                    }
                }
            }         // 这里???是 Scala 中的占位符
            override def cancel(): Unit = ???
        }).setParallelism(2)
                .rescale
                .print()
                .setParallelism(4)
        env.execute()

        /**
         * rescale 分区观察结果可知， 1、3、5、7都在3、4分区上， 2、4、6、8都在1,2分区上
         * 3> 1
         * 1> 2
         * 4> 3
         * 2> 4
         * 4> 7
         * 1> 6
         * 3> 5
         * 2> 8
         */

        /** rebalance 分区结果如下。
         * 3> 8
         * 2> 6
         * 4> 2
         * 1> 4
         * 3> 3
         * 4> 5
         * 2> 1
         * 1> 7
         */
    }
}