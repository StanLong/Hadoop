# 用于监控HDFS的Zabbix模板

**说明**

- 支持监控HDFS、NameNode的运行数据。
- 在CentOS7，Zabbix5.0，Hadoop2.7.6下测试成功。

## 使用方法

在zabbix前端导入Template Cluster Hadoop.xml模板文件，链接该模板到Hadoop主机上，并根据实际情况在继承以及主机宏这里修改三个宏参数：

- {$HADOOP_NAMENODE_HOST} NameNode 修改为 hostname
- {$HADOOP_NAMENODE_METRICS_PORT} NameNode Web UI端口，默认为50070
- {$ZABBIX_NAME} Zabbix   前端创建当前主机时设置的主机名，也就是当前namenode主机名

然后上传剩余的两个脚本文件到Zabbix Server主机上存放Zabbix外部脚本的文件夹（默认为/usr/local/share/zabbix/externalscripts/ 或者 /usr/lib/zabbix/externalscripts ）中，并使用chmod +x赋予脚本文件可执行权限。

完成！

## 当前支持的监控项目

HDFS（24项）

- Blocks总数blocks
- ​	副本不足的Block个数under_replicated_blocks
- ​	文件及文件夹总数量files_and_directorys
- ​	集群总配置容量configured_capacity
- ​	DFS已使用的容量dfs_used
- ​	DFS已使用的容量占总配置容量的百分比dfs_used_persent
- ​	非DFS已使用的容量non_dfs_used
- ​	DFS可用的剩余容量dfs_remaining
- ​	DFS可用的剩余容量占总配置容量的百分比dfs_remaining_persent
- ​	单节点最大的可用容量max_remaining
- ​	单节点最大的可用容量百分比max_remaining_persent
- ​	单节点最小的可用容量min_remaining
- ​	单节点最小的可用容量百分比min_remaining_persent
- ​	具有最大可用容量的DataNode主机名max_remaining_nodename
- ​	具有最大的可用容量百分比的DataNode主机名max_remaining_persent_nodename
- ​	具有最小可用容量的DataNode主机名min_remaining_nodename
- ​	具有最小的可用容量百分比的DataNode主机名min_remaining_persent_nodename
- ​	各DataNode节点已使用空间百分比的中位数median_datanodes_usages
- ​	各DataNode节点已使用空间百分比的标准差stdDev_datanodes_usages
- ​	处于Dead状态的DataNode节点数dead_nodes
- ​	处于Live状态的DataNode节点数live_nodes
- ​	处于Decommissing状态的节点数decommissioning_nodes
- ​	最大的DataNode节点空间使用量(%)max_datanodes_usages
- ​	最小的DataNode节点空间使用量(%)min_datanodes_usages

NameNode（9项）

- ​	Hadoop版本hadoop_version
- ​	NameNode启动时间start_time
- ​	NameNode状态namenode_state
- ​	堆内存使用量heap_memory_used
- ​	非堆内存使用量non_heap_memory_used
- ​	总内存使用量all_memory_used
- ​	提交的堆内存大小heap_memory
- ​	提交的非堆内存大小commited_non_heap_memory
- ​	最大堆内存大小max_heap_memory

## 当前支持的触发器项目

- ​	Hadoop集群总可用存储空间已不足20%
- ​	主机{HOSTNAME}上的NameNode刚刚被重启过
- ​	副本不足的Block个数有增加
- ​	在过去的2分钟内未获取到主机[{HOSTNAME}]上NameNode的任何数据，请检查数据采集器的日志或者查看该主机NameNode运行状态
- ​	当前处于Active状态的NameNode主机为: {HOSTNAME}
- ​	有一个或多个节点变为Decommissioning状态
- ​	有一个或多个节点变为Live状态或者重启了
- ​	有一个或多个节点处于Dead状态
- ​	集群中出现了可用存储空间不足20%的节点
- ​	集群中处于Live状态的节点数量发生了变化

