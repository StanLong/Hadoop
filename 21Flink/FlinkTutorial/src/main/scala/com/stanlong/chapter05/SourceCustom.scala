package com.stanlong.chapter05

// 导入隐式类型转换，必须导入！
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import org.apache.flink.streaming.api.scala._
import scala.tools.nsc.doc.html.page.JSONObject

object SourceCustom {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        // 使用自定义的数据源
        val stream = env.addSource(new ClickSource)
        stream.print()

        env.execute()
    }
}
