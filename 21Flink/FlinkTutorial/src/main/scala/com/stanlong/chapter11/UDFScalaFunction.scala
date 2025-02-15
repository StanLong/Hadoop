package com.stanlong.chapter11

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.functions.ScalarFunction

object UDFScalaFunction {

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
        tableEnv.createTemporarySystemFunction("myHash", classOf[MyHash])

        val resultTable = tableEnv.sqlQuery("select uid, myHash(uid) from eventTable")

        tableEnv.toDataStream(resultTable).print()

        env.execute()

    }

    private class MyHash extends ScalarFunction{
        def eval(str : String) : Int = {
            str.hashCode
        }
    }

}
