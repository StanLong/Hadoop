package com.stanlong.chapter11

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object TopNExample {

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

        val urlCountTable = tableEnv.sqlQuery("select uid, count(url) as cnt from eventTable group by uid")

        tableEnv.createTemporaryView("urlCountTable", urlCountTable)

        val top2Result = tableEnv.sqlQuery(
            """
              |SELECT uid, cnt, row_num
              |FROM
              |(
              |    SELECT *, ROW_NUMBER() over (ORDER BY cnt desc) as row_num
              |    FROM urlCountTable
              |)
              |WHERE row_num <= 2
              |""".stripMargin)

        tableEnv.toChangelogStream(top2Result).print()

        env.execute()

    }

}
