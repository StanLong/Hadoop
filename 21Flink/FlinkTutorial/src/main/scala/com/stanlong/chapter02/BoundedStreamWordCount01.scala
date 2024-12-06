package com.stanlong.chapter02

import org.apache.flink.streaming.api.scala._

object BoundedStreamWordCount01 {
    def main(args: Array[String]): Unit = {
        // 创建流执行环境
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        // 读取文件获取数据流
        val lineDS = env.readTextFile("input/words.txt")
        //对数据流执行转换操作
        val wordAndOne = lineDS.flatMap(_.split(" ")).map(data => (data, 1))
        //对数据进行分组
        val wordAndOneKS = wordAndOne.keyBy(_._1)
        //对分组数据进行聚合
        val result = wordAndOneKS.sum(1)
        //打印结果
        result.print()
        //执行任务
        env.execute()
    }
}

