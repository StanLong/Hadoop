package com.stanlong.chapter09

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object KeyedValueState {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(_.timestamp)
          .keyBy(_.user)
          .process(new PeriodicPv)
          .print()
        env.execute()
    }

    private class PeriodicPv extends KeyedProcessFunction[String, Event, String]{

        private lazy val countState : ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("count", classOf[Long]))

        private lazy val timestampState : ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("timestamp", classOf[Long]))

        override def processElement(i: Event, context: KeyedProcessFunction[String, Event, String]#Context, collector: Collector[String]): Unit = {
            val count = countState.value()
            countState.update(count + 1)

            if(timestampState.value() == 0L){
                context.timerService().registerEventTimeTimer(i.timestamp + 10 * 1000L)
                timestampState.update(i.timestamp + 10 * 1000L)
            }
        }

        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, Event, String]#OnTimerContext, out: Collector[String]): Unit = {
            out.collect(s"用户 ${ctx.getCurrentKey} 的pv值为 ${countState.value()}")
            timestampState.clear()
        }
    }



}
