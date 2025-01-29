package com.stanlong.chapter09

import com.stanlong.chapter05.ClickSource
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object KeyedListState {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream1 = env.fromElements(
              ("a", "stream-1", 1000L),
              ("b", "stream-1", 2000L)
          ).assignAscendingTimestamps(_._3)

        val stream2 = env.fromElements(
              ("a", "stream-1", 3000L),
              ("b", "stream-1", 4000L)
          ).assignAscendingTimestamps(_._3)

        stream1.keyBy(_._1)
          .connect(stream2.keyBy(_._1))
          .process(new TwoStreamJoin)
          .print()
        env.execute()
    }

    private class TwoStreamJoin extends CoProcessFunction[(String, String, Long),(String, String, Long), String] {

        lazy val stream1ListState : ListState[(String, String, Long)] = getRuntimeContext.getListState(
            new ListStateDescriptor[(String, String, Long)]("stream1-list", classOf[(String, String, Long)])
        )

        lazy val stream2ListState : ListState[(String, String, Long)] = getRuntimeContext.getListState(
            new ListStateDescriptor[(String, String, Long)]("stream2-list", classOf[(String, String, Long)])
        )

        override def processElement1(in1: (String, String, Long), context: CoProcessFunction[(String, String, Long), (String, String, Long), String]#Context, collector: Collector[String]): Unit = {
            stream1ListState.add(in1)

            import scala.collection.convert.ImplicitConversions._
            for(in2 <- stream2ListState.get()){
                collector.collect(in1 + "=>" + in2)
            }
        }

        override def processElement2(in2: (String, String, Long), context: CoProcessFunction[(String, String, Long), (String, String, Long), String]#Context, collector: Collector[String]): Unit = {
            stream2ListState.add(in2)

            import scala.collection.convert.ImplicitConversions._
            for(in1 <- stream1ListState.get()){
                collector.collect(in1 + "=>" + in2)
            }
        }
    }

}
