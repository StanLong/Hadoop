# expect实现无交互操作

## 安装

```shell
yum install expect 
```

## 测试

expect-01.sh 

```shell
#!/usr/bin/expect
spawn ssh root@192.168.229.12
expect {
	"yes/no" { send "yes\r"; exp_continue }
	"password:" { send "root\r" };
}
interact
```

注意不能用bash执行， 给脚本赋可执行权限，然后执行

## 整合Shell脚本

```shell
#!/bin/bash
echo "Long Great CHINA"
/usr/bin/expect <<EOF
spawn ssh 192.168.229.12 hostname
expect {
"yes/no" { send "yes\r"; exp_continue}
"*password:" { send "root\r" }
}
expect eof
EOF
echo "Long Great CHINA"
```

