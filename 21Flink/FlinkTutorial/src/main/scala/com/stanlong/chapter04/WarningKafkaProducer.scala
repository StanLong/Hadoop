package com.stanlong.chapter04

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Random

/**
 * 读取文件中的json数据, 生成随机数组并发送到 kafka
 */
object WarningKafkaProducer {
    def main(args: Array[String]): Unit = {
        val topic = "test"
        sendTokafka(topic)
    }

    private def sendTokafka(topic: String): Unit = {
        val pro = new Properties()
        pro.put("bootstrap.servers", "node02:9092")
        pro.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        pro.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        val producer = new KafkaProducer[String, String](pro)
        val filePath = "input/warning.txt"
        val data = Source.fromFile(filePath)
        val lines = data.getLines().toList
        val array = ArrayBuffer[String]()
        for (line <- lines){
            array.append(line)
        }
        val random = new Random()
        while (true){
            val line = array(random.nextInt(array.length))
            val record = new ProducerRecord[String, String](topic, line)
            producer.send(record)
            Thread.sleep(500)
        }
        producer.close()
    }
}
