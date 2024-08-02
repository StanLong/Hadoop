# keeplived

keepalived 是集群管理中保证集群高可用的服务软件. 实验用keepalived实现负载均衡

**缺点：当主keepalived异常退出的时候，不能及时卸下虚拟ip，而虚拟ip又漂移到备机上，会造成互联网中出现相同的ip**

## 节点规划

|        |      |            |                           |                  |
| ------ | ---- | ---------- | ------------------------- | ---------------- |
| node01 | 主   | keepalived |                           | 启动主keepalived |
| node02 | RS01 | httpd 80   | 调整内核ARP通告和响应级别 | 启动httpd        |
| node03 | RS02 | httpd 80   | 调整内核ARP通告和响应级别 | 启动httpd        |
| node04 | 备   | keepalived |                           | 启动备keepalived |

## 安装keepalived

```shell
[root@node01 ~]# yum install keepalived -y
```

```shell
[root@node04 ~]# yum install keepalived ipvsadm -y # node04上安装ipvsadm用来查看负载情况 
```

## keepalived 配置

```shell
[root@node01 ~]# cd /etc/keepalived/
[root@node01 keepalived]# ll
total 4
-rw-r--r-- 1 root root 3598 Oct  1 00:39 keepalived.conf
[root@node01 keepalived]# cp keepalived.conf keepalived.conf_bak
[root@node01 keepalived]# vi keepalived.conf
```

node01 主 keepalived 配置

```shell
! Configuration File for keepalived

global_defs {
   notification_email {
     acassen@firewall.loc
     failover@firewall.loc
     sysadmin@firewall.loc
   }
   notification_email_from Alexandre.Cassen@firewall.loc
   smtp_server 192.168.200.1
   smtp_connect_timeout 30
   router_id LVS_DEVEL
   vrrp_skip_check_adv_addr
   vrrp_strict
   vrrp_garp_interval 0
   vrrp_gna_interval 0
}

vrrp_instance VI_1 {
    state MASTER # 主
    interface ens33 # 网卡名称
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
       192.168.235.100/24 dev ens33 label ens33:3 # 虚拟地址
    }
}

virtual_server 192.168.235.100 80 {
    delay_loop 6
    lb_algo rr
    lb_kind DR
    persistence_timeout 0
    protocol TCP

    real_server 192.168.235.12 80 {
        weight 1
        HTTP_GET {
            url {
              path /
              status_code 200
            }
            connect_timeout 3
            nb_get_retry 3
            delay_before_retry 3
        }
    }
    real_server 192.168.235.13 80 {
        weight 1
        HTTP_GET {
            url {
              path /
	      status_code 200
            }
            connect_timeout 3
            nb_get_retry 3
            delay_before_retry 3
        }
    }
}
```

node04 备 keepalived 配置

```shell
[root@node01 keepalived]# scp keepalived.conf node04:/etc/keepalived/
```

```shell
[root@node04 keepalived]# ll
total 8
-rw-r--r-- 1 root root 1307 Jan 10 01:14 keepalived.conf
-rw-r--r-- 1 root root 3598 Jan 10 01:14 keepalived.conf_bak
[root@node04 keepalived]# vi keepalived.conf
```

```she
! Configuration File for keepalived

global_defs {
   notification_email {
     acassen@firewall.loc
     failover@firewall.loc
     sysadmin@firewall.loc
   }
   notification_email_from Alexandre.Cassen@firewall.loc
   smtp_server 192.168.200.1
   smtp_connect_timeout 30
   router_id LVS_DEVEL
   vrrp_skip_check_adv_addr
   vrrp_strict
   vrrp_garp_interval 0
   vrrp_gna_interval 0
}

vrrp_instance VI_1 {
    state BACKUP # 备
    interface ens33
    virtual_router_id 51
    priority 50
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
       192.168.235.100/24 dev ens33 label ens33:3 
    }
}

virtual_server 192.168.235.100 80 {
    delay_loop 6
    lb_algo rr
    lb_kind DR
    persistence_timeout 0
    protocol TCP

    real_server 192.168.235.12 80 {
        weight 1
        HTTP_GET {
            url {
              path /
              status_code 200
            }
            connect_timeout 3
            nb_get_retry 3
            delay_before_retry 3
        }
    }
    real_server 192.168.235.13 80 {
        weight 1
        HTTP_GET {
            url {
              path /
	      status_code 200
            }
            connect_timeout 3
            nb_get_retry 3
            delay_before_retry 3
        }
    }
}
```

## 调整RS的响应和通告级别并配置RS的VIP

参考02LVS的实操

