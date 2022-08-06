# 第1章 Zabbix入门

https://www.zabbix.com/download

## 1.1 Zabbix概述

Zabbix是一款能够监控各种网络参数以及服务器健康性和完整性的软件。Zabbix使用灵活的通知机制，允许用户为几乎任何事件配置基于邮件的告警。这样可以快速反馈服务器的问题。基于已存储的数据，Zabbix提供了出色的报告和数据可视化功能。

## 1.2 Zabbix 基础架构

![](./doc/01.png)

# 第2章 Zabbix部署

## 2.1 集群规划

| 进程          | node01节点 | node02节点 | node03节点 |
| ------------- | ---------- | ---------- | ---------- |
| zabbix-agent  | √          | √          | √          |
| zabbix-server | √          |            |            |
| MySQL         | √          |            |            |
| zabbix-web    | √          |            |            |
## 2.2 准备工作

### 2.2.1 关闭集群

如果集群开启，先关闭集群。因为安装Zabbix前，需要重启虚拟机。

### 2.2.2 关闭防火墙（3台节点，已关闭）

### 2.2.3 关闭SELinux

## 2.3 配置Zabbix yum源（3台节点）

### 2.3.1 安装yum仓库

1）安装zabbix的yum仓库

```shell
[root@node01 ~]# sudo rpm -Uvh https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/x86\_64/zabbix-release-5.0-1.el7.noarch.rpm

[root@node02 ~]# sudo rpm -Uvh https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/x86\_64/zabbix-release-5.0-1.el7.noarch.rpm

[root@node03 ~]# sudo rpm -Uvh https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/x86\_64/zabbix-release-5.0-1.el7.noarch.rpm
```

2）安装Software Collections仓库

```shell
[root@node01 ~]# sudo yum install -y centos-release-scl

[root@node02 ~]# sudo yum install -y centos-release-scl

[root@node03 ~]# sudo yum install -y centos-release-scl
```

### 2.3.2 修改zabbix仓库配置文件

node1、node02、node03三台节点，依次执行如下步骤。

**1）修改为阿里云镜像**

**（ 1）查看原始zabbix.repo文件 **

```shell
[root@node01 ~]# cat /etc/yum.repos.d/zabbix.repo
```

查看内容如下

```properties
[zabbix]
name=Zabbix Official Repository - $basearch
baseurl=http://repo.zabbix.com/zabbix/5.0/rhel/7/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591

[zabbix-frontend]
name=Zabbix Official Repository frontend - $basearch
baseurl=http://repo.zabbix.com/zabbix/5.0/rhel/7/$basearch/frontend
enabled=0
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591

[zabbix-debuginfo]
name=Zabbix Official Repository debuginfo - $basearch
baseurl=http://repo.zabbix.com/zabbix/5.0/rhel/7/$basearch/debuginfo/
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591
gpgcheck=1

[zabbix-non-supported]
name=Zabbix Official Repository non-supported - $basearch
baseurl=http://repo.zabbix.com/non-supported/rhel/7/$basearch/
enabled=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX
gpgcheck=1
```

**（ 2 ）执行以下命令完成全局替换 **

```shell
[root@node01 ~]# sed -i 's/http:\/\/repo.zabbix.com/https:\/\/mirrors.aliyun.com\/zabbix/g' /etc/yum.repos.d/zabbix.repo
```

**（ 3）查看修改之后的 zabbix.repo文件**

```shell
[root@node01 ~]# cat /etc/yum.repos.d/zabbix.repo
```

查看内容如下

```properties
[zabbix]
name=Zabbix Official Repository - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591

[zabbix-frontend]
name=Zabbix Official Repository frontend - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/$basearch/frontend
enabled=0
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591

[zabbix-debuginfo]
name=Zabbix Official Repository debuginfo - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/$basearch/debuginfo/
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591
gpgcheck=1

[zabbix-non-supported]
name=Zabbix Official Repository non-supported - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/non-supported/rhel/7/$basearch/
enabled=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX
gpgcheck=1
```

**2 ）启用 zabbix-web 仓库 **

**打开 /etc/yum.repos.d/zabbix.repo 文件，做如下修改**

