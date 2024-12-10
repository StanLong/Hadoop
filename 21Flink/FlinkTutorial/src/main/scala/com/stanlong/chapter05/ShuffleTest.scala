package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._

object ShuffleTest {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        // 读取数据源，并行度为 1
        val stream = env.addSource(new ClickSource)
        stream.print("original").setParallelism(1)

        // 经洗牌后打印输出，并行度为 4
        // stream.shuffle.print("shuffle").setParallelism(4)

        env.execute()
    }
}
