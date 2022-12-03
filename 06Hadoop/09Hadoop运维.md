# Hadoop运维

## 一、块损坏

```shell
命令格式
hadoop fsck 损坏的块所在目录

-- 检查损坏的块
hadoop fsck /pstm_tmp/P01_01_TH8_psdm_IL3000/

-- 删除损坏的块
hadoop fsck /pstm_tmp/P01_01_TH8_psdm_IL3000/ -delete
```

## 二、负载均衡

配置 blance_list， 文件中 中写入hdfs磁盘使用率标红的IP地址和对应数目的磁盘使用率较低的IP地址

配置好 blance_list 文件后， 在CPU使用率较低的机器上执行如下两条命令

```
# hdfs dfsadmin -setBalancerBandwidth 52428800  # 设置宽带 50 M
# hdfs balancer -policy datanode -threshold 5 -include -f blance_list
```

### 补充知识

1. hdfs dfsadmin -setBalancerBandwidth newbandwidth ： 设置平衡数据传输带宽

-   newbandwidth 表示可以使用的最大网络带宽，以每秒字节数为单位。

  命令介绍：

  Hadoop的HDFS集群中DataNode之间很可能会出现分布不均匀的情况，比如新增了集群节点、删除节点等。当数据不均匀时，处理数据时会集中在某一些节点上，可能导致相关node节点的网络带宽消耗到瓶颈，而新增或数据较少的节点处于空闲状况。在上面的情况下，可以通过HDFS的负载均衡器进行调整，使数据平均分布在各个Node节点上，均衡各项资源的性能，从而提升DataNode节点和集群的利用率。

  这个命令是datanode做balance或者mover时候一个比较核心的参数配置.

2. hdfs balancer -policy datanode -threshold 5 -include -f blance_list

-   -policy datanode 平衡策略：datanode或blockpool，默认：datanode，如果datanode均衡，则集群均衡
-   -threshold 5 : 表示每个datanode上的磁盘使用量与集群中的总使用量相差不超过5%
-   -include -f blance_list 仅包括指定的数据节点

参考地址： https://blog.csdn.net/moyefeng198436/article/details/113649445

## 三、没有active主机

手动执行如下命令指定active主机

```shell
hdfs haadmin -transitionToActive nn2
```