## 启动RS上的httpd

```shell
[root@node02 ~]# service httpd start
```

## 启动keepalived

### 启动node01主keepalived

```shell
[root@node01 ~]# service keepalived start
Redirecting to /bin/systemctl start keepalived.service
[root@node01 ~]# ifconfig
ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.235.11  netmask 255.255.255.0  broadcast 192.168.235.255
        inet6 fe80::4776:cded:346a:2363  prefixlen 64  scopeid 0x20<link>
        ether 00:0c:29:fd:d4:e0  txqueuelen 1000  (Ethernet)
        RX packets 17225  bytes 14984837 (14.2 MiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 5504  bytes 616804 (602.3 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

ens33:3: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.235.100  netmask 255.255.255.0  broadcast 0.0.0.0
        ether 00:0c:29:fd:d4:e0  txqueuelen 1000  (Ethernet)

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1  (Local Loopback)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
[root@node01 keepalived]# ipvsadm -ln
IP Virtual Server version 1.2.1 (size=4096)
Prot LocalAddress:Port Scheduler Flags
  -> RemoteAddress:Port           Forward Weight ActiveConn InActConn
TCP  192.168.235.100:80 rr
  -> 192.168.235.12:80            Route   1      0          1         
  -> 192.168.235.13:80            Route   1      0          2         
```

在浏览器中访问 192.168.235.100. 观察实验现象。实验现象和02LVS实验现象相同

### 启动node04备keepalived

```shell
[root@node04 keepalived]# service keepalived start
Redirecting to /bin/systemctl start keepalived.service
[root@node04 keepalived]# ifconfig
ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.235.14  netmask 255.255.255.0  broadcast 192.168.235.255
        inet6 fe80::c833:d85e:5081:6819  prefixlen 64  scopeid 0x20<link>
        inet6 fe80::4776:cded:346a:2363  prefixlen 64  scopeid 0x20<link>
        inet6 fe80::5e46:3279:9c35:eaf3  prefixlen 64  scopeid 0x20<link>
        ether 00:0c:29:cc:d5:09  txqueuelen 1000  (Ethernet)
        RX packets 24555  bytes 26991535 (25.7 MiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 7911  bytes 705704 (689.1 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1  (Local Loopback)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
[root@node04 keepalived]# ipvsadm -ln
IP Virtual Server version 1.2.1 (size=4096)
Prot LocalAddress:Port Scheduler Flags
  -> RemoteAddress:Port           Forward Weight ActiveConn InActConn
TCP  192.168.235.100:80 rr
  -> 192.168.235.12:80            Route   1      0          0         
  -> 192.168.235.13:80            Route   1      0          0     
```

## 模拟keepalived高可用

1. 关闭node01的网卡，使keepalived挂机

```shell
[root@node01 keepalived]# ifconfig ens33 down #关闭node01上的物理网卡
```

```shell
# 虚拟地址现在漂移到 node04 上
[root@node04 keepalived]# ifconfig
ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.235.14  netmask 255.255.255.0  broadcast 192.168.235.255
        inet6 fe80::c833:d85e:5081:6819  prefixlen 64  scopeid 0x20<link>
        inet6 fe80::4776:cded:346a:2363  prefixlen 64  scopeid 0x20<link>
        inet6 fe80::5e46:3279:9c35:eaf3  prefixlen 64  scopeid 0x20<link>
        ether 00:0c:29:cc:d5:09  txqueuelen 1000  (Ethernet)
        RX packets 25666  bytes 27099098 (25.8 MiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 8751  bytes 781985 (763.6 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

ens33:3: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.235.100  netmask 255.255.255.0  broadcast 0.0.0.0
        ether 00:0c:29:cc:d5:09  txqueuelen 1000  (Ethernet)

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1  (Local Loopback)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```

2. 重启node01的网卡

![](./doc/03.png)

```shell
# 备机的虚拟地址重新漂移回主机
[root@node04 keepalived]# ifconfig
ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.235.14  netmask 255.255.255.0  broadcast 192.168.235.255
        inet6 fe80::c833:d85e:5081:6819  prefixlen 64  scopeid 0x20<link>
        inet6 fe80::4776:cded:346a:2363  prefixlen 64  scopeid 0x20<link>
        inet6 fe80::5e46:3279:9c35:eaf3  prefixlen 64  scopeid 0x20<link>
        ether 00:0c:29:cc:d5:09  txqueuelen 1000  (Ethernet)
        RX packets 26309  bytes 27169513 (25.9 MiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 9482  bytes 839021 (819.3 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1  (Local Loopback)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```



