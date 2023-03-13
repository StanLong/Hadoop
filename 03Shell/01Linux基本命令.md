## Linux 基本命令

### date

常用命令

```shell
# 查看系统日期及时间
[root@node01 ~]# date
Thu Feb  9 22:39:01 CST 2023

# 以YYYY-MM-DD显示日期
[root@node01 ~]# date +%F
2023-02-09

# 以MM/DD/YY显示日期
[root@node01 ~]# date +%D
02/09/23

# 以MM/DD/YYYY显示日期
[root@node01 ~]# date +%x
02/09/2023

# 获取系统年、月、日
[root@node01 ~]# date +%Y
2023
[root@node01 ~]# date +%m
02
[root@node01 ~]# date +%d
09

# 获取系统星期值
[root@node01 ~]#  date +%a
Thu
[root@node01 ~]# date +%A
Thursday
[root@node01 ~]# date +%u
4
[root@node01 ~]# date +%w
4

```

**1、命令详解**

```
用法：#date [参数选项] [+格式]
或者：date [-u|–utc|–universal] [MMDDhhmm[[CC]YY][.ss]]
```

**2、参数说明**

| 参数                   | 参数说明                                 |
| ---------------------- | ---------------------------------------- |
| -d, --date=STRING      | 显示 datestr 中所设定的时间 (非系统时间) |
| -s, --set=STRING       | 将系统时间设为 datestr 中所设定的时间    |
| -u, --utc, --universal | 打印或设置协调世界时（UTC）              |
| –version               | 显示版本编号                             |
| –help                  | 显示辅助讯息                             |

**3、时间格式符号**

| 符号 | 符号说明                                           |
| ---- | -------------------------------------------------- |
| %    | 印出 %                                             |
| %n   | 下一行                                             |
| %t   | 跳格                                               |
| %H   | 小时(00…23)                                        |
| %I   | 小时(01…12)                                        |
| %k   | 小时(0…23)                                         |
| %l   | 小时(1…12)                                         |
| %M   | 分钟(00…59)                                        |
| %p   | 显示本地 AM 或 PM                                  |
| %r   | 直接显示时间 (12 小时制，格式为 hh:mm:ss [AP]M)    |
| %R   | 24小时制方式显示时间，相当于%H:%M                  |
| %s   | 从 1970 年 1 月 1 日 00:00:00 UTC 到目前为止的秒数 |
| %S   | 秒(00…60)                                          |
| %T   | 直接显示时间 (24 小时制)                           |
| %X   | 相当于 %H:%M:%S                                    |
| %z   | 数字方式显示时区                                   |
| %Z   | 字母缩写方式显示时区                               |

**4、日期格式符号**

| 符号 | 符号说明                                                |
| ---- | ------------------------------------------------------- |
| %a   | 星期几 ，缩写(Sun…Sat)                                  |
| %A   | 星期几 ，完整英文星期(Sunday…Saturday)                  |
| %b   | 月份 (Jan…Dec)                                          |
| %B   | 月份 (January…December)                                 |
| %c   | 直接显示日期与时间                                      |
| %d   | 日 (01…31)                                              |
| %D   | 直接显示日期 (mm/dd/yy)                                 |
| %e   | 一个月中的第几天，类似%_d                               |
| %F   | 完整的日期，相当于%Y-%m-%d                              |
| %h   | 同 %b                                                   |
| %j   | 一年中的第几天 (001…366)                                |
| %m   | 月份 (01…12)                                            |
| %u   | 一周中的第几天 (1…7) (1是星期一)                        |
| %U   | 一年中的第几周 (00…53) (以 Sunday 为一周的第一天的情形) |
| %w   | 一周中的第几天 (0…6)(0是星期天)                         |
| %W   | 一年中的第几周 (00…53) (以 Monday 为一周的第一天的情形) |
| %x   | 直接显示日期 (mm/dd/yy)                                 |
| %y   | 年份的最后两位数字 (00.99)                              |
| %Y   | 完整年份 (0000…9999)                                    |

### file
查看命令文件类型

```
[root@gmall opt]# file /usr/sbin/ifconfig
/usr/sbin/ifconfig: ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), dynamically linked (uses shared libs), for GNU/Linux 2.6.32, BuildID[sha1]=dff548da1b4ad9ae2afe44c9ee33c2365a7c5f8f, stripped
```

### cut

数据截取

- -c :  以字符为单位进行分割

- -d ：自定义分隔符， 默认为制表符 \t

- -s  : 与-d一起使用，如果加上-s表示只显示有分隔符的行

