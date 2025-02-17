package com.stanlong.chapter11

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.Expressions.$
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment


object BasicTable {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        // 1. 创建表环境,  直接基于流执行环境创建
        val tableEnv = StreamTableEnvironment.create(env)

        // 2. 创建表
        // 2.1 连接器表
        tableEnv.executeSql("CREATE TABLE eventTable (" +
            " uid String, " +
            " url String, " +
            " ts BIGINT " +
            ") WITH (" +
            " 'connector' = 'filesystem', " +
            " 'path' = 'input/click.txt', " +
            " 'format' = 'csv' " +
            ")" )

        // 3. 表的查询转换
        // 3.1 使用sql
        val resultTable = tableEnv.sqlQuery("select uid, url ,ts from eventTable where uid= 'Alice' ")

        // 3.2 或者使用 Table API
        val resultTable2 = tableEnv.from("eventTable")
          .where($("url").isEqual("./home"))
          .select($("url"), $("uid"), $("ts"))

        // 4. 输出表的创建
        tableEnv.executeSql("CREATE TABLE outputTable (" +
          " user_name String, " +
          " url String, " +
          " `timestamp` BIGINT " +
          ") WITH (" +
          " 'connector' = 'filesystem', " +
          " 'path' = 'output', " +
          " 'format' = 'csv' " +
          ")" )

        // 5. 将结果写入输出表中
        resultTable.executeInsert("outputTable")

    }
}
