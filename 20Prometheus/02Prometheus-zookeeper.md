# Prometheus 监控 Zookeeper

### 监控ZooKeeper指标

#### 修改zkServer.sh

vim /opt/zookeeper-3.8.4/conf/zoo.cfg

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
[root@node02 conf]# scp zoo.cfg node03:/opt/zookeeper-3.8.4/conf/
[root@node02 conf]# scp zoo.cfg node04:/opt/zookeeper-3.8.4/conf/
```

#### 启动ZooKeeper集群

#### 测试获取ZooKeeper metrics

```shell
curl node01:7000/metrics
```

马上就能看到大量的指标了。

#### Prometheus开启监控

编辑prometheus.yaml文件：

```shell
  # 监控zookeeper
  - job_name: 'zookeeper'
    static_configs:
    - targets: ["node02:7000","node03:7000","node04:7000"]
```

启动prometheus：

```shell
 nohup ./prometheus --config.file=prometheus.yml > /var/log/prometheus/prometheus.log 2>&1 & 
```