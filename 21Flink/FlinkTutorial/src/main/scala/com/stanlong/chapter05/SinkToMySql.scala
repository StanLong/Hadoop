package com.stanlong.chapter05

import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.scala._

import java.sql.PreparedStatement

/**
 * 创建测试库, 测试表
 * create database test;
 * use test;
 * create table clicks(
 * user varchar(20) not null,
 * url varchar(100) not null
 * );
 */
object SinkToMySql {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val data = env.fromElements(
            Event("zhangsan", "/portal", 1L),
            Event("lisi", "/fav", 2L),
            Event("wangwu", "/cart", 3L),
            Event("zhangsan", "/prod?id=1", 4L)
        )

        data.addSink(JdbcSink.sink(
            "INSERT INTO clicks(user, url) VALUES(?, ?)",
            new JdbcStatementBuilder[Event] {
                override def accept(t: PreparedStatement, u: Event): Unit = {
                    t.setString(1, u.user)
                    t.setString(2, u.url)
                }
            },
            new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
              .withUrl("jdbc:mysql://node01:3306/test?useSSL=false")
              .withDriverName("com.mysql.jdbc.Driver")
              .withUsername("root")
              .withPassword("root")
              .build()
        ))

        env.execute()
    }
}