```properties
[zabbix]
name=Zabbix Official Repository - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591

[zabbix-frontend]
name=Zabbix Official Repository frontend - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/$basearch/frontend
# 这里改成1， 按照规划node01对外提供web服务，因此只改node01即可
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591

[zabbix-debuginfo]
name=Zabbix Official Repository debuginfo - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/zabbix/5.0/rhel/7/$basearch/debuginfo/
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX-A14FE591
gpgcheck=1

[zabbix-non-supported]
name=Zabbix Official Repository non-supported - $basearch
baseurl=https://mirrors.aliyun.com/zabbix/non-supported/rhel/7/$basearch/
enabled=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-ZABBIX
gpgcheck=1
```

## 2.4 安装Zabbix

**在三台节点分别执行以下安装命令**

```shell
[root@node01 ~]#  yum install -y zabbix-server-mysql zabbix-agent zabbix-web-mysql-scl zabbix-apache-conf-scl

[root@node02 ~]#  sudo yum install -y zabbix-agent

[root@node03 ~]#  sudo yum install -y zabbix-agent
```

## 2.5 配置Zabbix

### 2.5.1 创建zabbix数据库

```shell
[root@node01 ~]#  mysql -uroot -proot -e "create database zabbix character set utf8 collate utf8_bin"
```

### 2.5.2 导入Zabbix建表语句

```shell
[root@node01 ~]# zcat /usr/share/doc/zabbix-server-mysql-5.0.*/create.sql.gz | mysql -uroot -proot zabbix
```

### 2.5.3 配置Zabbix\_Server（node01）

**修改 zabbix-server 配置文件**

```shell
[root@node01 ~]# sudo vi /etc/zabbix/zabbix_server.conf

DBHost=node01
DBName=zabbix
DBUser=root
DBPassword=root
```

### 2.5.4 配置Zabbix\_Agent

**修改 zabbix-agent 配置文件**

```shell
[root@node01 ~]# sudo vi /etc/zabbix/zabbix_agentd.conf
# 修改如下内容, 一个修改，两个注释：
Server=node01
#ServerActive=127.0.0.1
#Hostname=Zabbix server
```

修改完成后分发到另外两台节点上去

### 2.5.5 配置Zabbix\_Web时区

**修改 /etc/opt/rh/rh-php72/php-fpm.d/zabbix.conf 文件**

注意，是在node01上配置了Web，这个目录只能在node01上改

``` shell
[root@node01 ~]#  sudo vi /etc/opt/rh/rh-php72/php-fpm.d/zabbix.conf
```

修改如下内容：

```properties
[zabbix]
user = apache
group = apache

listen = /var/opt/rh/rh-php72/run/php-fpm/zabbix.sock
listen.acl_users = apache
listen.allowed_clients = 127.0.0.1

pm = dynamic
pm.max_children = 50
pm.start_servers = 5
pm.min_spare_servers = 5
pm.max_spare_servers = 35

php_value[session.save_handler] = files
php_value[session.save_path]    = /var/opt/rh/rh-php72/lib/php/session/

php_value[max_execution_time] = 300
php_value[memory_limit] = 128M
php_value[post_max_size] = 16M
php_value[upload_max_filesize] = 2M
php_value[max_input_time] = 300
php_value[max_input_vars] = 10000
php_value[date.timezone] = Asia/Shanghai
```

## 2.6 启动停止Zabbix

### 2.6.1 启动Zabbix

```shell
[root@node01 ~]# sudo systemctl start zabbix-server zabbix-agent httpd rh-php72-php-fpm
[root@node01 ~]# sudo systemctl enable zabbix-server zabbix-agent httpd rh-php72-php-fpm

[root@node02 ~]# sudo systemctl start zabbix-agent
[root@node02 ~]# sudo systemctl enable zabbix-agent

[root@node03 ~]# sudo systemctl start zabbix-agent
[root@node03 ~]# sudo systemctl enable zabbix-agent
```

### 2.6.2 停止Zabbix

```shell
[root@node01 ~]# sudo systemctl stop zabbix-server zabbix-agent httpd rh-php72-php-fpm
[root@node01 ~]# sudo systemctl disable zabbix-server zabbix-agent httpd rh-php72-php-fpm

[root@node02 ~]# sudo systemctl stop zabbix-agent
[root@node02 ~]# sudo systemctl disable zabbix-agent

[root@node03 ~]# sudo systemctl stop zabbix-agent
[root@node03 ~]# sudo systemctl disable zabbix-agent
```

### 2.6.3 连接Zabbix\_Web数据库

**1 ）浏览器访问 http://node01/zabbix/**

![](./doc/02.png)

**2 ）检查配置**

![](./doc/03.png)

**3 ）配置数据库**

![](./doc/04.png)

