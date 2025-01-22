package com.stanlong.chapter08

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.functions.co.CoMapFunction
import org.apache.flink.streaming.api.scala._

object ConnectStream {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream1 = env.fromElements(1,2,3)

        val stream2 = env.fromElements(1L, 2L, 3L)

        stream1.connect(stream2)
          .map(new CoMapFunction[Int, Long, String] {
              override def map1(in1: Int): String = s"Int: ${in1}"

              override def map2(in2: Long): String = s"Long: ${in2}"
          }).print()
        env.execute()
    }

}
