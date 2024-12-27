package com.stanlong.chapter05

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.streaming.api.scala._

object TransformFilterTest {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        val stream1 = env.addSource(new ClickSource).filter(data => data.user.equals("Mary"))
        val stream2 = env.addSource(new ClickSource).filter(new UserFilter)
        stream1.print()
        stream2.print()
        env.execute()

    }

}

class UserFilter extends FilterFunction[Event]{

    override def filter(t: Event): Boolean = t.user.equals("Bob")
}
