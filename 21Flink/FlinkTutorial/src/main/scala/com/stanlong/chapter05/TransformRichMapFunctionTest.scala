package com.stanlong.chapter05

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

object TransformRichMapFunctionTest {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(2)
        val data = env.fromElements(
            Event("zhangsan", "/fav", 1L),
            Event("lisi", "/cart", 2L),
            Event("wangwu", "/portal", 3L),
            Event("zhangsan", "/prod?id=1", 4L),
            Event("zhangsan", "/prod?id=2", 5L),
            Event("lisi", "/prod?id=3", 6L),
            Event("lisi", "/prod?id=4", 7L),
            Event("lisi", "/prod?id=5", 8L),
        )
        val stream = data.map(new MyRichMapFunction)
        stream.print()
        env.execute()
    }
}

class MyRichMapFunction extends RichMapFunction[Event, Long]{
    override def open(parameters: Configuration): Unit = {
        println("当前子任务索引号: " + getRuntimeContext.getIndexOfThisSubtask + " 开始")
    }

    override def map(in: Event): Long = in.timestamp

    override def close(): Unit = {
        println("当前子任务索引号: " + getRuntimeContext.getIndexOfThisSubtask + " 结束")
    }
}