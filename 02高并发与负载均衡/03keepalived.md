# keeplived

## 一、工作原理

Keepalived是一个基于VRRP协议来实现LVS服务高可用方案，可以利用其来避免单点故障。一个LVS服务会使用2台服务器运行Keepalived，一台为主服务器MASTER，另一台为备份服务器BACKUP，但是对外表现为一个虚拟IP，Master会不停的向Backup节点通告自己的心跳，当Backup服务器收不到这个消息的时候，即主服务器宕机的时候，备份服务器就会接管虚拟IP，继续提供服务，从而保证高可用性。直接一句话Keepalived就是VRRP协议的实现，该协议是虚拟冗余路由协议。

VRRP协议如图：

![](https://img2018.cnblogs.com/blog/1448094/201904/1448094-20190427140331315-1105050518.png)

这里有个问题，VRRP提供一个VIP，它可以来设定那个路由器是活动节点，然后出现故障进行切换，VIP也随之对应到新的路由器上，但是内网是通过MAC地址来寻址的，虽然VIP对应到了新的路由器上，可是MAC变了，客户端的ARP表也没有更新，所以还是用不了，为了解决这个问题VRRP不但提供VIP还提供VMAC地址，这个VMAC地址是VRRP单独申请的，大家都可以正常使用。故障切换的时候虽然改变了后端路由器，但是由于客户端使用的是VIP和VMAC地址，这样就不会有任何影响了。

## 二、实验过程

### 1、节点规划

| 主机名 | 业务规划            | 说明                                  |
| ------ | ------------------- | ------------------------------------- |
| node01 | keepalived - master | 启动主 keepalived                     |
| node02 | RS01 - httpd        | 调整内核ARP通告和响应级别 - 启动httpd |
| node03 | RS02 - httpd        | 调整内核ARP通告和响应级别 - 启动httpd |
| node04 | keepalived - backup | 启动备 keepalived                     |

### 2、RS配置

1. 先调整RS的响应和通告级别（每一台RS都配）

   ```shell
   echo 1 > /proc/sys/net/ipv4/conf/ens33/arp_ignore
   echo 2 > /proc/sys/net/ipv4/conf/ens33/arp_announce
   echo 1 > /proc/sys/net/ipv4/conf/all/arp_ignore
   echo 2 > /proc/sys/net/ipv4/conf/all/arp_announce
   ```

2. 再配置RS的VIP（每一台RS都配）

   这里记录了node02上的操作，在node03上做同样的操作即可

   ```shell
   # 注意这里子网掩码配置了4个255， 如果配置了3个255会导致数据从ens33走，虚拟IP是配置在lo上的
   [root@node02 all]# ifconfig lo:8 192.168.235.100 netmask 255.255.255.255 
   [root@node02 all]# ifconfig
   lo:8: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
           inet 192.168.235.100  netmask 255.255.255.255
           loop  txqueuelen 1  (Local Loopback)
   ```

3. 启动两台RS上的httpd

   ```shell
   # httpd 静态的webserver 服务
   [root@node02 ~]# yum install httpd -y  # 安装httpd
   [root@node02 ~]# echo "from 192.168.235.12" >> /var/www/html/index.html
   [root@node02 ~]# service httpd start
   # 启动成功后在浏览器里访问 192.168.235.12 默认端口80
   ```

   ![](D:/StanLong/git_repository/Hadoop/02高并发与负载均衡/doc/02.png) ![](D:/StanLong/git_repository/Hadoop/02高并发与负载均衡/doc/10.png)

### 3、主节点配置

1. 安装keepalived

   ```shell
   [root@node01 ~]# yum install keepalived ipvsadm -y # 这里安装ipvsadm来查看负载情况
   ```

2. keepalived 配置

   ```shell
   [root@node01 ~]# cp /etc/keepalived/keepalived.conf /etc/keepalived/keepalived.conf_bak
   [root@node01 ~]# cat /etc/keepalived/keepalived.conf
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
   
   # 路由冗余协议配置
   vrrp_instance VI_1 {
       state MASTER # 主
       interface ens33 # 走 ens33 这个网卡
       virtual_router_id 51
       priority 100 # 主的 priority 要比备高
       advert_int 1
       authentication {
           auth_type PASS
           auth_pass 1111
       }
       virtual_ipaddress {
           192.168.235.100/24 dev ens33 label ens33:2 # 配置虚拟网卡，类似于执行命令 ifconfig ens33:2 192.168.235.100/24
       }
   }
   
   virtual_server 192.168.235.100 80 { # 配置成上面设置的虚拟IP，走80端口， 相当于执行命令：ipvsadm -A -t 192.168.235.100:80 -s rr
       delay_loop 6
       lb_algo rr # 轮询
       lb_kind DR # DR模型
       persistence_timeout 0 # 为了做实验看到效果，这里调成0
       protocol TCP
   
       real_server 192.168.235.12 80 { # RS配置
           weight 1
           HTTP_GET { # 走HTTP 服务
               url {
                 path /
                 status_code 200
               }
               connect_timeout 3
               nb_get_retry 3
               delay_before_retry 3
           }
       }
       real_server 192.168.235.13 80 { # RS配置
           weight 1
           HTTP_GET { # 走HTTP 服务
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

3. 启动keepalived

   ```shell
   [root@node01 ~]# service keepalived start
   [root@node01 ~]# ip addr
       inet 192.168.235.100/24 scope global secondary ens33:2 # 虚拟网卡已生成
          valid_lft forever preferred_lft forever
   ```

4. 验证LVS

   ```shell
   [root@node01 ~]# ipvsadm -ln
   IP Virtual Server version 1.2.1 (size=4096)
   Prot LocalAddress:Port Scheduler Flags
     -> RemoteAddress:Port           Forward Weight ActiveConn InActConn
   TCP  192.168.235.100:80 rr
     -> 192.168.235.12:80            Route   1      0          0         
     -> 192.168.235.13:80            Route   1      0          0   
   ```

   配置完成后即时生效， 在浏览器里访问 192.168.235.100. 不断刷新可看到 node02和node03返回的页面

      ![](D:/StanLong/git_repository/Hadoop/02高并发与负载均衡/doc/01.png)![](D:/StanLong/git_repository/Hadoop/02高并发与负载均衡/doc/11.png)

### 4、备节点配置

1. 安装keepalived

   ```shell
   [root@node04 ~]# yum install -y keepalived ipvsadm # 这里安装ipvsadm来查看负载情况
   ```

2. keepalived 配置

   ```shell
   [root@node04 ~]# cp /etc/keepalived/keepalived.conf /etc/keepalived/keepalived.conf_bak
   [root@node04 ~]# cat /etc/keepalived/keepalived.conf
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
   
   # 路由冗余协议配置
   vrrp_instance VI_1 {
       state BACKUP # 备
       interface ens33 # 走 ens33 这个网卡
       virtual_router_id 51
       priority 50 # 备的 priority 要比主低
       advert_int 1
       authentication {
           auth_type PASS
           auth_pass 1111
       }
       virtual_ipaddress {
           192.168.235.100/24 dev ens33 label ens33:2 # 配置虚拟网卡，类似于执行命令 ifconfig ens33:2 192.168.235.100/24
       }
   }
   
   virtual_server 192.168.235.100 80 { # 配置成上面设置的虚拟IP，走80端口， 相当于执行命令：ipvsadm -A -t 192.168.235.100:80 -s rr
       delay_loop 6
       lb_algo rr # 轮询
       lb_kind DR # DR模型
       persistence_timeout 0 # 为了做实验看到效果，这里调成0
       protocol TCP
   
       real_server 192.168.235.12 80 { # RS配置
           weight 1
           HTTP_GET { # 走HTTP 服务
               url {
                 path /
                 status_code 200
               }
               connect_timeout 3
               nb_get_retry 3
               delay_before_retry 3
           }
       }
       real_server 192.168.235.13 80 { # RS配置
           weight 1
           HTTP_GET { # 走HTTP 服务
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

3. 启动keepalived

   ```shell
   [root@node04 ~]# systemctl start keepalived
   [root@node04 ~]# ip addr # 会发现此时备机上的虚拟IP没有变化
   ```

4. 验证LVS

   ```shell
   [root@node04 ~]# ipvsadm -ln
   IP Virtual Server version 1.2.1 (size=4096)
   Prot LocalAddress:Port Scheduler Flags
     -> RemoteAddress:Port           Forward Weight ActiveConn InActConn
   TCP  192.168.235.100:80 rr
     -> 192.168.235.12:80            Route   1      0          0         
     -> 192.168.235.13:80            Route   1      0          0      
   ```

### 3、验证keepalived

1. 模拟主节点挂机，查看地址是否正常漂移到备机上

   ```shell
   [root@node01 ~]# ifconfig ens33 down #关闭node01上的物理网卡
   ```

   ```shell
   # 会发现虚拟地址现在漂移到 node04 上
   [root@node04 keepalived]# ifconfig
   ens33:3: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
           inet 192.168.235.100  netmask 255.255.255.0  broadcast 0.0.0.0
           ether 00:0c:29:cc:d5:09  txqueuelen 1000  (Ethernet)
   ```

   在网页上重新刷新页面，会发现并没有什么影响

2. 当主节点恢复正常后

   ```shell
   [root@node01 ~]# ifconfig ens33 up #重启node01上的物理网卡
   ```

   ```shell
   [root@node01 ~]# ifconfig # 会发现虚拟地址漂移回主节点
   ens33:2: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
           inet 192.168.235.100  netmask 255.255.255.0  broadcast 0.0.0.0
           ether 00:0c:29:fd:d4:e0  txqueuelen 1000  (Ethernet)
   ```

   ```shell
   [root@node04 ~]# ifconfig # 备机上已看不到虚拟地址
   ```

## 三、异常退出的影响

如果主keepalived异常退出了，主节点的虚拟IP可能来不及卸载掉，此时无法向备机发送心跳信息。keepalived 会认为主机挂掉，就把虚拟IP漂移到备机上，这个时候网络中就会出现两个相同的IP地址