- -f  :  与 -d  一起使用， 指定显示哪个区域

  以 /etc/passwd 为例，演示以上命令

  ```shell
  [root@node01 ~]# cp /etc/passwd .
  
  [root@node01 ~]# cut -d: -f1 passwd # 按:分隔，取分隔后的第一列
  
  [root@node01 ~]# cut -d: -f1,3 passwd # 按:分隔，取分隔后的第1列和第3列
  
  [root@node01 ~]# cut -d: -f1-3 passwd # 按:分隔，取分隔后的第1列至第3列
  
  [root@node01 ~]# cut -d: -f1 passwd | cut -c 1 # 按:分隔，取分隔后第一列的第一个字符
  
  截取ip地址
  [root@node01 ~]# ifconfig ens33 | grep -w "inet" | cut -d" " -f10  
  192.168.235.11
  
  [root@kermit ~]# cat pass
  database:mysql:sqlserver:oracle
  country:中国:美国:other
  行业:互联网:金融
  /home/ftpuser:/bin/bash
  hello;yes;test
  
  [root@kermit ~]# cut -sd: -f1-2 pass #此处加了-s最后一行就不显示，因为它没有:符号
  database:mysql
  country:中国
  行业:互联网
  /home/ftpuser:/bin/bash
  
  [root@kermit ~]# cut -sd: -f2- pass #如果不指定尾列，表示输出从f2到最后的所有列
  mysql:sqlserver:oracle
  中国:美国:other
  互联网:金融
  /bin/bash
  ```

### tr

字符串转换，替换、删除。

- -d  删除字符串1中所有输入字符
- -s  删除所有重复出现的字符序列，只保留第一个。即将重复出现的字符压缩为一个字符。

```shell
替换语法
commands|tr 'old_string' 'new_string'

# 替换， 替换是进行等额匹配，所以只有四个字符替换成功了
[root@node01 ~]# ifconfig ens33 | grep -w "inet" | tr "inet" "new_inet"
        new_ 192.168.235.11  ew_mask 255.255.255.0  broadcas_ 192.168.235.255

# 去掉重复的空格，只保留第一个空格
[root@node01 ~]# ifconfig ens33 | grep -w "inet" | tr -s " "
 inet 192.168.235.11 netmask 255.255.255.0 broadcast 192.168.235.255

# 删掉字符 1
[root@node01 ~]# ifconfig ens33 | grep -w "inet" | tr -s " " | cut -d" " -f3 | tr -d "1"
92.68.235.

# 删掉字符 1和9
[root@node01 ~]# ifconfig ens33 | grep -w "inet" | tr -s " " | cut -d" " -f3 | tr -d "[1,9]"
2.68.235.

# 删掉字符 1至3
[root@node01 ~]# ifconfig ens33 | grep -w "inet" | tr -s " " | cut -d" " -f3 | tr -d "[1-3]"
```

### sort

排序， 将文件的每一行作为一个单位，从首字符向后，依次按ASCII码值进行比较，最后升序输出。

- -u   :  去除重复行

- -r   : 降序排列，默认是升序

- -o  : 将排序结果输出到文件中上，类似重定向符号 > 

- -n  : 以数字分隔，默认是按字符排序

- -t  : 分隔符

- -k  : 第N列

- -b  : 忽略前导空格

- -R  : 随机排序， 每次运行的结果均不同

  ```shell
  sort -n passwod # 按照数字升序排
  sort -nu passwod # 按照数字去重后升序排
  sort -n -t: -k3 passwd  # 按:分隔，对第三列进行升序排序
  sort -nr -t: -k3 passwd  # 按:分隔，对第三列进行降序排序
  sort -n passwd -o 1.txt # 将排序后的结果重定向到文件1.txt
  ```

### wc

统计文件行数

```shell
[root@gmall ~]# wc -l zlftext.txt 
4 zlftext.txt
[root@gmall ~]# cat zlftext.txt | wc -l
4
```

### ps -fe

打印进程信息

### df 

查看磁盘空间

```shell
[root@gmall opt]# df -h
Filesystem               Size  Used Avail Use% Mounted on
/dev/mapper/centos-root  198G  1.1G  197G   1% /
devtmpfs                 478M     0  478M   0% /dev
tmpfs                    489M     0  489M   0% /dev/shm
tmpfs                    489M  6.7M  482M   2% /run
tmpfs                    489M     0  489M   0% /sys/fs/cgroup
/dev/sda1                197M  103M   95M  53% /boot
tmpfs                     98M     0   98M   0% /run/user/0
```
### du

查看当前目录下所有文件所占用的磁盘空间大小

