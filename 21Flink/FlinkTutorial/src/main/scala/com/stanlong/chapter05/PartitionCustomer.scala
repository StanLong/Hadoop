package com.stanlong.chapter05

import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.streaming.api.scala._

object PartitionCustomer {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        val stream = env.fromElements(1,2,3,4,5,6,7,8)
        stream.partitionCustom( //  partitionCustom 有两个参数 def partitionCustom[K: TypeInformation](partitioner: Partitioner[K], fun: T => K): DataStream[T]
            new Partitioner[Int] {
                override def partition(k: Int, i: Int): Int = k % 2
            },
            data => data
        ).print("PartitionCustomer").setParallelism(4)
        env.execute()
    }
}
