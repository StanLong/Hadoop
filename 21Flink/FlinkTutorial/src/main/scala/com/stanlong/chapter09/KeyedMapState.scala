package com.stanlong.chapter09

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object KeyedMapState {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(_.timestamp)
          .keyBy(_.url)
          .process( new FakeWindow(10000L))
          .print()
        env.execute()
    }

    private class FakeWindow(size : Long) extends KeyedProcessFunction[String, Event, String] {

        private lazy val windowPvMapState: MapState[Long, Long] = getRuntimeContext.getMapState(
            new MapStateDescriptor[Long, Long]("window-pv", classOf[Long], classOf[Long])
        )

        override def processElement(i: Event, context: KeyedProcessFunction[String, Event, String]#Context, collector: Collector[String]): Unit = {
            val start = i.timestamp / size * size // 时间戳取整
            val end = start + size

            context.timerService().registerEventTimeTimer(end -1)

            if(windowPvMapState.contains(start)){
                val pv = windowPvMapState.get(start)
                windowPvMapState.put(start, pv + 1)
            } else {
                windowPvMapState.put(start, 1L)
            }
        }

        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, Event, String]#OnTimerContext, out: Collector[String]): Unit = {
            val start = timestamp + 1 - size
            val pv = windowPvMapState.get(start)
            out.collect(s"url:${ctx.getCurrentKey} 的浏览量为: ${pv}  窗口为: ${start} - ${start+size}")

            // 销毁窗口
            windowPvMapState.remove(start)
        }
    }

}
