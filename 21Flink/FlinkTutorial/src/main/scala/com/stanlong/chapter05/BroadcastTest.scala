package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._

object BroadcastTest {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        // 读取数据源，并行度为 1     
        val stream = env.addSource(new ClickSource)

        // 经广播后打印输出，并行度为 4
        stream.broadcast.print("broadcast").setParallelism(4)

        env.execute()
    }
}