package com.stanlong.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Memory_Par {

    def main(args: Array[String]): Unit = {
        // 准备环境
        val spakConf = new SparkConf().setMaster("local[*]").setAppName("RDD") // [*] 表示当前系统最大可用核数，如果省略则表示用单线程模拟单核

        // spakConf.set("spark.default.parallelism", "5") // 配置默认并行度

        val sc = new SparkContext(spakConf)

        // 创建RDD
        // RDD 的并行度&分区
        // makeRDD 参数一表示序列，参数二表示分区的数量，不传表示使用默认值，默认值和CPU核数相同。
        val rdd = sc.makeRDD(List(1,2,3,4), 2)

        // 【数据是如何保存到哪个分区的】源码
        // (0 until numSlices).iterator.map{ i =>
        //     val start = ((i * length)/numSlices).toInt
        //     val end = (((i+1) * length)/numSlices).toInt
        //     (start, end)
        // }
        // 以 rdd = sc.makeRDD(List(1,2,3,4), 2) 的分区数据为例
        // part-00000 中的数据是 1, 2 。 part-00001 中的数据是3,4
        // 套用源码，length=4, numSlices = 2 。 可划分出两个区间 [0, 2), [2, 4) 按这两个区间对数据进行切分
        // 下标为0和下标为1的元素落在 [0, 2) 区间   -- 对应 part-00000
        // 下标为2和下标为3的元素落在 [3, 4) 区间   -- 对应 part-00001


        // 将处理的数据保存成分区文件
        rdd.saveAsTextFile("output")

        // 关闭环境
        sc.stop()
    }
}
