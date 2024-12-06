# flink对接kafka消费者

```scala
package com.atguigu.chapter05

import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object SourceKafkaTest {
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
        val stream = env.addSource(new FlinkKafkaConsumer[String]( "test", new SimpleStringSchema(), properties ))

        stream.print("kafka")
        env.execute()
    }
}
```

