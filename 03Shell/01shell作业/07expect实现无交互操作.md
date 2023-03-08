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

## 实例

```shell
#!/bin/bash

password1="gaoxn0130W"
password2="111111"

function root_nopass_shell(){
    host_ip=$1
    shift
    shell_command="$@"
    expect <<EOF
spawn ssh root@$host_ip $shell_command
expect {
"yes/no" { send "yes\r"; exp_continue}
"*password:" { send "$password1\r" }
}
expect eof
EOF
}

root_nopass_shell "$@"

```

for ip in `cat ip_list.txt`;do echo $ip; bash ssh.sh $ip dmidecode | grep "System Information" -A9 | egrep "Manufacturer|Product";done
