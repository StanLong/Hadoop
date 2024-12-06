package com.stanlong.chapter02

import org.apache.flink.api.scala._

object BatchWordCount {
    def main(args: Array[String]): Unit = {
        //创建执行环境并配置并行度
        val env = ExecutionEnvironment.getExecutionEnvironment
        //读取文本文件
        val lineDS = env.readTextFile("input/words.txt")
        //对数据进行格式转换
        val wordAndOne = lineDS.flatMap(_.split(" ")).map(word => (word, 1))
        //对数据进行分组
        val wordAndOneUG = wordAndOne.groupBy(0)
        //对分组数据进行聚合
        val sum = wordAndOneUG.sum(1)
        //打印结果
        sum.print
    }
}
