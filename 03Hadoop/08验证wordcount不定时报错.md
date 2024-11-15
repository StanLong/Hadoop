```
21/02/19 08:28:16 INFO client.ConfiguredRMFailoverProxyProvider: Failing over to rm1
21/02/19 08:28:16 INFO retry.RetryInvocationHandler: java.net.ConnectException: Call From node03/192.168.235.13 to node02:8032 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused, while invoking ApplicationClientProtocolPBClientImpl.getNewApplication over rm1 after 4 failover attempts. Trying to failover after sleeping for 22203ms.
21/02/19 08:28:38 INFO client.ConfiguredRMFailoverProxyProvider: Failing over to rm2
21/02/19 08:28:38 INFO retry.RetryInvocationHandler: java.net.ConnectException: Call From node03/192.168.235.13 to node03:8032 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused, while invoking ApplicationClientProtocolPBClientImpl.getNewApplication over rm2 after 5 failover attempts. Trying to failover after sleeping for 39654ms.
```

