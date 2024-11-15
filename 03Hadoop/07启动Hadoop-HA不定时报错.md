## 启动报错日志

recoverUnfinalizedSegments failed for required journal

```
2021-07-03 06:30:32,418 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: STARTUP_MSG: 
/************************************************************
STARTUP_MSG: Starting NameNode
STARTUP_MSG:   host = node01/192.168.235.11
STARTUP_MSG:   args = []
STARTUP_MSG:   version = 2.9.2
STARTUP_MSG:   classpath = /opt/stanlong/hadoop-ha/hadoop-2.9.2/etc/hadoop:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jaxb-impl-2.2.3-1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/slf4j-log4j12-1.7.25.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/activation-1.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/woodstox-core-5.0.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-configuration-1.6.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-beanutils-1.7.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/xz-1.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/htrace-core4-4.1.0-incubating.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/junit-4.11.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/snappy-java-1.0.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/stax-api-1.0-2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/apacheds-i18n-2.0.0-M15.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jaxb-api-2.2.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/mockito-all-1.8.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/slf4j-api-1.7.25.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jackson-jaxrs-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-logging-1.1.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/avro-1.7.7.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/curator-recipes-2.7.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jetty-sslengine-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jersey-json-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/log4j-1.2.17.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-cli-1.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-digester-1.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/servlet-api-2.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/protobuf-java-2.5.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jcip-annotations-1.0-1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/xmlenc-0.52.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jackson-xc-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jetty-util-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/httpclient-4.5.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/guava-11.0.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-compress-1.4.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-io-2.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jackson-core-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/stax2-api-3.1.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jersey-core-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jsp-api-2.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-codec-1.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/netty-3.6.2.Final.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jetty-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-beanutils-core-1.8.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jersey-server-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/java-xmlbuilder-0.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/paranamer-2.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/zookeeper-3.4.6.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-collections-3.2.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jettison-1.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/asm-3.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/api-asn1-api-1.0.0-M20.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/apacheds-kerberos-codec-2.0.0-M15.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/hamcrest-core-1.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jsch-0.1.54.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/httpcore-4.4.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/json-smart-1.3.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/hadoop-annotations-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/api-util-1.0.0-M20.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/curator-framework-2.7.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-net-3.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/gson-2.2.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jets3t-0.9.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-lang-2.6.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jackson-mapper-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-math3-3.1.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/commons-lang3-3.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/jsr305-3.0.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/nimbus-jose-jwt-4.41.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/hadoop-auth-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/lib/curator-client-2.7.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/hadoop-common-2.9.2-tests.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/hadoop-common-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/hadoop-nfs-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/common/hadoop-lzo-0.4.21.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/htrace-core4-4.1.0-incubating.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/xercesImpl-2.9.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/commons-logging-1.1.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jackson-annotations-2.7.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/log4j-1.2.17.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/commons-cli-1.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/xml-apis-1.3.04.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/servlet-api-2.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/protobuf-java-2.5.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/xmlenc-0.52.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jetty-util-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/guava-11.0.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/okio-1.6.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/commons-io-2.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jackson-core-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jersey-core-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/okhttp-2.7.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/commons-codec-1.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/netty-3.6.2.Final.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jetty-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jersey-server-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/asm-3.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/hadoop-hdfs-client-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/commons-lang-2.6.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/leveldbjni-all-1.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jackson-databind-2.7.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jackson-core-2.7.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jackson-mapper-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/netty-all-4.0.23.Final.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/commons-daemon-1.0.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/lib/jsr305-3.0.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-2.9.2-tests.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-nfs-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-rbf-2.9.2-tests.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-rbf-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-native-client-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-client-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-native-client-2.9.2-tests.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/hdfs/hadoop-hdfs-client-2.9.2-tests.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jaxb-impl-2.2.3-1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/activation-1.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/woodstox-core-5.0.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/aopalliance-1.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-configuration-1.6.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-beanutils-1.7.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/guice-servlet-3.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/xz-1.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/htrace-core4-4.1.0-incubating.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/snappy-java-1.0.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/stax-api-1.0-2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/apacheds-i18n-2.0.0-M15.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/java-util-1.9.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jaxb-api-2.2.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jackson-jaxrs-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-logging-1.1.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/avro-1.7.7.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/curator-recipes-2.7.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jetty-sslengine-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/geronimo-jcache_1.0_spec-1.0-alpha-1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jersey-json-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/log4j-1.2.17.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-cli-1.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-digester-1.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/servlet-api-2.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/protobuf-java-2.5.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jcip-annotations-1.0-1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/xmlenc-0.52.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jackson-xc-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jetty-util-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/fst-2.50.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/httpclient-4.5.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/guava-11.0.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/mssql-jdbc-6.2.1.jre7.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-compress-1.4.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-io-2.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jackson-core-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/stax2-api-3.1.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jersey-core-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jsp-api-2.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-codec-1.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/netty-3.6.2.Final.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jetty-6.1.26.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-beanutils-core-1.8.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jersey-server-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/guice-3.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/java-xmlbuilder-0.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/metrics-core-3.0.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jersey-client-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jersey-guice-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/paranamer-2.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/zookeeper-3.4.6.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-collections-3.2.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jettison-1.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/asm-3.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/api-asn1-api-1.0.0-M20.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/apacheds-kerberos-codec-2.0.0-M15.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jsch-0.1.54.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/httpcore-4.4.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/ehcache-3.3.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/json-smart-1.3.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/api-util-1.0.0-M20.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/curator-framework-2.7.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-net-3.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/gson-2.2.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jets3t-0.9.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-lang-2.6.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/HikariCP-java7-2.4.12.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/json-io-2.5.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/leveldbjni-all-1.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jackson-mapper-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-math3-3.1.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/commons-lang3-3.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/javax.inject-1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/jsr305-3.0.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/nimbus-jose-jwt-4.41.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/lib/curator-client-2.7.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-timeline-pluginstorage-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-applications-distributedshell-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-registry-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-sharedcachemanager-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-applications-unmanaged-am-launcher-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-applicationhistoryservice-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-tests-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-api-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-common-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-web-proxy-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-nodemanager-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-resourcemanager-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-common-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-server-router-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/yarn/hadoop-yarn-client-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/aopalliance-1.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/guice-servlet-3.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/xz-1.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/junit-4.11.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/snappy-java-1.0.5.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/avro-1.7.7.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/log4j-1.2.17.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/protobuf-java-2.5.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/commons-compress-1.4.1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/commons-io-2.4.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/jackson-core-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/jersey-core-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/netty-3.6.2.Final.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/jersey-server-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/guice-3.0.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/jersey-guice-1.9.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/paranamer-2.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/asm-3.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/hamcrest-core-1.3.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/hadoop-annotations-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/leveldbjni-all-1.8.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/jackson-mapper-asl-1.9.13.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/lib/javax.inject-1.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-app-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-hs-plugins-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-hs-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-shuffle-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-2.9.2-tests.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-2.9.2.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/contrib/capacity-scheduler/*.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/contrib/capacity-scheduler/*.jar:/opt/stanlong/hadoop-ha/hadoop-2.9.2/contrib/capacity-scheduler/*.jar
STARTUP_MSG:   build = https://git-wip-us.apache.org/repos/asf/hadoop.git -r 826afbeae31ca687bc2f8471dc841b66ed2c6704; compiled by 'ajisaka' on 2018-11-13T12:42Z
STARTUP_MSG:   java = 1.8.0_221
************************************************************/
2021-07-03 06:30:32,443 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: registered UNIX signal handlers for [TERM, HUP, INT]
2021-07-03 06:30:32,716 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: createNameNode []
2021-07-03 06:30:32,966 INFO org.apache.hadoop.metrics2.impl.MetricsConfig: loaded properties from hadoop-metrics2.properties
2021-07-03 06:30:33,252 INFO org.apache.hadoop.metrics2.impl.MetricsSystemImpl: Scheduled Metric snapshot period at 10 second(s).
2021-07-03 06:30:33,252 INFO org.apache.hadoop.metrics2.impl.MetricsSystemImpl: NameNode metrics system started
2021-07-03 06:30:33,328 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: fs.defaultFS is hdfs://hacluster
2021-07-03 06:30:33,332 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: Clients are to use hacluster to access this namenode/service.
2021-07-03 06:30:33,778 INFO org.apache.hadoop.util.JvmPauseMonitor: Starting JVM pause monitor
2021-07-03 06:30:33,783 INFO org.apache.hadoop.hdfs.DFSUtil: Starting Web-server for hdfs at: http://node01:50070
2021-07-03 06:30:33,892 INFO org.mortbay.log: Logging to org.slf4j.impl.Log4jLoggerAdapter(org.mortbay.log) via org.mortbay.log.Slf4jLog
2021-07-03 06:30:33,914 INFO org.apache.hadoop.security.authentication.server.AuthenticationFilter: Unable to initialize FileSignerSecretProvider, falling back to use random secrets.
2021-07-03 06:30:33,960 INFO org.apache.hadoop.http.HttpRequestLog: Http request log for http.requests.namenode is not defined
2021-07-03 06:30:33,973 INFO org.apache.hadoop.http.HttpServer2: Added global filter 'safety' (class=org.apache.hadoop.http.HttpServer2$QuotingInputFilter)
2021-07-03 06:30:33,977 INFO org.apache.hadoop.http.HttpServer2: Added filter static_user_filter (class=org.apache.hadoop.http.lib.StaticUserWebFilter$StaticUserFilter) to context hdfs
2021-07-03 06:30:33,978 INFO org.apache.hadoop.http.HttpServer2: Added filter static_user_filter (class=org.apache.hadoop.http.lib.StaticUserWebFilter$StaticUserFilter) to context logs
2021-07-03 06:30:33,978 INFO org.apache.hadoop.http.HttpServer2: Added filter static_user_filter (class=org.apache.hadoop.http.lib.StaticUserWebFilter$StaticUserFilter) to context static
2021-07-03 06:30:34,249 INFO org.apache.hadoop.http.HttpServer2: Added filter 'org.apache.hadoop.hdfs.web.AuthFilter' (class=org.apache.hadoop.hdfs.web.AuthFilter)
2021-07-03 06:30:34,250 INFO org.apache.hadoop.http.HttpServer2: addJerseyResourcePackage: packageName=org.apache.hadoop.hdfs.server.namenode.web.resources;org.apache.hadoop.hdfs.web.resources, pathSpec=/webhdfs/v1/*
2021-07-03 06:30:34,275 INFO org.apache.hadoop.http.HttpServer2: Jetty bound to port 50070
2021-07-03 06:30:34,275 INFO org.mortbay.log: jetty-6.1.26
2021-07-03 06:30:34,722 INFO org.mortbay.log: Started HttpServer2$SelectChannelConnectorWithSafeStartup@node01:50070
2021-07-03 06:30:34,752 WARN org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Only one image storage directory (dfs.namenode.name.dir) configured. Beware of data loss due to lack of redundant storage directories!
2021-07-03 06:30:34,921 INFO org.apache.hadoop.hdfs.server.namenode.FSEditLog: Edit logging is async:true
2021-07-03 06:30:34,934 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: KeyProvider: null
2021-07-03 06:30:34,936 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: fsLock is fair: true
2021-07-03 06:30:34,936 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Detailed lock hold time metrics enabled: false
2021-07-03 06:30:34,944 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: fsOwner             = root (auth:SIMPLE)
2021-07-03 06:30:34,944 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: supergroup          = supergroup
2021-07-03 06:30:34,944 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: isPermissionEnabled = true
2021-07-03 06:30:34,945 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Determined nameservice ID: hacluster
2021-07-03 06:30:34,945 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: HA Enabled: true
2021-07-03 06:30:34,991 INFO org.apache.hadoop.hdfs.server.common.Util: dfs.datanode.fileio.profiling.sampling.percentage set to 0. Disabling file IO profiling
2021-07-03 06:30:35,042 INFO org.apache.hadoop.hdfs.server.blockmanagement.DatanodeManager: dfs.block.invalidate.limit: configured=1000, counted=60, effected=1000
2021-07-03 06:30:35,042 INFO org.apache.hadoop.hdfs.server.blockmanagement.DatanodeManager: dfs.namenode.datanode.registration.ip-hostname-check=true
2021-07-03 06:30:35,045 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: dfs.namenode.startup.delay.block.deletion.sec is set to 000:00:00:00.000
2021-07-03 06:30:35,046 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: The block deletion will start around 2021 Jul 03 06:30:35
2021-07-03 06:30:35,059 INFO org.apache.hadoop.util.GSet: Computing capacity for map BlocksMap
2021-07-03 06:30:35,059 INFO org.apache.hadoop.util.GSet: VM type       = 64-bit
2021-07-03 06:30:35,061 INFO org.apache.hadoop.util.GSet: 2.0% max memory 966.7 MB = 19.3 MB
2021-07-03 06:30:35,061 INFO org.apache.hadoop.util.GSet: capacity      = 2^21 = 2097152 entries
2021-07-03 06:30:35,104 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: dfs.block.access.token.enable=false
2021-07-03 06:30:35,105 WARN org.apache.hadoop.conf.Configuration: No unit for dfs.heartbeat.interval(3) assuming SECONDS
2021-07-03 06:30:35,110 WARN org.apache.hadoop.conf.Configuration: No unit for dfs.namenode.safemode.extension(30000) assuming MILLISECONDS
2021-07-03 06:30:35,110 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManagerSafeMode: dfs.namenode.safemode.threshold-pct = 0.9990000128746033
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManagerSafeMode: dfs.namenode.safemode.min.datanodes = 0
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManagerSafeMode: dfs.namenode.safemode.extension = 30000
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: defaultReplication         = 2
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: maxReplication             = 512
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: minReplication             = 1
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: maxReplicationStreams      = 2
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: replicationRecheckInterval = 3000
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: encryptDataTransfer        = false
2021-07-03 06:30:35,111 INFO org.apache.hadoop.hdfs.server.blockmanagement.BlockManager: maxNumBlocksToLog          = 1000
2021-07-03 06:30:35,125 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Append Enabled: true
2021-07-03 06:30:35,901 INFO org.apache.hadoop.hdfs.server.namenode.FSDirectory: GLOBAL serial map: bits=24 maxEntries=16777215
2021-07-03 06:30:35,946 INFO org.apache.hadoop.util.GSet: Computing capacity for map INodeMap
2021-07-03 06:30:35,946 INFO org.apache.hadoop.util.GSet: VM type       = 64-bit
2021-07-03 06:30:35,947 INFO org.apache.hadoop.util.GSet: 1.0% max memory 966.7 MB = 9.7 MB
2021-07-03 06:30:35,947 INFO org.apache.hadoop.util.GSet: capacity      = 2^20 = 1048576 entries
2021-07-03 06:30:35,954 INFO org.apache.hadoop.hdfs.server.namenode.FSDirectory: ACLs enabled? false
2021-07-03 06:30:35,954 INFO org.apache.hadoop.hdfs.server.namenode.FSDirectory: XAttrs enabled? true
2021-07-03 06:30:35,954 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: Caching file names occurring more than 10 times
2021-07-03 06:30:35,959 INFO org.apache.hadoop.hdfs.server.namenode.snapshot.SnapshotManager: Loaded config captureOpenFiles: falseskipCaptureAccessTimeOnlyChange: false
2021-07-03 06:30:35,979 INFO org.apache.hadoop.util.GSet: Computing capacity for map cachedBlocks
2021-07-03 06:30:35,979 INFO org.apache.hadoop.util.GSet: VM type       = 64-bit
2021-07-03 06:30:35,979 INFO org.apache.hadoop.util.GSet: 0.25% max memory 966.7 MB = 2.4 MB
2021-07-03 06:30:35,979 INFO org.apache.hadoop.util.GSet: capacity      = 2^18 = 262144 entries
2021-07-03 06:30:35,984 INFO org.apache.hadoop.hdfs.server.namenode.top.metrics.TopMetrics: NNTop conf: dfs.namenode.top.window.num.buckets = 10
2021-07-03 06:30:35,984 INFO org.apache.hadoop.hdfs.server.namenode.top.metrics.TopMetrics: NNTop conf: dfs.namenode.top.num.users = 10
2021-07-03 06:30:35,984 INFO org.apache.hadoop.hdfs.server.namenode.top.metrics.TopMetrics: NNTop conf: dfs.namenode.top.windows.minutes = 1,5,25
2021-07-03 06:30:36,001 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Retry cache on namenode is enabled
2021-07-03 06:30:36,001 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Retry cache will use 0.03 of total heap and retry cache entry expiry time is 600000 millis
2021-07-03 06:30:36,004 INFO org.apache.hadoop.util.GSet: Computing capacity for map NameNodeRetryCache
2021-07-03 06:30:36,004 INFO org.apache.hadoop.util.GSet: VM type       = 64-bit
2021-07-03 06:30:36,004 INFO org.apache.hadoop.util.GSet: 0.029999999329447746% max memory 966.7 MB = 297.0 KB
2021-07-03 06:30:36,004 INFO org.apache.hadoop.util.GSet: capacity      = 2^15 = 32768 entries
2021-07-03 06:30:36,057 INFO org.apache.hadoop.hdfs.server.common.Storage: Lock on /var/data/hadoop/ha/data/dfs/name/in_use.lock acquired by nodename 11173@node01
2021-07-03 06:30:37,846 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:37,857 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:37,858 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:38,851 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:38,859 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:38,860 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:39,852 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:39,861 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:39,861 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:40,866 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:40,867 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:40,867 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:41,877 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:41,877 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:41,878 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:42,599 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 6006 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:42,882 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:42,897 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:42,897 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:43,607 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 7014 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:43,898 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:43,919 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:43,919 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:44,612 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 8019 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:44,920 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:44,921 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:44,931 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:45,613 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 9020 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:45,922 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:45,922 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:45,933 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:46,615 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 10022 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:46,924 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:46,925 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:46,935 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:46,937 WARN org.apache.hadoop.hdfs.server.namenode.FSEditLog: Unable to determine input streams from QJM to [192.168.235.12:8485, 192.168.235.13:8485, 192.168.235.14:8485]. Skipping.
org.apache.hadoop.hdfs.qjournal.client.QuorumException: Got too many exceptions to achieve quorum size 2/3. 3 exceptions thrown:
192.168.235.14:8485: Call From node01/192.168.235.11 to node04:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
192.168.235.13:8485: Call From node01/192.168.235.11 to node03:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
192.168.235.12:8485: Call From node01/192.168.235.11 to node02:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
	at org.apache.hadoop.hdfs.qjournal.client.QuorumException.create(QuorumException.java:81)
	at org.apache.hadoop.hdfs.qjournal.client.QuorumCall.rethrowException(QuorumCall.java:286)
	at org.apache.hadoop.hdfs.qjournal.client.AsyncLoggerSet.waitForWriteQuorum(AsyncLoggerSet.java:142)
	at org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager.selectInputStreams(QuorumJournalManager.java:473)
	at org.apache.hadoop.hdfs.server.namenode.JournalSet.selectInputStreams(JournalSet.java:278)
	at org.apache.hadoop.hdfs.server.namenode.FSEditLog.selectInputStreams(FSEditLog.java:1590)
	at org.apache.hadoop.hdfs.server.namenode.FSEditLog.selectInputStreams(FSEditLog.java:1614)
	at org.apache.hadoop.hdfs.server.namenode.FSImage.loadFSImage(FSImage.java:700)
	at org.apache.hadoop.hdfs.server.namenode.FSImage.recoverTransitionRead(FSImage.java:322)
	at org.apache.hadoop.hdfs.server.namenode.FSNamesystem.loadFSImage(FSNamesystem.java:1052)
	at org.apache.hadoop.hdfs.server.namenode.FSNamesystem.loadFromDisk(FSNamesystem.java:681)
	at org.apache.hadoop.hdfs.server.namenode.NameNode.loadNamesystem(NameNode.java:666)
	at org.apache.hadoop.hdfs.server.namenode.NameNode.initialize(NameNode.java:728)
	at org.apache.hadoop.hdfs.server.namenode.NameNode.<init>(NameNode.java:953)
	at org.apache.hadoop.hdfs.server.namenode.NameNode.<init>(NameNode.java:932)
	at org.apache.hadoop.hdfs.server.namenode.NameNode.createNameNode(NameNode.java:1673)
	at org.apache.hadoop.hdfs.server.namenode.NameNode.main(NameNode.java:1741)
2021-07-03 06:30:46,940 INFO org.apache.hadoop.hdfs.server.namenode.FSImage: No edit log streams selected.
2021-07-03 06:30:46,940 INFO org.apache.hadoop.hdfs.server.namenode.FSImage: Planning to load image: FSImageFile(file=/var/data/hadoop/ha/data/dfs/name/current/fsimage_0000000000000023750, cpktTxId=0000000000000023750)
2021-07-03 06:30:47,035 INFO org.apache.hadoop.hdfs.server.namenode.FSImageFormatPBINode: Loading 508 INodes.
2021-07-03 06:30:47,261 INFO org.apache.hadoop.hdfs.server.namenode.FSImageFormatProtobuf: Loaded FSImage in 0 seconds.
2021-07-03 06:30:47,261 INFO org.apache.hadoop.hdfs.server.namenode.FSImage: Loaded image for txid 23750 from /var/data/hadoop/ha/data/dfs/name/current/fsimage_0000000000000023750
2021-07-03 06:30:47,267 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Need to save fs image? false (staleImage=true, haEnabled=true, isRollingUpgrade=false)
2021-07-03 06:30:47,267 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: FSNamesystem write lock held for 11259 ms via
java.lang.Thread.getStackTrace(Thread.java:1559)
org.apache.hadoop.util.StringUtils.getStackTrace(StringUtils.java:1021)
org.apache.hadoop.hdfs.server.namenode.FSNamesystemLock.writeUnlock(FSNamesystemLock.java:261)
org.apache.hadoop.hdfs.server.namenode.FSNamesystem.writeUnlock(FSNamesystem.java:1569)
org.apache.hadoop.hdfs.server.namenode.FSNamesystem.loadFSImage(FSNamesystem.java:1081)
org.apache.hadoop.hdfs.server.namenode.FSNamesystem.loadFromDisk(FSNamesystem.java:681)
org.apache.hadoop.hdfs.server.namenode.NameNode.loadNamesystem(NameNode.java:666)
org.apache.hadoop.hdfs.server.namenode.NameNode.initialize(NameNode.java:728)
org.apache.hadoop.hdfs.server.namenode.NameNode.<init>(NameNode.java:953)
org.apache.hadoop.hdfs.server.namenode.NameNode.<init>(NameNode.java:932)
org.apache.hadoop.hdfs.server.namenode.NameNode.createNameNode(NameNode.java:1673)
org.apache.hadoop.hdfs.server.namenode.NameNode.main(NameNode.java:1741)
	Number of suppressed write-lock reports: 0
	Longest write-lock held interval: 11259
2021-07-03 06:30:47,267 INFO org.apache.hadoop.hdfs.server.namenode.NameCache: initialized with 5 entries 86 lookups
2021-07-03 06:30:47,267 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Finished loading FSImage in 11259 msecs
2021-07-03 06:30:47,641 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: RPC server is binding to node01:8020
2021-07-03 06:30:47,670 INFO org.apache.hadoop.ipc.CallQueueManager: Using callQueue: class java.util.concurrent.LinkedBlockingQueue queueCapacity: 1000 scheduler: class org.apache.hadoop.ipc.DefaultRpcScheduler
2021-07-03 06:30:47,711 INFO org.apache.hadoop.ipc.Server: Starting Socket Reader #1 for port 8020
2021-07-03 06:30:47,838 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Registered FSNamesystemState MBean
2021-07-03 06:30:47,852 INFO org.apache.hadoop.hdfs.server.namenode.LeaseManager: Number of blocks under construction: 0
2021-07-03 06:30:47,865 INFO org.apache.hadoop.hdfs.StateChange: STATE* Safe mode ON. 
The reported blocks 0 needs additional 331 blocks to reach the threshold 0.9990 of total blocks 332.
The number of live datanodes 0 has reached the minimum number 0. Safe mode will be turned off automatically once the thresholds have been reached.
2021-07-03 06:30:47,952 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: NameNode RPC up at: node01/192.168.235.11:8020
2021-07-03 06:30:47,955 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Starting services required for standby state
2021-07-03 06:30:47,958 INFO org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer: Will roll logs on active node at node04/192.168.235.14:8020 every 120 seconds.
2021-07-03 06:30:47,964 INFO org.apache.hadoop.hdfs.server.namenode.ha.StandbyCheckpointer: Starting standby checkpoint thread...
Checkpointing active NN at http://node04:50070
Serving checkpoints at http://node01:50070
2021-07-03 06:30:47,949 INFO org.apache.hadoop.ipc.Server: IPC Server Responder: starting
2021-07-03 06:30:47,950 INFO org.apache.hadoop.ipc.Server: IPC Server listener on 8020: starting
2021-07-03 06:30:48,991 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:48,992 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:48,993 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:50,004 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:50,004 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:50,005 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:51,008 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:51,008 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:51,008 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:52,013 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:52,013 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:52,013 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:53,020 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:53,020 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:53,021 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:53,984 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 6007 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:54,023 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:54,023 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:54,023 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:54,986 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 7009 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:55,024 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:55,025 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:55,035 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:55,987 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 8010 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:56,026 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:56,027 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:56,036 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:56,988 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 9011 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:57,028 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:57,029 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:57,037 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:57,989 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Waited 10012 ms (timeout=20000 ms) for a response for selectInputStreams. No responses yet.
2021-07-03 06:30:58,030 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:58,031 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:58,040 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:58,043 WARN org.apache.hadoop.hdfs.server.namenode.FSEditLog: Unable to determine input streams from QJM to [192.168.235.12:8485, 192.168.235.13:8485, 192.168.235.14:8485]. Skipping.
org.apache.hadoop.hdfs.qjournal.client.QuorumException: Got too many exceptions to achieve quorum size 2/3. 3 exceptions thrown:
192.168.235.14:8485: Call From node01/192.168.235.11 to node04:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
192.168.235.13:8485: Call From node01/192.168.235.11 to node03:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
192.168.235.12:8485: Call From node01/192.168.235.11 to node02:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
	at org.apache.hadoop.hdfs.qjournal.client.QuorumException.create(QuorumException.java:81)
	at org.apache.hadoop.hdfs.qjournal.client.QuorumCall.rethrowException(QuorumCall.java:286)
	at org.apache.hadoop.hdfs.qjournal.client.AsyncLoggerSet.waitForWriteQuorum(AsyncLoggerSet.java:142)
	at org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager.selectInputStreams(QuorumJournalManager.java:473)
	at org.apache.hadoop.hdfs.server.namenode.JournalSet.selectInputStreams(JournalSet.java:278)
	at org.apache.hadoop.hdfs.server.namenode.FSEditLog.selectInputStreams(FSEditLog.java:1590)
	at org.apache.hadoop.hdfs.server.namenode.FSEditLog.selectInputStreams(FSEditLog.java:1614)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer.doTailEdits(EditLogTailer.java:244)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.doWork(EditLogTailer.java:395)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.access$300(EditLogTailer.java:348)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread$1.run(EditLogTailer.java:365)
	at org.apache.hadoop.security.SecurityUtil.doAsLoginUserOrFatal(SecurityUtil.java:481)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.run(EditLogTailer.java:361)
2021-07-03 06:30:58,044 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: FSNamesystem write lock held for 10067 ms via
java.lang.Thread.getStackTrace(Thread.java:1559)
org.apache.hadoop.util.StringUtils.getStackTrace(StringUtils.java:1021)
org.apache.hadoop.hdfs.server.namenode.FSNamesystemLock.writeUnlock(FSNamesystemLock.java:261)
org.apache.hadoop.hdfs.server.namenode.FSNamesystemLock.writeUnlock(FSNamesystemLock.java:220)
org.apache.hadoop.hdfs.server.namenode.FSNamesystem.writeUnlock(FSNamesystem.java:1566)
org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer.doTailEdits(EditLogTailer.java:278)
org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.doWork(EditLogTailer.java:395)
org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.access$300(EditLogTailer.java:348)
org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread$1.run(EditLogTailer.java:365)
org.apache.hadoop.security.SecurityUtil.doAsLoginUserOrFatal(SecurityUtil.java:481)
org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.run(EditLogTailer.java:361)
	Number of suppressed write-lock reports: 0
	Longest write-lock held interval: 10067
2021-07-03 06:30:58,062 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Stopping services started for standby state
2021-07-03 06:30:58,170 WARN org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer: Edit log tailer interrupted
java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.doWork(EditLogTailer.java:413)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.access$300(EditLogTailer.java:348)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread$1.run(EditLogTailer.java:365)
	at org.apache.hadoop.security.SecurityUtil.doAsLoginUserOrFatal(SecurityUtil.java:481)
	at org.apache.hadoop.hdfs.server.namenode.ha.EditLogTailer$EditLogTailerThread.run(EditLogTailer.java:361)
2021-07-03 06:30:58,184 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem: Starting services required for active state
2021-07-03 06:30:58,191 INFO org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager: Starting recovery process for unclosed journal segments...
2021-07-03 06:30:59,226 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:59,227 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:30:59,230 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 0 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:00,228 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:00,231 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:00,432 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 1 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:01,232 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:01,233 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:01,434 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 2 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:02,234 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:02,235 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:02,436 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 3 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:03,237 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:03,239 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:03,439 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 4 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:04,239 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:04,245 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:04,441 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 5 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:05,241 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:05,247 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:05,443 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 6 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:06,248 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:06,248 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:06,447 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 7 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:07,268 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:07,268 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:07,463 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:08,281 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node02/192.168.235.12:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:08,281 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:08,472 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node04/192.168.235.14:8485. Already tried 9 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
2021-07-03 06:31:08,477 FATAL org.apache.hadoop.hdfs.server.namenode.FSEditLog: Error: recoverUnfinalizedSegments failed for required journal (JournalAndStream(mgr=QJM to [192.168.235.12:8485, 192.168.235.13:8485, 192.168.235.14:8485], stream=null))
org.apache.hadoop.hdfs.qjournal.client.QuorumException: Got too many exceptions to achieve quorum size 2/3. 3 exceptions thrown:
192.168.235.13:8485: Call From node01/192.168.235.11 to node03:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
192.168.235.12:8485: Call From node01/192.168.235.11 to node02:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
192.168.235.14:8485: Call From node01/192.168.235.11 to node04:8485 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused
	at org.apache.hadoop.hdfs.qjournal.client.QuorumException.create(QuorumException.java:81)
	at org.apache.hadoop.hdfs.qjournal.client.QuorumCall.rethrowException(QuorumCall.java:286)
	at org.apache.hadoop.hdfs.qjournal.client.AsyncLoggerSet.waitForWriteQuorum(AsyncLoggerSet.java:142)
	at org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager.createNewUniqueEpoch(QuorumJournalManager.java:180)
	at org.apache.hadoop.hdfs.qjournal.client.QuorumJournalManager.recoverUnfinalizedSegments(QuorumJournalManager.java:438)
	at org.apache.hadoop.hdfs.server.namenode.JournalSet$8.apply(JournalSet.java:624)
	at org.apache.hadoop.hdfs.server.namenode.JournalSet.mapJournalsAndReportErrors(JournalSet.java:393)
	at org.apache.hadoop.hdfs.server.namenode.JournalSet.recoverUnfinalizedSegments(JournalSet.java:621)
	at org.apache.hadoop.hdfs.server.namenode.FSEditLog.recoverUnclosedStreams(FSEditLog.java:1521)
	at org.apache.hadoop.hdfs.server.namenode.FSNamesystem.startActiveServices(FSNamesystem.java:1180)
	at org.apache.hadoop.hdfs.server.namenode.NameNode$NameNodeHAContext.startActiveServices(NameNode.java:1919)
	at org.apache.hadoop.hdfs.server.namenode.ha.ActiveState.enterState(ActiveState.java:61)
	at org.apache.hadoop.hdfs.server.namenode.ha.HAState.setStateInternal(HAState.java:64)
	at org.apache.hadoop.hdfs.server.namenode.ha.StandbyState.setState(StandbyState.java:49)
	at org.apache.hadoop.hdfs.server.namenode.NameNode.transitionToActive(NameNode.java:1777)
	at org.apache.hadoop.hdfs.server.namenode.NameNodeRpcServer.transitionToActive(NameNodeRpcServer.java:1649)
	at org.apache.hadoop.ha.protocolPB.HAServiceProtocolServerSideTranslatorPB.transitionToActive(HAServiceProtocolServerSideTranslatorPB.java:107)
	at org.apache.hadoop.ha.proto.HAServiceProtocolProtos$HAServiceProtocolService$2.callBlockingMethod(HAServiceProtocolProtos.java:4460)
	at org.apache.hadoop.ipc.ProtobufRpcEngine$Server$ProtoBufRpcInvoker.call(ProtobufRpcEngine.java:503)
	at org.apache.hadoop.ipc.RPC$Server.call(RPC.java:989)
	at org.apache.hadoop.ipc.Server$RpcCall.run(Server.java:871)
	at org.apache.hadoop.ipc.Server$RpcCall.run(Server.java:817)
	at java.security.AccessController.doPrivileged(Native Method)
	at javax.security.auth.Subject.doAs(Subject.java:422)
	at org.apache.hadoop.security.UserGroupInformation.doAs(UserGroupInformation.java:1893)
	at org.apache.hadoop.ipc.Server$Handler.run(Server.java:2606)
2021-07-03 06:31:08,479 INFO org.apache.hadoop.util.ExitUtil: Exiting with status 1: Error: recoverUnfinalizedSegments failed for required journal (JournalAndStream(mgr=QJM to [192.168.235.12:8485, 192.168.235.13:8485, 192.168.235.14:8485], stream=null))
2021-07-03 06:31:08,531 INFO org.apache.hadoop.hdfs.server.namenode.NameNode: SHUTDOWN_MSG: 
/************************************************************
SHUTDOWN_MSG: Shutting down NameNode at node01/192.168.235.11
************************************************************/
```

