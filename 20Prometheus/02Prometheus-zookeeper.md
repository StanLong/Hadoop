# Prometheus 监控Zookeeper

### 监控ZooKeeper指标

#### 修改zkServer.sh

vim /opt/apache-zookeeper-3.6.1-bin/conf/zoo.cfg

```properties
## Metrics Providers
#
# https://prometheus.io Metrics Exporter
metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider
metricsProvider.httpPort=7000
metricsProvider.exportJvmInfo=true
```

#### 将ZK分发到每个节点

```shell
scp /opt/apache-zookeeper-3.6.1-bin/conf/zoo.cfg ha-node2:/opt/apache-zookeeper-3.6.1-bin/conf; \
scp /opt/apache-zookeeper-3.6.1-bin/conf/zoo.cfg ha-node3:/opt/apache-zookeeper-3.6.1-bin/conf; \
scp /opt/apache-zookeeper-3.6.1-bin/conf/zoo.cfg ha-node4:/opt/apache-zookeeper-3.6.1-bin/conf; \
scp /opt/apache-zookeeper-3.6.1-bin/conf/zoo.cfg ha-node5:/opt/apache-zookeeper-3.6.1-bin/conf
```

#### 启动ZooKeeper集群

#### 测试获取ZooKeeper metrics

```shell
curl ha-node1:7000/metrics
```

马上就能看到大量的指标了。

#### Prometheus开启监控

编辑prometheus.yaml文件：

```shell
  - job_name: 'zk_cluster'
    static_configs:
    - targets: ['ha-node1:9505','ha-node2:9505','ha-node3:9505','ha-node4:9505','ha-node5:9505']
```

启动prometheus：

```shell
./prometheus --config.file=prometheus.yml
```