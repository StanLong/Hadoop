package com.stanlong.chapter05

import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.streaming.api.scala._

object TransCustomPartitioner {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.fromElements(1,2,3,4,5,6,7,8)
                .partitionCustom(
                    new Partitioner[Int] {
                        // 根据 key 的奇偶性计算出数据将被发送到哪个分区
                        override def partition(key: Int, numPartitions: Int): Int = key % 2
                    },
                    data => data  // 以自身作为 key
                ).print()
        env.execute()
    }
}
