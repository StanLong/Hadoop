package com.stanlong.chapter11

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.functions.AggregateFunction

object UDFAggFunction {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val tableEnv = StreamTableEnvironment.create(env)

        tableEnv.executeSql(
            "CREATE TABLE eventTable (" +
              " uid String, " +
              " url String, " +
              " ts BIGINT, " +
              " et AS TO_TIMESTAMP( FROM_UNIXTIME(ts/1000)) ," +
              " WATERMARK FOR et AS et - INTERVAL '2' SECOND" +
              ") WITH (" +
              " 'connector' = 'filesystem', " +
              " 'path' = 'input/click.txt', " +
              " 'format' = 'csv' " +
              ")"
        )

        tableEnv.createTemporarySystemFunction("weightedAvg", classOf[weightedAvg])

        val resultTable = tableEnv.sqlQuery("select uid, weightedAvg(ts, 1) as avg_ts from eventTable group by uid")
        tableEnv.toChangelogStream(resultTable).print()
        env.execute()

    }

    case class WeightedAvgAcc(var sum : Long=0, var count : Int=0)

    class weightedAvg extends AggregateFunction[java.lang.Long, WeightedAvgAcc]{

        override def getValue(acc: WeightedAvgAcc): java.lang.Long = {
            if(acc.count == 0){
                null
            } else {
                acc.sum / acc.count
            }
        }

        override def createAccumulator(): WeightedAvgAcc = WeightedAvgAcc()

        def accumulate(acc: WeightedAvgAcc, iValue:java.lang.Long, iWeight : Int) ={
            acc.sum += iValue
            acc.count += iWeight
        }
    }
}
