package com.stanlong.chapter05

import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object SourceKafka {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        //使用 Java 配置类保存与 Kafka 连接的必要配置
        val properties = new Properties();
        properties.setProperty("bootstrap.servers", "node02:9092")
        properties.setProperty("group.id", "consumer-group")
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        properties.setProperty("value.deserializer",  "org.apache.kafka.common.serialization.StringDeserializer")
        properties.setProperty("auto.offset.reset", "latest")
        //创建一个 FlinkKafkaConsumer 对象，传入必要参数，从 Kafka 中读取数据
        // ‌SimpleStringSchema是Apache Flink中用于字符串序列化和反序列化的一个简单模式, 它可以将Kafka中的字符串数据直接转换为Flink可以处理的格式
        val kafkaStream = env.addSource(new FlinkKafkaConsumer[String]( "test", new SimpleStringSchema(), properties ))

        kafkaStream.print()

        // flink 对接 kafka 数据源实现 wordcount
        /* val  reslut = stream.flatMap(data => data.split(" ")) // 用空格切分字符串
                .map((_, 1))    // 切分以后的单词转换成一个元组
                .keyBy(_._1)    // 使用元组的第一个字段进行分组
                .sum(1)    // 对分组后的数据的第二个字段进行累加
        reslut.print() */



        env.execute()
    }
}
