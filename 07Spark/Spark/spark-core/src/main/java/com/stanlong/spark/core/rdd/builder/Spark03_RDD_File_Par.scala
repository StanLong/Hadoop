package com.stanlong.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_File_Par {

    def main(args: Array[String]): Unit = {
        // 准备环境
        val spakConf = new SparkConf().setMaster("local[*]").setAppName("RDD") // [*] 表示当前系统最大可用核数，如果省略则表示用单线程模拟单核
        val sc = new SparkContext(spakConf)

        // textFile
        // 第一个参数是文件路径，第二个参数是分区数量 math.min(defaultParallelism, 2)，defaultParallelism 为系统核数，不设置的话默认不会超过2
        // val rdd = sc.textFile("datas/1.txt")
        val rdd = sc.textFile("datas/1.txt", 5)

        // 分区数量的计算方式
        // 举例如下：
        // datas/1.txt
        // 1
        // 2
        // 3
        // 这个文件7个字节
        // 那么 7 / 3 = 2（Byte)..1 也就说每个分区理论应该放2个字节（标准分区）的数据，但是还余下了1个字节，
        // 这时候要根据1.1规则进行判断，如果剩余的分区大于标准分区的10%，则成为一个新的分区，在这里 1 除以 2 = 0.5所以会产生新的分区。
        // 所以，产生的分区数和minPartitions相等或者minPartitions+1，所以这里应该是3+1=4个分区

        // 分区数据的计算方式
        // 1. 文件中的行是不可分割的单位
        // 2. 字节对应偏移量，读取数据时以偏移量为单位， 偏移量不会被重新读取
        // 举例如下：
        // datas/1.txt
        // 1  --》 偏移量[0,2] 末尾会有两个换行符偏移量为2（我们看不到）
        // 2  --》 偏移量[3,5]
        // 3  --》 偏移量[6,7]
        // 分区0  -》 分区0只能放两个字节，而第一行是三个字节超出了理论偏移量，但是行不可拆分。所以分区0里放的是 1@@ (@表示换行符)
        // 分区1  -》 分区1只能放两个字节，而第二行是三个字节超出了理论偏移量，但是行不可拆分。所以分区1里放的是 2@@ (@表示换行符)
        // 分区2  -》 分区2只能放两个字节，而第三行是一个字节，所以分区3里放的是 3
        // 分区4  -》 偏移量全部读完，这个分区为空





        // 将处理的数据保存成分区文件
        rdd.saveAsTextFile("output")

        // 关闭环境
        sc.stop()
    }
}
