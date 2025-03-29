package com.stanlong.spark.test

import java.text.SimpleDateFormat
import org.apache.spark.{SparkConf, SparkContext}

object Test {
    def main(args: Array[String]): Unit = {
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("agent")
        val sparkContext = new SparkContext(sparkConf)

        val rdd = sparkContext.makeRDD(List(1, 2, 3, 4))
        println(rdd.aggregate(9)(_ + _, _ + _))

        sparkContext.stop()

    }
}
