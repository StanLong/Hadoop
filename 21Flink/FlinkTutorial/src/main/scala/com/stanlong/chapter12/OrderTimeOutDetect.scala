package com.stanlong.chapter12

import org.apache.flink.cep.functions.{PatternProcessFunction, TimedOutPartialMatchHandler}
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

import java.util

case class OrderEvent(userId : String, orderId : String, eventType:String, timestamp:Long)
object OrderTimeOutDetect {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val orderStream = env.fromElements(
            OrderEvent("user_1", "order_1", "create", 1000L),
            OrderEvent("user_2", "order_2", "create", 2000L),
            OrderEvent("user_1", "order_1", "modify", 10 * 1000L),
            OrderEvent("user_1", "order_1", "pay", 60 * 1000L),
            OrderEvent("user_2", "order_3", "create", 10 * 60 * 1000L),
            OrderEvent("user_2", "order_3", "pay", 20 * 60 * 1000L)
        ).assignAscendingTimestamps(_.timestamp)
          .keyBy(_.orderId)

        val pattern = Pattern.begin[OrderEvent]("create")
          .where(_.eventType == "create")
          .followedBy("pay").where(_.eventType == "pay")
          .within(Time.minutes(15))

        val patternStream = CEP.pattern(orderStream, pattern)

        val payedOrderStream = patternStream.process(new OrderPayDetect())

        payedOrderStream.getSideOutput(new OutputTag[String]("timeout")).print("timeout")
        
        payedOrderStream.print("payed")

        env.execute()
    }

    private class OrderPayDetect extends PatternProcessFunction[OrderEvent, String] with TimedOutPartialMatchHandler[OrderEvent] {
        override def processMatch(map: util.Map[String, util.List[OrderEvent]], context: PatternProcessFunction.Context, collector: Collector[String]): Unit = {
            val payEvent = map.get("pay").get(0)
            collector.collect(s"${payEvent.orderId} 已成功支付！")
        }

        override def processTimedOutMatch(map: util.Map[String, util.List[OrderEvent]], context: PatternProcessFunction.Context): Unit = {
            val createEvent = map.get("create").get(0)
            context.output(new OutputTag[String]("timeout"), s"${createEvent.userId} 的订单 ${createEvent.orderId} 超时未支付")
        }
    }
}