## 定位步骤

### 可能是磁盘空间不够了

进入到logs目录，/opt/stanlong/hadoop-ha/hadoop-2.9.2/logs，清空日志。 然后停掉服务，再重启。

**如果还报错**

### 检查 journal日志

三台JNN机器上， node02 上journal日志一直报错， node03和 node04日志正常。 根据日志分析，可能是node02上的edits文件损坏。

处理方式，到node02上 edits 文件所在的目录  ` /var/data/hadoop/ha/jnn/hacluster/ `， 先备份

```shell
tar -zcf node02_edits_bak.tar ./current
```

备份完成后，将node03上的 current 拷贝到node02 上

然后再重启hadoop服务

**如果还报错**

### 加参数

这个时候查看NN日志有这么一段

```
2021-07-03 18:57:14,062 INFO org.apache.hadoop.ipc.Client: Retrying connect to server: node03/192.168.235.13:8485. Already tried 8 time(s); retry policy is RetryUpToMaximumCountWithFixedSleep(maxRetries=10, sleepTime=1000 MILLISECONDS)
```

分析原因可能是 NN连接JNN没连上， 因此在 core-site.xml 里添加如下参数。 

```xml
	<!--配置nn连接journal的重试次数和时间间隔 -->
	<property>
        <name>ipc.client.connect.max.retries</name>
        <value>100</value>
    </property>
    <property>
        <name>ipc.client.connect.retry.interval</name>
        <value>10000</value>
    </property>
```

再重启，启动成功



