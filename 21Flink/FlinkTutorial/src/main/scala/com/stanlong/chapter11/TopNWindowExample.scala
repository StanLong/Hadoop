package com.stanlong.chapter11

import com.stanlong.chapter05.Event
import java.time.Duration
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.Expressions.$
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object TopNWindowExample {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val tableEnv = StreamTableEnvironment.create(env)

        val eventStream = env.fromElements(
            Event("Alice", "./home", 1000L),
            Event("Bob", "./cart", 1000L),
            Event("Alice", "./prod?id=1", 25 * 60 * 1000L),
            Event("Alice", "./prod?id=4", 55 * 60 * 1000L),
            Event("Bob", "./prod?id=5", 3600 * 1000L + 60 * 1000L),
            Event("Cary", "./home", 3600 * 1000L + 30 * 60 * 1000L),
            Event("Cary", "./prod?id=7", 3600 * 1000L + 59 * 60 * 1000L)
        ).assignTimestampsAndWatermarks(
            WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(2))
              .withTimestampAssigner(new SerializableTimestampAssigner[Event] {
                  override def extractTimestamp(t: Event, l: Long): Long = t.timestamp
              })
        )

        val eventTable = tableEnv.fromDataStream(eventStream, $("url"), $("user").as("uid"), $("timestamp").as("ts"), $("et").rowtime())
        tableEnv.createTemporaryView("eventTable", eventTable)

        val urlCountWindowTable = tableEnv.sqlQuery(
            """
              |SELECT uid, count(url) as cnt, window_start, window_end
              |FROM TABLE(
              |    TUMBLE(
              |        TABLE eventTable,
              |        DESCRIPTOR(et),
              |        INTERVAL '1' HOUR
              |    )
              |)
              |GROUP BY uid, window_start, window_end
              |""".stripMargin)

        tableEnv.createTemporaryView("urlCountWindowTable", urlCountWindowTable)

        val top2Result = tableEnv.sqlQuery(
            """
              |SELECT uid, cnt, row_num
              |FROM
              |(
              |    SELECT *, ROW_NUMBER() over (PARTITION BY window_start, window_end ORDER BY cnt desc) as row_num
              |    FROM urlCountWindowTable
              |)
              |WHERE row_num <= 2
              |""".stripMargin)

        tableEnv.toChangelogStream(top2Result).print()
        env.execute()


    }

}
