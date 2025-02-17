package com.stanlong.chapter12

import java.util
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.cep.{PatternSelectFunction, scala}
import org.apache.flink.streaming.api.scala._

object LoginFailDetect {

    private case class LoginEvent(userId : String, ipAddr : String, eventType: String, timestamp: Long)

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

        val pattern = Pattern.begin[LoginEvent]("firstFail").where(_.eventType == "fail")
          .next("secondFail").where(_.eventType == "fail")
          .next("thirdFail").where(_.eventType == "fail")

        val patternStream: scala.PatternStream[LoginEvent] = CEP.pattern(loginStream.keyBy(_.userId), pattern)

        val resultStream = patternStream.select(new PatternSelectFunction[LoginEvent, String] {
            override def select(map: util.Map[String, util.List[LoginEvent]]): String = {
                val firstFail = map.get("firstFail").get(0)
                val secondFail = map.get("secondFail").get(0)
                val thirdFail = map.get("thirdFail").get(0)
                s"${firstFail.userId} 连续三次登录失败！ 登录时间 ${firstFail.timestamp}, ${secondFail.timestamp}, ${thirdFail.timestamp}"
            }
        })

        resultStream.print()
        env.execute()
    }
}
