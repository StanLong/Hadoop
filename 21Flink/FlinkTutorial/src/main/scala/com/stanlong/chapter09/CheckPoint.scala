package com.stanlong.chapter09

import com.stanlong.chapter05.{ClickSource, Event}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala._
import scala.collection.mutable.ListBuffer

object CheckPoint {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        val stream = env.addSource(new ClickSource)
          .assignAscendingTimestamps(_.timestamp)
          .addSink(new BufferingSink(10))

        env.execute()
    }

    private class BufferingSink (threshold : Int) extends SinkFunction[Event]
      with CheckpointedFunction{

        private var checkpointState : ListState[Event] = _
        private var bufferedElements = ListBuffer[Event]()

        override def invoke(value: Event, context: SinkFunction.Context): Unit = {
            bufferedElements += value

            if(bufferedElements.size == threshold){
                bufferedElements.foreach(data => println(data))
                println("=========== 输出完毕 ===========")
                bufferedElements.clear()
            }
        }

        override def snapshotState(functionSnapshotContext: FunctionSnapshotContext): Unit = {
            // checkpointState.clear()
            for(element <- bufferedElements){
                checkpointState.add(element)
            }
        }

        override def initializeState(functionInitializationContext: FunctionInitializationContext): Unit = {

            checkpointState = functionInitializationContext.getOperatorStateStore
              .getListState(new ListStateDescriptor[Event]("buffered-elements", classOf[Event]))

            if(functionInitializationContext.isRestored){
                import scala.collection.convert.ImplicitConversions._
                for(element <- checkpointState.get()){
                    bufferedElements += element
                }
            }
        }
    }

}
