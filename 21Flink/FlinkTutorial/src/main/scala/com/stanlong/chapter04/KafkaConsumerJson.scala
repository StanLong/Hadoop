package com.stanlong.chapter04

import com.alibaba.fastjson.JSON
import java.text.SimpleDateFormat
import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.scala._

object KafkaConsumerJson {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        //使用 Java 配置类保存与 Kafka 连接的必要配置
        val properties = new Properties();
        properties.setProperty("bootstrap.servers", "node02:9092")
        properties.setProperty("group.id", "consumer-group")
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        properties.setProperty("auto.offset.reset", "latest")
        //创建一个 FlinkKafkaConsumer 对象，传入必要参数，从 Kafka 中读取数据
        val kafkaStream = env.addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties))

        kafkaStream.print()

        val result = kafkaStream.map(line => {
            val userid=JSON.parseObject(line).getString("userid")
            val item=JSON.parseObject(line).getString("item")
            val action=JSON.parseObject(line).getString("action")
            val times=JSON.parseObject(line).getString("times")
            var time=0L
            try{
                time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(times).getTime //时间戳类型
            } catch {
                case e: Exception => print( e.getMessage)
            }
            (userid.toInt, item.toString,action.toString ,time.toLong)
        }).print()

        env.execute()
    }
}
