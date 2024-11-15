# 离线安装

Grafana不联网安装grafana-zabbix插件

http://t.zoukankan.com/9527l-p-8191485.html

离线安装zabbix

https://blog.csdn.net/a648642694/article/details/107332363

https://blog.csdn.net/mmffmmm/article/details/126853949



在实际生产环境中，可能会出现服务器连接不到外网，从而没有办法去下载一些服务，此办法适用于解决各种服务及其依赖。

首先需要一个能连到外网的虚拟机或者服务器

## 一、获取zabbix源

```shell
yum -y install wget

Wget https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/x86_64/zabbix-release-5.0-1.el7.noarch.rpm

rpm -ivh zabbix-release-5.0-1.el7.noarch.rpm
```

2、修改zabbix.repo文件

看一下/etc/yum.repos.d目录下多了一个zabbix.repo

```shell
ls /etc/yum.repos.d

vim /etc/yum.repos.d/zabbix.repo

将zabbix-frontend 下面的 enabled = 0更改为 enabled = 1
```

3、修改yum源为阿里源

```shell
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

yum clean all
yum makecache fast
```

4、下载安装zabbix5.0所需的rpm包

使用yum --downloadonly 进行下载rpm包和相关依赖包

–downloaddir参数可进行设置下载保存路径

- 下载mariadb（mysql）数据库

  `yum install mariadb-server.x86_64 --downloadonly --downloaddir=/tmp/offline_rpm`

- 下载 zabbix主服务

  `yum install zabbix-server-mysql zabbix-agent --downloadonly --downloaddir=/tmp/offline_rpm`

- 下载 Red Hat Software Collections

  `yum install centos-release-scl --downloadonly --downloaddir=/tmp/offline_rpm`

- 安装 Red Hat Software Collections

  `yum -y install centos-release-scl --downloadonly --downloaddir=/tmp/offline_rpm`

- 下载 Zabbix web服务

  `yum install zabbix-web-mysql-scl zabbix-apache-conf-scl --downloadonly --downloaddir=/tmp/offline_rpm`

- 下载zabbix_get服务

  `yum install zabbix-get --downloadonly --downloaddir=/tmp/offline_rpm`

5、创建本地yum仓库

使用createrepo来创建repository仓库，如果系统里没有该命令的话需要先进行createrepo的安装 yum -y install createrepo

```shell
yum -y install createrepo

createrepo /tmp/offline_rpm

cd /tmp/offline_rpm

tar -zcf offline_rpm.tar   ./*
```

## 二、在内网中离线安装

1、准备yum仓库

将 offline_rpm.tar安装包下载下来，然后传到内网服务器中，解压，并且放到/tmp目录下。

/将etc/yum.repos.d 下面所有repo文件进行转移至bak文件夹下

移动offline_rpm.tar到/tmp目录下

tar xf offline_rpm.tar

1.1、新建一个repo源文件

将/etc/yum.repos.d 下面所有repo文件进行转移至bak文件夹下

`mv /etc/yum.repos.d/*.repo bak/`

在/etc/yum.repos.d/新建一个repo源文件

```shell
[root@localhost tmp]# vim /etc/yum.repos.d/new.repo

[New]
name=New
baseurl=file:///tmp/offline_rpm
gpgcheck=0
enabled=1


:wq保存退出
yum clean all
yum makecache
```

2、安装 Zabbix server 和 agent

`yum -y install zabbix-server-mysql zabbix-agent`

3、安装mysql数据库

`yum -y install mariadb-server.x86_64 `

4、安装RHEL Software collections（SCLs）

`yum -y install centos-release-scl`

5、安装前端组件

`yum -y install zabbix-web-mysql-scl zabbix-apache-conf-scl`

6、安装zabbix-get，调试组件

`yum -y install zabbix-get`

7、配置数据库

7.1、启动mariadb并设置开机自启

```shell
systemctl start mariadb.service
systemctl enable mariadb.service
```

7.3、设置mysql的登录密码

`mysqladmin -u root password "******"`

7.4、设置mysql用户相关的操作

修改数据库字符集

`create database zabbix character set utf8 collate utf8_bin;`

创建数据库

`create user zabbix@localhost identified by '******';`

用户授权

`grant all privileges on zabbix.* to zabbix@localhost;`

7.5、导入初始架构和数据

`cd /usr/share/doc/zabbix-server-mysql`

解压create.sql.gz

```shell
gzip -d create.sql.gz

mysql -uzabbix -p123456 zabbix <create.sql
```

之后可以进入到数据库看一下，有没有zabbix库

8、修改 zabbix-server 和 php配置文件

8.1、编辑 zabbix_server.conf

进入zabbix_server.conf配置文件中

`vim /etc/zabbix/zabbix_server.conf`

#找到对应项有注释则取消；没有的则添加；

```shell
DBHost= localhost

DBPassword = 123456
```

8.2、编辑php配置文件

#进入php的配置文件

`vim /etc/opt/rh/rh-php72/php-fpm.d/zabbix.conf`

#删除第25行，也就是最后一行；

dd就可以删除了

#更改时区为上海时区

`php_value[date.timezone] = Asia/Shanghai`

9、启动所有服务并设置开机自启

#启动zabbix-server zabbix-agent httpd rh-php72-php-fpm

`systemctl restart zabbix-server zabbix-agent rh-php72-php-fpm httpd`

#设置开机自启

`systemctl enable zabbix-server zabbix-agent httpd rh-php72-php-fpm`