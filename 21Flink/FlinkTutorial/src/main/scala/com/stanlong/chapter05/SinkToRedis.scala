package com.stanlong.chapter05

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

object SinkToRedis {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val conf = new FlinkJedisPoolConfig.Builder().setHost("node01").build()
        env.addSource(new ClickSource).addSink(new RedisSink[Event](conf, new MyRedisMapper()))

        env.execute()
    }
}

class MyRedisMapper extends RedisMapper[Event] {
    override def getKeyFromData(t: Event): String = t.user

    override def getValueFromData(t: Event): String = t.url

    override def getCommandDescription: RedisCommandDescription = new RedisCommandDescription(RedisCommand.HSET, "clicks")
}
