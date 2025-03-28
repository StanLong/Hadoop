package com.stanlong.chapter05

import java.util.Calendar
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.watermark.Watermark
import scala.util.Random


class ClickSource extends SourceFunction[Event] {
    // 标志位，用来控制循环的退出
    private var running = true

    //重写 run 方法，使用上下文对象 sourceContext 调用 collect 方法
    override def run(ctx: SourceContext[Event]): Unit = {
        // 实例化一个随机数发生器
        val random = new Random
        // 供随机选择的用户名的数组
        val users = Array("Mary", "Bob", "Alice", "Cary")
        // 供随机选择的 url 的数组
        val urls = Array("./home", "./cart", "./fav", "./prod?id=1", "./prod?id=2")
        //通过 while 循环发送数据，running 默认为 true，所以会一直发送数据
        while (running) {

            val event = Event(
                users(random.nextInt(users.length)), // 随机选择一个用户名
                urls(random.nextInt(urls.length)),    // 随机选择一个 url
                Calendar.getInstance.getTimeInMillis // 当前时间戳
            )

            // 为要发送的数据分配时间戳
            // ctx.collectWithTimestamp(event, event.timestamp)

            // 向下游直接发送水位线
            // ctx.emitWatermark(new Watermark(event.timestamp -1L))

            // 调用 collect 方法向下游发送数据
            ctx.collect(event)

            // 隔 1 秒生成一个点击事件，方便观测
            Thread.sleep(1000)
        }
    }
    //通过将 running 置为 false 终止数据发送循环
    override def cancel(): Unit = running = false
}

case class Event(user: String, url: String, timestamp: Long)