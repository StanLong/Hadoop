package com.stanlong.chapter11

import java.sql.Timestamp
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.Expressions.{$, call}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.functions.TableAggregateFunction
import org.apache.flink.util.Collector

object UDFTableAggFunction {

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

        tableEnv.createTemporarySystemFunction("top2", classOf[Top2])

        val urlCountWindowTable = tableEnv.sqlQuery(
            """
              |SELECT uid, count(url) as cnt, window_start as wstart, window_end as wend
              |FROM TABLE(
              |    TUMBLE(
              |        TABLE eventTable,
              |        DESCRIPTOR(et),
              |        INTERVAL '1' HOUR
              |    )
              |)
              |GROUP BY uid, window_start, window_end
              |""".stripMargin)

        urlCountWindowTable.groupBy($("wend"))
          .flatAggregate(call("top2", $("uid"), $("cnt"), $("wstart"), $("wend")))
          .select($("uid"), $("rank"), $("cnt"), $("wend"))

        tableEnv.toChangelogStream(urlCountWindowTable).print()

        env.execute()
    }



    case class Top2Result(uid : String, window_start : Timestamp, window_end : Timestamp, cnt : Long , rank:Int)

    case class Top2Accumulator(var maxCount : Long, var secondMaxCount : Long, var uid1 : String, var uid2 : String, var window_start : Timestamp, var window_end : Timestamp)

    class Top2 extends TableAggregateFunction[Top2Result, Top2Accumulator]{

        override def createAccumulator(): Top2Accumulator = Top2Accumulator(Long.MinValue, Long.MinValue, null, null, null, null)

        def accumulate(acc : Top2Accumulator, uid:String, cnt : Long, window_start : Timestamp, window_end : Timestamp): Unit = {
            acc.window_start = window_start
            acc.window_end = window_end
            if(cnt > acc.maxCount){
                acc.secondMaxCount = acc.maxCount
                acc.uid2 = acc.uid1
                acc.maxCount = cnt
                acc.uid1 = uid
            } else if(cnt > acc.secondMaxCount){
                acc.secondMaxCount = cnt
                acc.uid2 = uid
            }
        }

        def emitValue(acc : Top2Accumulator, out:Collector[Top2Result]): Unit = {
            if(acc.maxCount != Long.MinValue){
                out.collect(Top2Result(acc.uid1, acc.window_start, acc.window_end, acc.maxCount, 1))
            }
            if(acc.secondMaxCount != Long.MinValue){
                out.collect(Top2Result(acc.uid2, acc.window_start, acc.window_end, acc.secondMaxCount, 2))
            }
        }
    }



}
