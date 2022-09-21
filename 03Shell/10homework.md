## 批量更改表

```shell
# 从tablename 里读取表名后批量更改
# 并将错误日志输出到 total.log

#!/bin/bash
date_list=`date +"%Y%m%d%H%M%S"` 
mkdir $date_list
while read line
do
    beeline -e "alter table $line rename to $line_off20211116" > ./$date_list/$line.log 2&>1
    grep -i error ./$date_list/$line.log > ./$date_list/$line.check.log
    if[-s ./$date_list/$line.check.log]; then
        echo $line error
        cat ./$date_list/$line.check.log >> ./$date_list/total.log
        rm ./$date_list/$line.check.log ./$date_list/$line.check.log
    else
        echo $line ok
        rm ./$date_list/$line.check.log ./$date_list/$line.check.log
    fi
done < ./tablename
```

## 小练习

+ 添加用户
+ 用户密码同用户名
+ 静默运行脚本
+ 避免捕获用户接口
+ 程序自定义输出

```
[root@gmall ~]# vi userAdd.sh
#!/bin/bash
useradd $1
echo $1 | passwd --stdin $1 &> /dev/null
echo "user add ok"
[root@gmall ~]# chmod +x userAdd.sh 
[root@gmall ~]# ./userAdd.sh stanlong
user add ok
脚本优化：
[root@gmall ~]# vi userAdd.sh
#!/bin/bash
[ ! $# -eq 1 ] && echo "args error..." && exit 2
id $1 &> /dev/null && echo "user exist..." && exit 3
useradd $1 &> /dev/null && echo $1 | passwd --stdin $1 &> /dev/null && echo "user add ok" && exit 0
echo "No permission to add user" && exit 9
```

+ 用户给定目录
+ 输出文件大小最大的文件
+ 递归子目录

```
#!/bin/bash
oldIFS=$IFS
IFS=$'\n'
for i in `du -a $1 | sort -rn`; do
    echo $i
    fileName=`echo $i | awk '{print $2}'`
    if [ -f $fileName ]; then
        echo $fileName
        exit 0
    fi
done
IFS=$oldIFS
```

for i in `du -a $1 | sort -rn`; do
:bash在解释这条语句的时候，先执行命令替换，执行完成之后生成一段文本。这个时候还没有执行 for， bash 对这段文件执行了第二次词的拆分的扩展（根据制表符，空白，换行符进行word单词的切割），我们希望只按换行符切割。因为bash在做词的切割 的时候参照的是一个环境变量给出的切割符，这个环境变量是IFS, 这个环境变量存有三个字符，空白符，换行符，制表符
$'\n': 换行符代表的ASCII码

+ 定义一个计数器 num
+ 打印 num 正好是文件行数

文件准备

```
test.txt
a 1
b 2
c 3
```

```
#!/bin/bash
num=0
oldIFS=$IFS
IFS=$'\n'
for i in `cat test.txt`;do
    echo $i
    ((num++))
done
echo num:$num
IFS=$oldIFS

echo "---------------------------------------------"

num=0
lines=`cat test.txt | wc -l`
for ((i=1;i<=lines;i++));do
   line=`head -$i test.txt | tail -1`
   echo $line
   ((num++))
done
echo num:$num

echo "---------------------------------------------"

num=0
while read line;do
    echo $line
    ((num++))

done < test.txt
echo num:$num

echo "---------------------------------------------"

num=0
cat test.txt | (while read line;do
    echo $line
    ((num++))
    done ; echo num:$num)

echo "---------------------------------------------"

num=0
cat test.txt | { while read line;do
    echo $line
    ((num++))
    done ; echo num:$num ;}
```

管道父子进程：以第四个脚本为例
管道 | 左侧和右侧是两个bash. num 是在父进程中定义的，而管道是两个子进程子进程对变量的修改不会影响父进程,管道右边会先创建一个子进程

## ROOT免密登录

```shell
function root_nopass_shell(){
    host_ip=$1
    shift
    shell_command="$@"
    expect <<EOF 
spawn ssh root@$host_ip $shell_command
expect {
"yes/no" { send "yes\r"; exp_continue}
"*password:" { send "$root_passwd\r" }
}
expect eof
EOF
}
```

## 批量分发功能

各节点上要预先安装 rsync 工具

