package com.stanlong.chapter08

import com.stanlong.chapter05.ClickSource
import org.apache.flink.streaming.api.scala._

object SplitStream {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)

        val MaryStream = stream.filter(data => data.user.equals("Mary"))
        val BobStream = stream.filter(data => data.user.eq("Bob"))
        val elseStream = stream.filter(data => data.user != "Mary" && data.user != "Bob")

        MaryStream.print("Mary")
        BobStream.print("Bob")
        elseStream.print("else")

        env.execute()
    }

}
