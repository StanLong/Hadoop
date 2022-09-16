## Linux 基本命令

### cd

回到上一次所在的目录

```shell
[root@node01 stanlong]# cd -
/etc
[root@node01 etc]#
```

### type

查看命令路径

```
[root@gmall opt]# type ifconfig
ifconfig is hashed (/usr/sbin/ifconfig)
```
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

- -f :  与 -d  一起使用， 指定显示哪个区域

  以 /etc/passwd 为例，演示以上命令

  

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

### 去重

```shell
awk '!($1 in a){a[$1];print $1}'
或者
sort $1 | uniq 
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
[root@node01 ~]# cat /etc/redhat-release
CentOS Linux release 7.4.1708 (Core)

# 查看内核版本
[root@node01 ~]# cat /proc/version
Linux version 3.10.0-693.el7.x86_64 (builder@kbuilder.dev.centos.org) (gcc version 4.8.5 20150623 (Red Hat 4.8.5-16) (GCC) ) #1 SMP Tue Aug 22 21:09:27 UTC 2017
```



