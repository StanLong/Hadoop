package com.stanlong.spark.test

import java.text.SimpleDateFormat
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable

object Test {
    def main(args: Array[String]): Unit = {
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("agent")
        val sparkContext = new SparkContext(sparkConf)
        // wordCount1(sparkContext)
        // wordCount2(sparkContext)
        // wordCount3(sparkContext)
        // wordCount4(sparkContext)
        // wordCount5(sparkContext)
        // wordCount6(sparkContext)
        // wordCount7(sparkContext)
        // wordCount8(sparkContext)
        wordCount9(sparkContext)


        sparkContext.stop()

    }

    def wordCount1(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val group = data.groupBy(word => word)
        val result = group.mapValues(
            iter => iter.size
        )
        result.collect().foreach(println)
    }

    def wordCount2(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val mapRDD = data.map(
            data => (data, 1)
        )
        val group = mapRDD.groupByKey()
        val result = group.mapValues(
            iter => iter.size
        )
        result.collect().foreach(println)

    }

    def wordCount3(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val mapRDD = data.map(data => (data, 1))
        val result = mapRDD.reduceByKey((x, y) => x + y)
        result.collect().foreach(println)
    }

    def wordCount4(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val mapRDD = data.map(data => (data, 1))
        val result = mapRDD.aggregateByKey(0)((x, y) => x + y, (x, y) => x + y)
        result.collect().foreach(println)
    }

    def wordCount5(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val mapRDD = data.map(data => (data, 1))
        val result = mapRDD.foldByKey(0)((x,y) => x+y)
        result.collect().foreach(println)
    }

    def wordCount6(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val mapRDD = data.map(data => (data, 1))
        val result = mapRDD.combineByKey(
            v => v,
            (x: Int, y) => x + y,
            (x: Int, y: Int) => x + y
        )
        result.collect().foreach(println)
    }

    def wordCount7(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val mapRDD = data.map(data => (data, 1))
        val result = mapRDD.countByKey()
        println(result)
    }

    def wordCount8(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val data = rdd.flatMap(data => data.split(" "))
        val result = data.countByValue()
        println(result)
    }

    def wordCount9(sparkContext : SparkContext): Unit = {
        val rdd = sparkContext.makeRDD(List("Hello World","Hello Scala"))
        val words = rdd.flatMap(_.split(" "))
        val mapWord = words.map(
            word => {
                mutable.Map[String, Long]((word, 1L))
            }
        )
        val wordCount = mapWord.reduce(
            (map1, map2) => {
                map2.foreach {
                    case (word, count) => {
                        val newCount = map1.getOrElse(word, 0L) + count
                        // update方法的作用是为map更新或添加一对新的键值对，这个添加是在原map上进行的，原map会改变
                        map1.update(word, newCount) // Map(Hello -> 2, Scala -> 1, World -> 1)

                        // updated方法也是更新或添加一对新的键值对，但是不改变原map，而是返回一个包含更新的新map
                        // map1.updated(word, newCount) // Map(Hello -> 1)
                    }
                }
                map1
            }
        )
        println(wordCount)
    }



}
