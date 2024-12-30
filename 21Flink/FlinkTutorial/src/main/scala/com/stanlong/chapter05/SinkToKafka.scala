package com.stanlong.chapter05

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer

import java.util.Properties

object SinkToKafka {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val properties = new Properties()
        properties.setProperty("bootstrap.servers", "node05:9092")
        val stream = env.readTextFile("input/warning.txt")
        stream.addSink(
            new FlinkKafkaProducer[String](
            "test",
            new SimpleStringSchema(),
            properties
        ))

        env.execute()
    }

}
