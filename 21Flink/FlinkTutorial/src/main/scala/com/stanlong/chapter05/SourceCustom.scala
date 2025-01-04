package com.stanlong.chapter05

// 导入隐式类型转换，必须导入！
import org.apache.flink.streaming.api.scala._

object SourceCustom {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        // 使用自定义的数据源
        val stream = env.addSource(new ClickSource)
        stream.print()

        env.execute()
    }
}
