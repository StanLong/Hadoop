package com.stanlong.chapter12

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

case class LoginEvent(userId: String, ipAddress: String, eventType: String, timestamp: Long)

object NFAExample {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val loginStream = env.fromElements(
            LoginEvent("user_1", "192.168.0.1", "fail", 2000L),
            LoginEvent("user_1", "192.168.0.2", "fail", 3000L),
            LoginEvent("user_2", "192.168.1.29", "fail", 4000L),
            LoginEvent("user_1", "171.56.23.10", "fail", 5000L),
            LoginEvent("user_2", "192.168.1.29", "success", 6000L),
            LoginEvent("user_2", "192.168.1.29", "fail", 7000L),
            LoginEvent("user_2", "192.168.1.29", "fail", 8000L)
        ).assignAscendingTimestamps(_.timestamp)
          .keyBy(_.userId)
          .flatMap(new StateMachineMapper)
          .print()

        env.execute()

    }

    private class StateMachineMapper extends RichFlatMapFunction[LoginEvent, String] {
        override def flatMap(in: LoginEvent, collector: Collector[String]): Unit = {
            lazy val currentState = getRuntimeContext.getState(
                new ValueStateDescriptor[State]("state", classOf[State])
            )

            if(currentState.value() == null){
                currentState.update(Initial)
            }

            val nextState = transition(currentState.value(), in.eventType)

            nextState match {
                case Matched => collector.collect(s"${in.userId} 连续三次登录失败")
                case Terminal => currentState.update(Initial)
                case _ => currentState.update(nextState)
            }
        }
    }

    private sealed trait State
    private case object Initial extends State
    private case object Terminal extends State
    private case object Matched extends State
    private case object S1 extends State
    private case object S2 extends State

    private def transition(state : State, eventType: String): State = {
        (state, eventType) match {
            case (Initial, "success") => Terminal
            case (Initial, "fail") => S1
            case (S1, "success") => Terminal
            case (S1, "fail") => S2
            case (S2, "success") => Terminal
            case (S2, "fail") => Matched
        }
    }

}
