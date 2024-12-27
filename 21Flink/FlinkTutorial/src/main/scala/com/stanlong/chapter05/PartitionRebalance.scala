package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._

object PartitionRebalance {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        val stream = env.addSource(new ClickSource)

        // 经轮询重分区后打印输出，并行度为 4
        stream.rebalance.print("rebalance").setParallelism(4)

        env.execute()

        /** 可以看到子任务号是按照 4,1,2,3 轮询打印
         * rebalance:4> Event(Alice,./prod?id=1,1735223532936)
         * rebalance:1> Event(Alice,./fav,1735223533943)
         * rebalance:2> Event(Mary,./home,1735223534944)
         * rebalance:3> Event(Alice,./prod?id=2,1735223535944)
         * rebalance:4> Event(Mary,./prod?id=2,1735223536945)
         * rebalance:1> Event(Cary,./cart,1735223537945)
         * rebalance:2> Event(Mary,./prod?id=2,1735223538946)
         * rebalance:3> Event(Mary,./prod?id=1,1735223539946)
         * rebalance:4> Event(Bob,./prod?id=2,1735223540947)
         * rebalance:1> Event(Alice,./prod?id=2,1735223541948)
         * rebalance:2> Event(Alice,./cart,1735223542948)
         * rebalance:3> Event(Cary,./fav,1735223543949)
         * rebalance:4> Event(Alice,./home,1735223544949)
         */
    }
}