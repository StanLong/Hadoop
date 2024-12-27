package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._

object PartitionShuffle {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)

        // shuffle 一条数据过来随机分配到4个分区上
        stream.shuffle.print("shuffle随机分区").setParallelism(4)
        env.execute()

        /**
         * shuffle随机分区:1> Event(zhangsan,fav,1735222734686)
         * shuffle随机分区:3> Event(lisi,/prod?id=3,1735222735701)
         * shuffle随机分区:2> Event(zhangsan,/cart,1735222736709)
         * shuffle随机分区:1> Event(zhangsan,/prod?id=3,1735222737713)
         * shuffle随机分区:2> Event(wangwu,fav,1735222738720)
         * shuffle随机分区:4> Event(zhangsan,fav,1735222739729)
         * shuffle随机分区:2> Event(lisi,/prod?id=3,1735222740740)
         * shuffle随机分区:1> Event(lisi,/prod?id=3,1735222741752)
         * shuffle随机分区:2> Event(lisi,/portal,1735222742758)
         * shuffle随机分区:4> Event(wangwu,/cart,1735222743760)
         * shuffle随机分区:2> Event(wangwu,/prod?id=3,1735222744761)
         * shuffle随机分区:3> Event(zhangsan,/prod?id=3,1735222745771)
         * shuffle随机分区:1> Event(lisi,/portal,1735222746772)
         */
    }
}