**4 ）配置 zabbix-server**

![](./doc/05.png)

![](./doc/06.png)

![](./doc/07.png)

### 2.6.4 登录Zabbix

1）用户名密码如下

用户名：Admin 密码：zabbix

![](/doc/08.png)

2）点击User Settings，语言设成中文

![](./doc/09.png)

![](./doc/10.png)

![](./doc/11.png)

# 第3章 Zabbix使用

## 3.1 Zabbix术语

### 3.1.1 主机（Host）

一台你想监控的网络设备，用IP或域名表示。

### 3.1.2 监控项（Item）

你想要接收的主机的特定数据，一个度量数据。

### 3.1.3 触发器（Trigger）

一个被用于定义问题阈值和&quot;评估&quot;监控项接收到的数据的逻辑表达式。

### 3.1.4 动作（Action）

一个对事件做出反应的预定义的操作，比如邮件通知。

## 3.2 Zabbix实战

### 3.2.1 创建Host

**1 ）点击配置 / 主机 / 创建主机**

![](RackMultipart20220806-1-sbbs8l_html_1e57a43fc846bd4d.png)

**2 ）配置主机（ Host ）**

![](RackMultipart20220806-1-sbbs8l_html_9c51350318723e21.png)

**3 ）查看新增（ Host ）**

![](RackMultipart20220806-1-sbbs8l_html_f3c6a8fa5b9b0a.png)

**4 ）重复以上步骤，再创建 hadoop103 、 hadoop104 主机。**

![](RackMultipart20220806-1-sbbs8l_html_7033558f79119220.png)

### 3.2.2 创建监控项（Items）

**1**** ）点击监控项（ ****Items**** ）**

![](RackMultipart20220806-1-sbbs8l_html_a80b8b81e43acbbf.png)

**2**** ）点击创建监控项（ ****Create Item**** ）**

![](RackMultipart20220806-1-sbbs8l_html_d3c6d93e6f690ab1.png)

**3**** ）配置监控项（ ****Item**** ）**

![](RackMultipart20220806-1-sbbs8l_html_bc8d88ea4116ff57.png)

**4**** ）查看创建的监控项**

![](RackMultipart20220806-1-sbbs8l_html_2c6a9f8a609c3a25.png)

**5**** ）查看监控项最新数据**

![](RackMultipart20220806-1-sbbs8l_html_2336a8969b6c5872.png)

### 3.2.3 创建触发器（Trigger）

**1**** ）点击配置 ****/**** 主机 ****/**** 触发器**

![](RackMultipart20220806-1-sbbs8l_html_5ff776ef5646705d.png)

**2**** ）点击创建触发器**

![](RackMultipart20220806-1-sbbs8l_html_2e42f5b0f71268d6.png)

**3**** ）编辑触发器**

![](RackMultipart20220806-1-sbbs8l_html_c0569a7445c907c.png)

![](RackMultipart20220806-1-sbbs8l_html_213de3baeee690fd.png)

![](RackMultipart20220806-1-sbbs8l_html_79fc821130f9f450.png)

### 3.2.4 创建动作（Action）

**1**** ）点击配置 ****/**** 动作 ****/**** 创建动作**

![](RackMultipart20220806-1-sbbs8l_html_22418338cbdf8261.png)

**2**** ）编辑动作（ ****Action**** ）**

![](RackMultipart20220806-1-sbbs8l_html_cd2a0f9392eb6ade.png)

![](RackMultipart20220806-1-sbbs8l_html_ecc03a1bcbc9a9dc.png)

![](RackMultipart20220806-1-sbbs8l_html_3294943ad930ff55.png)

![](RackMultipart20220806-1-sbbs8l_html_230776aef2bf09d3.png)

![](RackMultipart20220806-1-sbbs8l_html_be44350aef3fb515.png)

![](RackMultipart20220806-1-sbbs8l_html_77296f5fc1cdc81f.png)

![](RackMultipart20220806-1-sbbs8l_html_dd1f09521452f623.png)

### 3.2.5 申请邮箱

邮件报警，可以采用126、163、qq等邮箱。下面以126邮箱为例。

1）申请注册一个126邮箱

2）点击邮箱账号=》账号管理

![](RackMultipart20220806-1-sbbs8l_html_74e796c894b2e6bf.png)

3）开启SMTP服务

![](RackMultipart20220806-1-sbbs8l_html_698cce18bfe14f97.png)

4）一定要记住授权码

