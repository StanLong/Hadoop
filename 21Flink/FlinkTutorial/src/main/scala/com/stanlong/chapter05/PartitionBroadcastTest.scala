package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._

object PartitionBroadcastTest {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)

        // 经广播后打印输出，并行度为 4
        stream.broadcast.print("broadcast").setParallelism(4)

        stream.global.print("gloabl").setParallelism(4)

        env.execute()


        /**
         * 观察输出可知 broadcast 分区是4个并行子任务同时打印，且输出内容相同
         * gloabl 分区的子任务都集中在 1 号子任务上。
         * broadcast:2> Event(Cary,./home,1735225231109)
         * broadcast:4> Event(Cary,./home,1735225231109)
         * gloabl:1> Event(Cary,./home,1735225231109)
         * broadcast:1> Event(Cary,./home,1735225231109)
         * broadcast:3> Event(Cary,./home,1735225231109)
         * broadcast:4> Event(Alice,./cart,1735225232120)
         * broadcast:1> Event(Alice,./cart,1735225232120)
         * broadcast:3> Event(Alice,./cart,1735225232120)
         * broadcast:2> Event(Alice,./cart,1735225232120)
         * gloabl:1> Event(Alice,./cart,1735225232120)
         * broadcast:2> Event(Bob,./cart,1735225233120)
         * broadcast:4> Event(Bob,./cart,1735225233120)
         * broadcast:3> Event(Bob,./cart,1735225233120)
         * broadcast:1> Event(Bob,./cart,1735225233120)
         * gloabl:1> Event(Bob,./cart,1735225233120)
         * broadcast:4> Event(Alice,./prod?id=1,1735225234121)
         * broadcast:1> Event(Alice,./prod?id=1,1735225234121)
         * broadcast:3> Event(Alice,./prod?id=1,1735225234121)
         * broadcast:2> Event(Alice,./prod?id=1,1735225234121)
         * gloabl:1> Event(Alice,./prod?id=1,1735225234121)
         * broadcast:4> Event(Alice,./prod?id=1,1735225235121)
         * broadcast:2> Event(Alice,./prod?id=1,1735225235121)
         * broadcast:3> Event(Alice,./prod?id=1,1735225235121)
         * broadcast:1> Event(Alice,./prod?id=1,1735225235121)
         * gloabl:1> Event(Alice,./prod?id=1,1735225235121)
         * broadcast:3> Event(Mary,./prod?id=2,1735225236122)
         * broadcast:2> Event(Mary,./prod?id=2,1735225236122)
         * broadcast:4> Event(Mary,./prod?id=2,1735225236122)
         * broadcast:1> Event(Mary,./prod?id=2,1735225236122)
         * gloabl:1> Event(Mary,./prod?id=2,1735225236122)
         */

    }
}