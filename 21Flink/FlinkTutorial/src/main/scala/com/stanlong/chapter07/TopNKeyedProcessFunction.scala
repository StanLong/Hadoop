package com.stanlong.chapter07

import com.stanlong.chapter05.ClickSource
import com.stanlong.chapter06.UrlView
import com.stanlong.chapter06.UrlViewCount.{UrlViewCountAgg, UrlViewCountResult}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

object TopNKeyedProcessFunction {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(_.timestamp)

        val urlCountStream = stream.keyBy(_.url)
          .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
          .aggregate(new UrlViewCountAgg, new UrlViewCountResult)

        val resultStream = urlCountStream.keyBy(_.windowEnd)
          .process(new TopN(2))

        resultStream.print()
        env.execute()
    }

    private class TopN(n : Int) extends KeyedProcessFunction[Long, UrlView, String]{

        private var urlViewCountListState: ListState[UrlView] = _

        override def open(parameters: Configuration): Unit = {
            urlViewCountListState = getRuntimeContext.getListState(new ListStateDescriptor[UrlView]("list-state", classOf[UrlView]))
        }

        override def processElement(i: UrlView, context: KeyedProcessFunction[Long, UrlView, String]#Context, collector: Collector[String]): Unit = {
            urlViewCountListState.add(i)
            context.timerService().registerEventTimeTimer(i.windowEnd + 1)
        }

        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, UrlView, String]#OnTimerContext, out: Collector[String]): Unit = {
            val urlViewCountList = urlViewCountListState.get().toList
            val topNList = urlViewCountList.sortBy(-_.count).take(n)
            val result = new StringBuilder()
            result.append(s"=======================窗口：${timestamp - 1 - 10000} - ${timestamp - 1}=======================\n")
            for( i <- topNList.indices){
                val urlViewCount = topNList(i)
                result.append(s"浏览量Top ${i+1}, ")
                  .append(s"url ${urlViewCount.url}, ")
                  .append(s"浏览次数: ${urlViewCount.count} \n")
            }
            out.collect(result.toString())
        }
    }
}