```shell
function node_sync_file()
{
    if [ $# -lt 2 ] ;then
        echo "Not Enough Arguement!"
        exit;
    fi

    host_group=$1
    src_dir=$2
    declare dest_dir=""

    pdir=`cd -P $(dirname $src_dir); pwd`
    echo pdir=$pdir

    fname=`basename $src_dir`
    echo fname=$fname

    user=`whoami`

    if [[ -z $3 ]];then
        dest_dir=$pdir/
    else
        dest_dir=${3%/}/
    fi

    if [ -O $src_dir ];then
        
        if [[ $host_group == "all" ]];then
            hosts=$(get_cluster_ip)
            for ip in $hosts
            do
                echo ====================  $ip ====================
                rsync -av $pdir/$fname $user@$ip:$dest_dir
            done
        else
            current_hosts_list=$(utils_get_host_list $host_group)
            for ip in $current_hosts_list;do
                echo ====================  $ip  ====================
                rsync -av $pdir/$fname $user@$ip:$dest_dir
            done
        fi
    fi

    if [ ! -O $src_dir ];then
        if [[ $host_group == "all" ]];then
            hosts=$(get_cluster_ip)

            for ip in $hosts
            do
                shell_command="su - root -c 'rsync -av $pdir/$fname root@$ip:$dest_dir'"
                root_nopass_shell $ip $shell_command
            done
        else
            current_hosts_list=$(utils_get_host_list $host_group)
            for ip in $current_hosts_list;do
                shell_command="su - root -c 'rsync -av $pdir/$fname root@$ip:$dest_dir'"
                root_nopass_shell $ip $shell_command
            done
        fi
    fi

}
```

## 在不分发脚本的情况下选程执行本地脚本

```shell
function execute_shell()
{
    if [ $# != 2 ];then
        echo "Not Enough Arguement!"
        exit;
    fi
    host_group=$1
    shell_command=$(base64 -w0 $2)
    shell_args=$3
    if [[ -z $shell_args ]];then
        if [[ $host_group == "all" ]];then
            hosts=$(get_cluster_ip)
            for ip in $hosts
            do
                echo ====================  $ip ====================
                ssh $ip "echo `base64 -w0 $shell_command` | base64 -d | bash && exit"
            done
        else
            current_hosts_list=$(utils_get_host_list $host_group)
            for ip in $current_hosts_list;do
                echo ====================  $ip  ====================
                ssh $ip " echo `base64 -w0 $shell_command` | base64 -d | bash && exit"
            done
        fi
    else 
        if [[ $host_group == "all" ]];then
            hosts=$(get_cluster_ip)
            for ip in $hosts
            do
                echo ====================  $ip ====================
                ssh $node_ip "echo `base64 -w0 $shell_command` |base64 -d>/tmp/test.sh;bash /tmp/test.sh $shell_args && rm -rf /tmp/test.sh"
            done
        else
            current_hosts_list=$(utils_get_host_list $host_group)
            for ip in $current_hosts_list;do
                echo ====================  $ip  ====================
                ssh $node_ip "echo `base64 -w0 $shell_command` |base64 -d>/tmp/test.sh;bash /tmp/test.sh $shell_args && rm -rf /tmp/test.sh"
            done
        fi
    fi
}
```

## 组合命令实战

基本步骤：1、先截取行  2、统一数据格式  3、截取字符串  4、处理字符串

1. 检索本机IP,  NETMASK, MAC 地址， 广播地址

   ```shell
   # IP 地址
   ifconfig ens192 | grep -w "inet" | tr -s " " | cut -d" " -f3 | xargs echo "IP: "
   IP:  192.168.6.101
   
   # 子网掩码
   ifconfig ens192 | grep -w "inet" | tr -s " " | cut -d" " -f5 | xargs echo "NETMASK: "
   NETMASK:  255.255.255.0
   
   # 广播地址
   ifconfig ens192 | grep -w "inet" | tr -s " " | cut -d" " -f7 | xargs echo "BOARDCAST: "
   BOARDCAST:  192.168.6.255
   
   # MAC地址
   ifconfig ens192 | grep -w "ether" | tr -s " " | cut -d" " -f3 | xargs echo "MAC_ADDRESS: "
   MAC_ADDRESS:  00:50:56:a7:ee:e5
   ```

2. 将系统中所有普通用户的用户名、密码和默认shell保存到一个文件中，要求用户名密码和默认shell之间用tab分割

   说明：linux系统中的用户分为管理员，系统用户，普通用户。

   管理员uid=0,  系统用户 uid<1000, 普通用户 uid > 1000

   ```shell
   cp /etc/passwd .
   
   # 查看普通用户
   grep -i "bash" passwd | grep -v "root"
   hadoop:x:2021:2021::/home/hadoop:/bin/bash
   oracle:x:2020:2020::/home/oracle:/bin/bash
   
   # 完成题目要求
   grep -i "bash" passwd | grep -v "root" | cut -d":" -f1,2,7 | tr ":" "\t"
   hadoop	x	/bin/bash
   oracle	x	/bin/bash
   ```

   