![](RackMultipart20220806-1-sbbs8l_html_f001f67de46cdfd8.png)

### 3.2.6 创建报警媒介类型（Media type）

**1**** ）点击管理 ****/**** 报警媒介类型 ****/Email**

![](RackMultipart20220806-1-sbbs8l_html_13dc1c7ee1b9e960.png)

**2**** ）编辑 ****Email**

![](RackMultipart20220806-1-sbbs8l_html_1cda50f7c26fc8dc.png)

**3**** ）测试 ****Email**

![](RackMultipart20220806-1-sbbs8l_html_d44d6b10615c9b99.png)

**4**** ） ****Email**** 绑定收件人**

![](RackMultipart20220806-1-sbbs8l_html_665f292fb7d70b37.png)

![](RackMultipart20220806-1-sbbs8l_html_1c587b83dd6fad07.png)

![](RackMultipart20220806-1-sbbs8l_html_189e07d4d4c1a51c.png)

![](RackMultipart20220806-1-sbbs8l_html_8af8c7f92bcf761a.png)

### 3.2.7 测试

**1**** ）关闭集群中的 ****HDFS**** ，会有如下效果**

[atguigu@hadoop102 hadoop-3.1.3]$ sbin/stop-dfs.sh

**2**** ）查看仪表盘故障信息**

![](RackMultipart20220806-1-sbbs8l_html_a8783cff441ab9d0.png)

**3**** ）查看邮件，接收故障信息**

![](RackMultipart20220806-1-sbbs8l_html_7c09541ea4abf321.png)

## 3.3 创建模板

**1**** ）点击配置 ****/**** 模板 ****/**** 创建模板**

![](RackMultipart20220806-1-sbbs8l_html_ee01f3e2d2f66e17.png)

