package com.stanlong.spark.test

import java.text.SimpleDateFormat
import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import scala.collection.mutable

object Test {
    def main(args: Array[String]): Unit = {
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("agent")
        val sparkContext = new SparkContext(sparkConf)
        val rdd = sparkContext.makeRDD(List(
            ("nba", "123"),
            ("cba", "456"),
            ("wnba", "nba"),
            ("nba", "#######")
        ))

        val partitionRDD = rdd.partitionBy(new MyPartitioner)
        partitionRDD.saveAsTextFile("datas/output")

        sparkContext.stop()

    }

    class MyPartitioner extends Partitioner {
        override def numPartitions: Int = 3

        override def getPartition(key: Any): Int = {
            key match {
                case "nba" =>0
                case "wnba" => 1
                case _ =>2
            }
        }
    }
}
