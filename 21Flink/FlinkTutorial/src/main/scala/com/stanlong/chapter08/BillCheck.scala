package com.stanlong.chapter08

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object BillCheck {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val appStream = env.fromElements(
            ("order-1", "success", 1000L),
            ("order-2", "success", 2000L)
        ).assignAscendingTimestamps(_._3)

        val thirdPartStream = env.fromElements(
            ("order-1", "success", "wechat", 3000L),
            ("order-3", "success", "alipay", 4000L)
        ).assignAscendingTimestamps(_._4)

        appStream.connect(thirdPartStream)
          .keyBy(_._1, _._1)
          .process(new CoProcessFunction[(String, String, Long), (String, String, String, Long), String] {

              var appEvent: ValueState[(String, String, Long)] = _
              var thirdEvent: ValueState[(String, String, String, Long)] = _

              override def open(parameters: Configuration): Unit = {
                  appEvent = getRuntimeContext.getState(new ValueStateDescriptor[(String, String, Long)]("app-event", classOf[(String, String, Long)]))
                  thirdEvent = getRuntimeContext.getState(new ValueStateDescriptor[(String, String, String, Long)]("third-event", classOf[(String, String, String, Long)]))
              }

              override def processElement1(in1: (String, String, Long), context: CoProcessFunction[(String, String, Long), (String, String, String, Long), String]#Context, collector: Collector[String]): Unit = {
                  if(thirdEvent.value() != null){
                      collector.collect(s"${in1._1} 对账成功")
                      thirdEvent.clear()
                  } else {
                      context.timerService().registerEventTimeTimer(in1._3 + 5000)
                      appEvent.update(in1)
                  }
              }

              override def processElement2(in2: (String, String, String, Long), context: CoProcessFunction[(String, String, Long), (String, String, String, Long), String]#Context, collector: Collector[String]): Unit = {
                  if(appEvent.value() != null){
                      collector.collect(s"${in2._1} 对账成功")
                      appEvent.clear()
                  } else {
                      context.timerService().registerEventTimeTimer(in2._4 + 5000)
                      thirdEvent.update(in2)
                  }
              }

              override def onTimer(timestamp: Long, ctx: CoProcessFunction[(String, String, Long), (String, String, String, Long), String]#OnTimerContext, out: Collector[String]): Unit = {
                  if(appEvent.value() != null){
                      out.collect(s"${appEvent.value()._1} 对账失败， 第三方支付事件未到")
                  }

                  if(thirdEvent.value() != null){
                      out.collect(s"${thirdEvent.value()._1} 对账失败， app支付事件未到")
                  }
                  appEvent.clear()
                  thirdEvent.clear()

              }
          }).print()

        env.execute()

    }




}