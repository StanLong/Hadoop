package com.stanlong.spark.core.rdd.serial

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Serial {

    def main(args: Array[String]): Unit = {
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val sc = new SparkContext(sparkConf)

        //3.创建一个 RDD
        val rdd: RDD[String] = sc.makeRDD(Array("hello world", "hello spark", "hive", "stanlong"))

        val search = new Search("h")

        search.getMatch1(rdd).collect().foreach(println)
        sc.stop()
    }


    // 类的构造参数其实就是类的属性 【val search = new Search("h") 相当于调用了类的有参构造方法】，
    // 构造参数需要进行闭包检测，其实就等同于类进行闭包检测
    class Search(query:String) extends Serializable {
        def isMatch(s: String): Boolean = {
            s.contains(query)
        }

        // 函数序列化案例
        def getMatch1 (rdd: RDD[String]): RDD[String] = {
            rdd.filter(this.isMatch)
            // rdd.filter(isMatch)
        }

        // 属性序列化案例
        def getMatch2(rdd: RDD[String]): RDD[String] = {
            rdd.filter(x => x.contains(this.query))
            // rdd.filter(x => x.contains(query))
            val q = query
            rdd.filter(x => x.contains(q))
        }
    }
}
