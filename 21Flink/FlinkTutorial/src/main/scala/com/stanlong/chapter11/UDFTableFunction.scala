package com.stanlong.chapter11

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.annotation.{DataTypeHint, FunctionHint}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.functions.TableFunction
import org.apache.flink.types.Row

object UDFTableFunction {

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

        tableEnv.createTemporarySystemFunction("mySplit", classOf[MySplit])

        val resultTable = tableEnv.sqlQuery("SELECT uid, url, word, len FROM eventTable, LATERAL TABLE(mySplit(url)) as T(word, len)")

        tableEnv.toDataStream(resultTable).print()
        env.execute()
    }

    @FunctionHint(output = new DataTypeHint ("ROW<word String, len INT>"))
    class MySplit extends TableFunction[Row] {
        def eval (str : String): Unit = {
            str.split("\\?").foreach(s => collect(Row.of(s, Int.box(s.length))))
        }
    }

}
