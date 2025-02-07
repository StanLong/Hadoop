package com.stanlong.chapter09

import org.apache.flink.api.common.state.{MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.LiteralBooleanExpression
import org.apache.flink.util.Collector

case class Action(userId: String, action:String)
case class Pattern(action1:String, action2:String)

object BroadCastState {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val actionStream = env.fromElements(
            Action("Alice", "login"),
            Action("Alice", "pay"),
            Action("Bob", "login"),
            Action("Bob", "buy"),
        )

        val patternStream = env.fromElements(
            Pattern("login", "pay"),
            Pattern("login", "buy")
        )

        // 定义广播状态描述器
        val patterns = new MapStateDescriptor[Unit, Pattern]("patterns", classOf[Unit], classOf[Pattern])
        val broadCastStream = patternStream.broadcast(patterns)

        actionStream.keyBy(_.userId)
          .connect(broadCastStream)
          .process(new PatternEvaluation)
          .print()

        env.execute()
    }

    private class PatternEvaluation extends KeyedBroadcastProcessFunction[String, Action, Pattern, (String, Pattern)] {

        private lazy val prevActionState: ValueState[String] = getRuntimeContext.getState(
            new ValueStateDescriptor[String]("prev-action", classOf[String])
        )

        override def processElement(in1: Action, readOnlyContext: KeyedBroadcastProcessFunction[String, Action, Pattern, (String, Pattern)]#ReadOnlyContext, collector: Collector[(String, Pattern)]): Unit = {
            val pattern = readOnlyContext.getBroadcastState(
                new MapStateDescriptor[Unit, Pattern]("patterns", classOf[Unit], classOf[Pattern])
            ).get(Unit)

            val prevAction = prevActionState.value()

            if(pattern != null && prevAction != null){
                if(pattern.action1 == prevAction && pattern.action2 == in1.action){
                    collector.collect(readOnlyContext.getCurrentKey, pattern)
                }
            }

            prevActionState.update(in1.action)
        }

        override def processBroadcastElement(in2: Pattern, context: KeyedBroadcastProcessFunction[String, Action, Pattern, (String, Pattern)]#Context, collector: Collector[(String, Pattern)]): Unit = {
            val bcState = context.getBroadcastState(
                new MapStateDescriptor[Unit, Pattern]("patterns", classOf[Unit], classOf[Pattern])
            )
            bcState.put(Unit, in2)
        }
    }
}
