package com.stanlong.chapter04

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeFilter
import java.text.SimpleDateFormat
import java.util.{Date, Properties}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import scala.collection.JavaConverters
import scala.util.Random.shuffle


// 参考地址： https://blog.csdn.net/lbship/article/details/109517017
object KafkaProducerJson {
    def main(args: Array[String]): Unit = {
        val topic = "test"
        SendtoKafka(topic)
    }

    private def SendtoKafka(topic: String): Unit = {
        val pro = new Properties()
        pro.put("bootstrap.servers", "node02:9092")
        pro.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        pro.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        val producer = new KafkaProducer[String, String](pro)
        var member_id = Array.range(1, 10)
        var goods = Array("Milk", "Bread", "Rice") //为了尽快显示效果，减少商品品相
        var action = Array("click", "buy")
        //var ts=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format( ZonedDateTime.now())

        while (true) {

            var ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            var map=new scala.collection.mutable.HashMap[String,Any]()
            map.put("userid",shuffle(member_id.toList).head)
            map.put("item",shuffle(goods.toList).head)
            map.put("action",shuffle(action.toList).head)
            map.put("times",ts)

            // 参考地址：https://blog.csdn.net/OldDirverHelpMe/article/details/103713625
            val jsons = JSON.toJSONString(JavaConverters.mapAsJavaMapConverter(map).asJava,new Array[SerializeFilter](0))
            println(jsons)
            var record = new ProducerRecord[String, String](topic, jsons)
            producer.send(record)
            Thread.sleep(2000)
        }
        producer.close()
    }
}