**2**** ）配置模板名称 ****/**** 群组**

![](RackMultipart20220806-1-sbbs8l_html_bc08bf8f2a8454d.png)

**3**** ）配置监控项**

![](RackMultipart20220806-1-sbbs8l_html_d03ee13370a4f1c4.png)

![](RackMultipart20220806-1-sbbs8l_html_abfc823f425b5199.png)

![](RackMultipart20220806-1-sbbs8l_html_8cf77e62bb6ee641.png)

**4**** ）配置触发器**

![](RackMultipart20220806-1-sbbs8l_html_ebdd87f654bd4ac6.png)

![](RackMultipart20220806-1-sbbs8l_html_cfb5163852ebf719.png)

**5**** ）配置动作**

![](RackMultipart20220806-1-sbbs8l_html_c0bbf55372227bff.png)

![](RackMultipart20220806-1-sbbs8l_html_868ebe679e847b7c.png)

![](RackMultipart20220806-1-sbbs8l_html_f209688c6a3d3b8f.png)

![](RackMultipart20220806-1-sbbs8l_html_e6ef8f6fab5ef0d4.png)

**6**** ）为 ****hadoop103**** 应用模板**

![](RackMultipart20220806-1-sbbs8l_html_a7492b0f3379599.png)

![](RackMultipart20220806-1-sbbs8l_html_72588602513d1048.png)

**7**** ）为 ****hadoop104**** 应用模板**

**同上**

**8**** ）测试**

**（**** 1 ****）启动**** hadoop ****集群**

[atguigu@hadoop102 hadoop-3.1.3]$ sbin/start-dfs.sh

**（**** 2 ****）停止**** hadoop ****集群**

[atguigu@hadoop102 hadoop-3.1.3]$ sbin/stop-dfs.sh

**（**** 3 ****）查看报警邮件**

# 第4章 集成Grafana

## 4.1 部署Grafana

1.下载Grafana安装包

官方仓库：[https://dl.grafana.com/oss/release/grafana-7.4.3-1.x86\_64.rpm](https://dl.grafana.com/oss/release/grafana-7.4.3-1.x86_64.rpm)

国内镜像：[https://repo.huaweicloud.com/grafana/7.4.3/grafana-7.4.3-1.x86\_64.rpm](https://repo.huaweicloud.com/grafana/7.4.3/grafana-7.4.3-1.x86_64.rpm)

2.将安装包上传到hadoop102主机的/opt/software路径

3.使用rpm安装Grafana

[atguigu@hadoop102 software]$ sudo rpm -ivh grafana-7.4.3-1.x86\_64.rpm

4.启动Grafana

[atguigu@hadoop102 software]$ sudo systemctl start grafana-server

5.访问Grafana页面

访问地址为：[http://hadoop102:3000/](http://hadoop102:3000/)

首次登录用户名和密码均为 admin

![](RackMultipart20220806-1-sbbs8l_html_c8ee4ac22d4c003c.png)

设置新密码或者跳过

![](RackMultipart20220806-1-sbbs8l_html_4ea30a7a3884614b.png)

## 4.2 快速入门

1.创建Dashboard

![](RackMultipart20220806-1-sbbs8l_html_4029f06dc5249aec.png)

2.新建panel

![](RackMultipart20220806-1-sbbs8l_html_73049ce3d1f7b4a2.png)

3.选择数据源，此处选择Grafana提供的随机测试数据

![](RackMultipart20220806-1-sbbs8l_html_207949e8797e773f.png)

4.选择合适的可视化类型

![](RackMultipart20220806-1-sbbs8l_html_929c9ffde0f29db1.png)

5.保存Dashboard和panel，并命名

![](RackMultipart20220806-1-sbbs8l_html_ab24d2ac524ee433.png)

## 4.3 集成Zabbix

### 4.3.1 配置数据源

使用Grafana与其他系统集成时，需要配置对应的DataSource

1.点击下图所示按钮，增加数据源

![](RackMultipart20220806-1-sbbs8l_html_8979ec8c540018b.png)

2.选择所需数据源

![](RackMultipart20220806-1-sbbs8l_html_8fe70a18a3301d9b.png)

3.如没有所需数据源，点击下图所示地址，获取更多数据源

![](RackMultipart20220806-1-sbbs8l_html_daa85d663937bdd7.png)

4.搜索zabbix，并点击搜索结果

![](RackMultipart20220806-1-sbbs8l_html_c39ee5c2f2f6ef20.png)

5.按照所需插件的说明进行部署

![](RackMultipart20220806-1-sbbs8l_html_e9b8cc6ef10f0936.png)

6.以下是插件部署步骤

1）安装插件

[atguigu@hadoop102 software]$ sudo grafana-cli plugins install alexanderzobnin-zabbix-app

2）重启Grafana

[atguigu@hadoop102 software]$ sudo systemctl restart grafana-server

3）启用插件

（1）点击设置/插件

![](RackMultipart20220806-1-sbbs8l_html_8b4d1cf1dcf1307d.png)

（2）搜索zabbix，并点击搜索结果

![](RackMultipart20220806-1-sbbs8l_html_68a00031f6223854.png)

（3）启用Zabbix插件

![](RackMultipart20220806-1-sbbs8l_html_fc6fa6f1c81db1b1.png)

7.配置zabbix数据源

1）新增数据源

![](RackMultipart20220806-1-sbbs8l_html_6a01d5ecd7119393.png)

2）搜索zabbix，并点击搜索结果

![](RackMultipart20220806-1-sbbs8l_html_21105f9647f9e977.png)

3）配置数据源

![](RackMultipart20220806-1-sbbs8l_html_608b0a280cf77c5e.png)

### 4.3.2 集成案例

1.为方便展示效果，在Zabbix中为hadoop102主机应用一个Zabbix内置的Linux系统监控模板。

1）找到hadoop102主机

![](RackMultipart20220806-1-sbbs8l_html_db2749f3a5ba53b0.png)

2）点击模板，搜索linux，并选择Template OS Linux by Zabbix agent

![](RackMultipart20220806-1-sbbs8l_html_378359de998af9c1.png)

3）点击更新

![](RackMultipart20220806-1-sbbs8l_html_fd769bdaee24a5d2.png)

2.集成Grafana，展示模板中的系统监控项

1）点击Dashboards，找到前边创建的Test看板

![](RackMultipart20220806-1-sbbs8l_html_130dd2fe3bb50c80.png)

2）新建panel

![](RackMultipart20220806-1-sbbs8l_html_65ec1f57bba6633f.png)

3）选择Zabbix数据源

![](RackMultipart20220806-1-sbbs8l_html_981ff048cd3781fa.png)

4）选择要展示的监控项

![](RackMultipart20220806-1-sbbs8l_html_fbeb09eadc1b4fdb.png)

5）选择合适的图标类型

![](RackMultipart20220806-1-sbbs8l_html_3caa5041a373d1b6.png)

6）保存配置

![](RackMultipart20220806-1-sbbs8l_html_2f1f50e523e437fb.png)

更多Java –大数据 –前端 –python人工智能资料下载，可百度访问：尚硅谷官网