```shell
[root@gmall opt]# du -sh ./*
31M	./dubbo-admin-2.6.0.war
153M	./jdk-8u65-linux-x64.rpm
```
### stat

 查看文件元数据信息

```shell
[root@gmall opt]# stat dubbo-admin-2.6.0.war 
  File: ‘dubbo-admin-2.6.0.war’
  Size: 32089280  	Blocks: 62680      IO Block: 4096   regular file
Device: fd00h/64768d	Inode: 134218813   Links: 1
Access: (0644/-rw-r--r--)  Uid: (    0/    root)   Gid: (    0/    root)
Access: 2020-01-31 00:29:42.000000000 +0800
Modify: 2018-03-27 17:54:06.000000000 +0800
Change: 2020-01-31 13:02:16.479957803 +0800
 Birth: -
```
### fc -l 

查看历史执行命令

```shell
[root@node02 ~]# fc -l
999	 cd conf/
1000	 ll
1001	 vi zoo.cfg 
1002	 cd ../
1003	 ll
1004	 cd
1005	 zkServer.sh status
1006	 zkServer.sh start
1007	 zkServer.sh status
1008	 ./beeline.sh 
1009	 apphome
1010	 cd /opt/stanlong/presto/presto-server-0.196/
1011	 ll
1012	 bin/launcher run
1013	 cd
1014	 fl -l
```

### man

- 1：用命令(/bin, /usr/bin, /usr/local/bin)
- 2：系统调用
- 3：库用户
- 4：特殊文件（设备文件）
- 5：文件格式（配置文件的语法）
- 6：游戏
- 7：杂项（Miscellaneous）
- 8：管理命令(/sbin, /usr/sbin/, /usr/local/sbin)

### cat

+ cat ： 全量展示文件内的所有内容
+ more ： 只支持向下翻屏
+ less ： 可以来回翻屏
+ head ： 默认展示头十行
	- head -n 文件名：显示文件头n行
+ tail ： 默认展示末尾十行
	- tail -n 文件名： 显示文件末尾n行

### zip

将 /home/html/ 这个目录下所有文件和文件夹打包为当前目录下的 html.zip：

```
zip -q -r html.zip /home/html
```

### unzip

```shell
1、unzip test5.zip 解压压缩文件test5.zip到当前目录；

[root@localhost test]# unzip test5.zip
Archive:  test5.zip
  inflating: test5.txt               
[root@localhost test]# ll
-rw-r--r-- 1 root root   50 Jul 16 14:50 test5.txt
-rw-r--r-- 1 root root  216 Jul 17 14:28 test5.zip

#######################################################################################################

2、-l 表示在不解压的情况下查看压缩文件内的文件；

[root@localhost test]# unzip -l test5.zip
Archive:  test5.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
       50  07-16-2022 14:50   test5.txt
---------                     -------
       50                     1 file
       
#######################################################################################################

3、-v 表示在不解压的情况下查看压缩文件内的文件，且显示更多信息（压缩比率等）；

[root@localhost test]# unzip -v test5.zip
Archive:  test5.zip
 Length   Method    Size  Cmpr    Date    Time   CRC-32   Name
--------  ------  ------- ---- ---------- ----- --------  ----
      50  Defl:N       48   4% 07-16-2022 14:50 7fa7173c  test5.txt
--------          -------  ---                            -------
      50               48   4%                            1 file

#######################################################################################################

4、-q 表示不显示执行过程；

[root@localhost data]# unzip -q test.zip

#######################################################################################################

5、-o 表示不询问用户，覆盖原有文件；

[root@localhost data]# unzip test.zip
Archive:  test.zip
replace data/test/test1.txt? [y]es, [n]o, [A]ll, [N]one, [r]ename: ^Z
[1]+  Stopped                 unzip test.zip
[root@localhost data]# unzip -o test.zip
Archive:  test.zip
 extracting: data/test/test2.zip
 
#######################################################################################################

6、-d 表示指定文件解压缩后所要存储的目录(-d 后面必须接路径，不然会报错，且当路径不存在时会自动创建)
[root@localhost data]# unzip  -d -q /data/test/test5  test.zip
error:  must specify directory to which to extract with -d option
[root@localhost data]# unzip -q -d /data/test/test5  test.zip
[root@localhost data]# ll
-rw-r--r-- 1 root  root     0 Jul 16 12:37 test.txt
```

### jobs

查看后台任务及任务编号

### fg

将后台任何在前台展示，按Ctrl+C退出后台任务

### env

查看环境变量

### grep

- -i   忽略大小写

