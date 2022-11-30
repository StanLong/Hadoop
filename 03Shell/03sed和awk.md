# awk:文本分析工具
**简单来说awk就是把文件逐行读入，（空格，制表符）为默认分隔符将每行切片，切开的部分再进行各种分析处理** 

+ awk -F'{pattern + action}' {filenames}
 - 支持自定义分隔符
 - 支持正则表达式匹配
 - 支持自定义变量，数组a[1] a[tom] map(key)
 - 支持内置变量
  	- ARGC 命令行参数个数
  	- ARGV 命令行参数排列
  	- ENVIRON 支持系统环境变量的使用
  	- FILENAME awk 浏览的文件名
  	- FNR 浏览文件的记录数
  	- FS 设置输入域分隔符，等价于命令行-F选项
  	- NF 浏览记录的域的个数
  	- NR 已读的记录数
  	- OFS 输出域分隔符
  	- ORS 输出记录分隔符
  	- RS 控制记录分隔符
 - 支持函数
    - print、split、substr、sub、gsub
 - 支持流程控制语言，类c语言
    - if、while、do/while、for、break、continue

**awk实操**

1. 文件准备

```shell
[root@gmall ~]# cp /etc/passwd ./
[root@gmall ~]# ll
total 12
-rw-------. 1 root root 1552 Jan 30 21:36 anaconda-ks.cfg
-rw-r--r--  1 root root  798 Feb  2 12:19 passwd
-rw-r--r--  1 root root   13 Feb  1 20:39 zlftext.txt
[root@gmall ~]# cat passwd 
root:x:0:0:root:/root:/bin/bash
bin:x:1:1:bin:/bin:/sbin/nologin
daemon:x:2:2:daemon:/sbin:/sbin/nologin
adm:x:3:4:adm:/var/adm:/sbin/nologin
lp:x:4:7:lp:/var/spool/lpd:/sbin/nologin
sync:x:5:0:sync:/sbin:/bin/sync
shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
halt:x:7:0:halt:/sbin:/sbin/halt
mail:x:8:12:mail:/var/spool/mail:/sbin/nologin
operator:x:11:0:operator:/root:/sbin/nologin
games:x:12:100:games:/usr/games:/sbin/nologin
ftp:x:14:50:FTP User:/var/ftp:/sbin/nologin
nobody:x:99:99:Nobody:/:/sbin/nologin
systemd-network:x:192:192:systemd Network Management:/:/sbin/nologin
dbus:x:81:81:System message bus:/:/sbin/nologin
polkitd:x:999:997:User for polkitd:/:/sbin/nologin
postfix:x:89:89::/var/spool/postfix:/sbin/nologin
sshd:x:74:74:Privilege-separated SSH:/var/empty/sshd:/sbin/nologin
```

2. 只显示文件中的第一列（）信息,按冒号分隔，打印出第一列的信息

```shell
[root@gmall ~]# awk -F':' '{print $1 }' passwd 
root
bin
daemon
adm
lp
sync
shutdown
halt
mail
operator
games
ftp
nobody
systemd-network
dbus
polkitd
postfix
sshd
```
+ 用cut命令也可以 -f1 打印第一列
```shell
[root@gmall ~]# cut -d':' -f1 passwd 
root
bin
daemon
adm
lp
sync
shutdown
halt
mail
operator
games
ftp
nobody
systemd-network
dbus
polkitd
postfix
sshd
```

3. 只显示帐户和帐户对应的shell， 而帐户与shell之间以逗号分割，而且在所有行开始前添加列名name， shell，在最后一行添加"blue,/bin/nosh"

```shell
[root@gmall ~]# awk -F':' 'BEGIN{print "name\tshell"} {print $1 "\t" $7} END{print "blue,/bin/nosh"}' passwd 
name	shell
root	/bin/bash
bin	/sbin/nologin
daemon	/sbin/nologin
adm	/sbin/nologin
lp	/sbin/nologin
sync	/bin/sync
shutdown	/sbin/shutdown
halt	/sbin/halt
mail	/sbin/nologin
operator	/sbin/nologin
games	/sbin/nologin
ftp	/sbin/nologin
nobody	/sbin/nologin
systemd-network	/sbin/nologin
dbus	/sbin/nologin
polkitd	/sbin/nologin
postfix	/sbin/nologin
sshd	/sbin/nologin
blue,/bin/nosh
```
+ 用cut和sed命令实现
```shell
[root@gmall ~]# cut -d':' -f1,7 passwd | sed "1i\name:shell"
name:shell
root:/bin/bash
bin:/sbin/nologin
daemon:/sbin/nologin
adm:/sbin/nologin
lp:/sbin/nologin
sync:/bin/sync
shutdown:/sbin/shutdown
halt:/sbin/halt
mail:/sbin/nologin
operator:/sbin/nologin
games:/sbin/nologin
ftp:/sbin/nologin
nobody:/sbin/nologin
systemd-network:/sbin/nologin
dbus:/sbin/nologin
polkitd:/sbin/nologin
postfix:/sbin/nologin
sshd:/sbin/nologin
```

4. 搜索有root关键字的所有行

```shell
$0 打印整行
[root@gmall ~]# awk -F':' '/root/{print $0}' passwd 
root:x:0:0:root:/root:/bin/bash
operator:x:11:0:operator:/root:/sbin/nologin
```

5. 统计每行的行号，每行的列数，对应的完整内容

```shell
[root@gmall ~]# awk -F':' '{print NR"\t"NF"\t" $0}' passwd 
1	7	root:x:0:0:root:/root:/bin/bash
2	7	bin:x:1:1:bin:/bin:/sbin/nologin
3	7	daemon:x:2:2:daemon:/sbin:/sbin/nologin
4	7	adm:x:3:4:adm:/var/adm:/sbin/nologin
5	7	lp:x:4:7:lp:/var/spool/lpd:/sbin/nologin
6	7	sync:x:5:0:sync:/sbin:/bin/sync
7	7	shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
8	7	halt:x:7:0:halt:/sbin:/sbin/halt
9	7	mail:x:8:12:mail:/var/spool/mail:/sbin/nologin
10	7	operator:x:11:0:operator:/root:/sbin/nologin
11	7	games:x:12:100:games:/usr/games:/sbin/nologin
12	7	ftp:x:14:50:FTP User:/var/ftp:/sbin/nologin
13	7	nobody:x:99:99:Nobody:/:/sbin/nologin
14	7	systemd-network:x:192:192:systemd Network Management:/:/sbin/nologin
15	7	dbus:x:81:81:System message bus:/:/sbin/nologin
16	7	polkitd:x:999:997:User for polkitd:/:/sbin/nologin
17	7	postfix:x:89:89::/var/spool/postfix:/sbin/nologin
18	7	sshd:x:74:74:Privilege-separated SSH:/var/empty/sshd:/sbin/nologin
```

# 统计报表

**合计每人1月工资， 0：manager， 1：worker**

**文件准备**

```shell
[root@gmall ~]# cat awk.txt 
Tom	0	2012-12-11	car	3000
John	1	2013-01-13	bike	1000
vivi	1	2013-01-18	car	2800
Tom	0	2013-01-20	car	2500
John	1	2013-01-28	bike	3500

[root@gmall ~]# awk '{split($3,date,"-");if(date[2]=="01"){name[$1]+=$5}} END{for(i in name){print i"\t"name[i]}}' awk.txt 
vivi	2800
Tom	2500
John	4500
```


