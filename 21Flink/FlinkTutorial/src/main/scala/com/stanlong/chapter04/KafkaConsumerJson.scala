package com.stanlong.chapter04

import com.alibaba.fastjson.JSON
import java.text.SimpleDateFormat
import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.scala._

/**
 * kafka 消费json数据并聚合分析
 */
object KafkaConsumerJson {
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
        val topic = "test"
        val kafkaStream = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))

        val result = kafkaStream.map(line => {
            val warning_id =JSON.parseObject(line).getString("id")
            val warning_name =JSON.parseObject(line).getString("name")
            (warning_id, warning_name)
        }).map(data => (data._2, 1L))
          .keyBy(data => data._1)
          .reduce((r1, r2) => (r1._1, r1._2 + r2._2))
          .keyBy(_ => true)
          .reduce((r1, r2) => if(r1._2 > r2._2) r1 else r2)

        result.print()
        env.execute()
    }
}