- -v  查找不包含指定内容的行，反向选择

- -w  按单词搜索

- -n  显示行号

- -A 显示匹配行及后面多少行 

  ```shell
  grep -A 5 'root' /etc/passwd
  ```

- -B 显示匹配行及前面多少行

### uniq

去除连续的重复行， **实现去重先要排序**

- -i  : 忽略大小写

- -c  : 统计重复行次数

- -d  : 只显示重复行

  ```shell
  sort -n 2.txt | uniq  # 按数字升序排序后去重
  
  sort -n 2.txt | uniq -c  # 按数字升序排序后去重, 并统计重复行的次数
  
  sort -n 2.txt | uniq -dc  # 按数字升序排序, 只显示重复行并统计重复行的次数
  ```

### tee

双向输出， 从标准输入读取并写入到标准输出和文件。即：双向覆盖重定向<屏幕输出|文本输入>

- -a 双向追加重定向

  ```shell
  echo hello world
  echo hello world | tee file1.txt # 将 hello world 输出到屏幕，同时输出到文件 file1.txt, 默认会覆盖原文件
  cat file1
  echo 999|tee -a file1.txt # 在文件file1.txt中追加 999
  cat file1
  ```

### paste

合并文件输出到屏幕，不会改变文件

- -d  : 自定义分隔符，默认是tab，只接受一个字符

- -s  : 将每个文件中的所有内容按照一行输出，文件中的行与行以TAB间隔

  ```
  cat a.txt
  hello
  
  cat b.txt
  hello world
  888
  999
  
  paste a.txt b.txt
  hello	hello world
  	888
  	999
  		
  		
  paste -d'@' a.txt b.txt
  hello@hello world
  @888
  @999
  
  paste -s a.txt b.txt 
  hello
  hello world	888	999
  ```

### xargs

将上一行命令的输出作为下一个命令的参数，通常和管道一起使用

- -a  file:  从文件读入作为下一个命令的参数

- -E  flag : flag必须是一个以空格分隔的标志，当xargs分析到含有flag这个标志时就停止

- -p  : 当每执行到一个 argument 时就询问一次用户

- -n  num: 后面加次数，表示命令在执行的时候一次用几个argument，默认是所有的。

- -t  :  先打印命令，然后再执行

- -i  或者 -I  :   这个参数得看linux是否支持了，将xargs的每项名称，一般是一行一行赋值给{}, 可以用 {} 代替

- -r no-run-if-empty  :  当 xargs的输入为空时则停止xargs， 不再去执行了。

- -d delim 分隔符，默认的xargs的分隔符是回车， argument 的分隔符是空格，这里修改的是xargs的分隔符

  ```shell
  cat b.txt 
  hello world
  888
  999
  
  xargs -a b.txt  # 从文件读入数据作为命令参数, 这里是按行输出的
  hello world 888 999
  
  xargs -a b.txt -d "@" # xargs 默认分隔符是换行，把分隔符替换成"@"之后换行符起作用。
  hello world
  888
  999
  
  
  xargs -a b.txt -E 999 # 从文件读入数据作为命令参数，当读到999时就不再往下读了
  hello world 888
  
  xargs -a b.txt -p # 从文件读入数据作为命令参数，读一次询问一次
  echo hello world 888 999 ?...y
  hello world 888 999
  
  xargs -a b.txt -t # 不询问， 直接输出参数
  echo hello world 888 999 
  hello world 888 999
  
  xargs -a b.txt -n 2 # 表示每次处理两个参数
  hello world
  888 999
  ```

### uptime 

```shell
[root@hadoop101 ~]# uptime
 08:47:35 up 34 days, 21:35,  7 users,  load average: 0.31, 0.41, 0.39
```

命令可以用来查看服务器已经运行了多久，当前登录的用户有多少，以及服务器在过去的1分钟、5分钟、15分钟的系统平均负载值。

### 查看定时任务

```shell
crontab -l
```

### 目录相关

```shell
dirname  : 打印上一级目录
basename ： 打印文件名

dirname /home/hadoop/test.sh 
/home/hadoop

basename /home/hadoop/test.sh 
test.sh
```

### 快捷键命令

```shell
ctrl+r    搜索历史命令
ctrl+k    删除光标后所有字符
ctrl+u    删除光标前所有字符
ctrl+l    光标移动支命令行的最后端
ctrl+a    光标移动到命令行的最前端
ctrl+z    将前台运行的程序挂起到后台
ctrl+d    退出，等价 exit
```



### vi最小化命令

按下！最小化vi并回到外部bash执行 ls -l /opt/ 命令，按enter再回到vi

