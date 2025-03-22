package com.stanlong.spark.core

import org.apache.spark.{SparkConf, SparkContext}

object Test {
    def main(args: Array[String]): Unit = {
        val rddConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val sparkContext = new SparkContext(rddConf)

        val rdd = sparkContext.makeRDD(List(1, 2, 3, 4), 2)


        rdd.collect().foreach(println)
        sparkContext.stop()


    }

}
