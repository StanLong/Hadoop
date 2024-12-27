package com.stanlong.chapter05

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.scala._

/**
 * map 转换算子
 * 提取 Event 中的 user 字段
 */
object TransformMapTest {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1) // 设置并行度为1， 仅测试
        val stream1 = env.addSource(new ClickSource).map(data => data.user)
        val stream2 = env.addSource(new ClickSource).map(new UserExtractor)

        stream1.print("1")
        stream2.print("2")

        env.execute()

    }
}

class UserExtractor extends MapFunction[Event, String]{

    override def map(t: Event): String = t.user
}