```
：！ ls -l /opt/
```

### 后台运行命令

```
 1. command &  后台运行，关掉终端会停止运行
 2. nohup command &  后台运行，关掉终端也会继续运行
 3. nohup command >/dev/null 2>&1 &  后台运行，将标准错误合并到标准输出，都输出到 /dev/null
```

### 查找并替换

s 查找并替换
g 一行内全部替换
i 忽略大小写
从第一行到最后一行，查找after并替换成before

```
:1,$s/after/before/
```

### 日期格式化

```shell
vi /etc/profile # 编辑
export TIME_STYLE='+%Y/%m/%d %H:%M:%S' # 日期格式

source /etc/profile # 使环境变量生效
```

### chkconfig

服务管理

~~~shell
[root@changgou init.d]# chkconfig --list
dubbo-admin    	0:off	1:off	2:on	3:on	4:on	5:on	6:off
jexec          	0:off	1:on	2:on	3:on	4:on	5:on	6:off
netconsole     	0:off	1:off	2:off	3:off	4:off	5:off	6:off
network        	0:off	1:off	2:on	3:on	4:on	5:on	6:off
zookeeper      	0:off	1:off	2:on	3:on	4:on	5:on	6:off
[root@changgou init.d]# chkconfig --del zookeeper --删除开机启动服务
~~~
### 根目录文件说明

```
1. bin sbin：存放可执行程序
2. boot： 引导程序目录
3. dev： 设备文件目录
4. etc：配置文件目录
5. home： 普通用户的家目录
6. lib lib64： Linux 扩展库
7. media mnt： 挂载目录
8. opt： 安装第三方程序
9. var： 存放程序产生的数据文件，比如日志，数据库库文件
```

### 查看操作系统版本

```shell
# 查看系统的发行版本
[root@node01 ~]# cat /etc/redhat-release
CentOS Linux release 7.4.1708 (Core)

# 查看内核版本
[root@node01 ~]# cat /proc/version
Linux version 3.10.0-693.el7.x86_64 (builder@kbuilder.dev.centos.org) (gcc version 4.8.5 20150623 (Red Hat 4.8.5-16) (GCC) ) #1 SMP Tue Aug 22 21:09:27 UTC 2017

# 查看系统信息
[root@node01 ~]# cat /proc/version
Linux version 3.10.0-693.el7.x86_64 (builder@kbuilder.dev.centos.org) (gcc version 4.8.5 20150623 (Red Hat 4.8.5-16) (GCC) ) #1 SMP Tue Aug 22 21:09:27 UTC 2017

# 获取系统信息
[root@node01 ~]# cat /etc/os-release
NAME="CentOS Linux"
VERSION="7 (Core)"
ID="centos"
ID_LIKE="rhel fedora"
VERSION_ID="7"
PRETTY_NAME="CentOS Linux 7 (Core)"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:centos:centos:7"
HOME_URL="https://www.centos.org/"
BUG_REPORT_URL="https://bugs.centos.org/"

CENTOS_MANTISBT_PROJECT="CentOS-7"
CENTOS_MANTISBT_PROJECT_VERSION="7"
REDHAT_SUPPORT_PRODUCT="centos"
REDHAT_SUPPORT_PRODUCT_VERSION="7"

# 获取操作系统内核信息
[root@node01 ~]# uname -a
Linux node01 3.10.0-693.el7.x86_64 #1 SMP Tue Aug 22 21:09:27 UTC 2017 x86_64 x86_64 x86_64 GNU/Linux
```

### 获取运行的脚本名称

```shell
shell_name=$(echo ${0##*/}) # 使用字符串的最大匹配

# 定义变量
[root@node01 hive]# path=/opt/stanlong/hive
# 最大匹配， 删除最后一个 / 及其左边所有的内容
[root@node01 hive]# echo ${path##*/}
hive
```

### 获取运行脚本的绝对路径

```shell
run_dir=$(dirname $(readlink -f $0))

#dirname的用处是：输出已经去除了尾部的”/”字符部分的名称；如果名称中不包含”/”，则显示”.”(表示当前目录)。
#例子：
$ readlink -f deploy-small.sh
/home/centos/tmp/706/deploy-small.sh
$ dirname deploy-small.sh
.
$ dirname /home/centos/tmp/706/deploy-small.sh 
/home/centos/tmp/706

#把两个命令结合起来，就可以获取shell运行的目录
$ dirname $(readlink -f deploy-small.sh)
/home/centos/tmp/706
```

无交互修改用户密码
echo '111111'|passwd --stdin root




