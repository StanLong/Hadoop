# Linux 运维

## 1 管理口配置命令

```shell
1 需要先安装 ipmitool 
	yum install ipmitool

2 管理口配置命令
	ipmitool lan print 1 #打印当前ipmi 地址配置信息。
	ipmitool lan set 1 ipsrc static  # 设置 id 1 为静态IP地址。
	ipmitool lan set 1 ipaddr 10.42.223.71  # 设置 IPMI 地址。
	ipmitool lan set 1 netmask 255.255.255.0 # 设置 IPMI 子网掩码。
	ipmitool lan set 1 defgw ipaddr 10.42.223.254 # 设置 IPMI 网关。
```

ipmitool使用报错处理

```shell
Could not open device at /dev/ipmi0 or /dev/ipmi/0 or /dev/ipmidev/0: No such file or directory
```

参考处理链接： https://www.cnblogs.com/samuel610/p/10868804.html?ivk_sa=1024320u  

```shell
解决办法：需要加载相关模块
查看先关模块是否加载（可以看出模块未加载）
#lsmod |grep ^ipmi

加载以下模块
# modprobe ipmi_watchdog
# modprobe ipmi_poweroff
# modprobe ipmi_devintf
# modprobe ipmi_si  加载该模块如果没有不影响ipmi的使用（与系统版本有关）
# modprobe ipmi_msghandler  加载该模块如果没有不影响ipmi的使用

之后就可以正常使用了：
ipmitool lan print   查看本机IPMI地址等信息
```

