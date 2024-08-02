# nginx

**Nginx 是一个高性能的HTTP和反向代理服务器。官方测试nginx能够支撑5万的并发链接**

**Tengin是nginx的加强版，封装版，淘宝开源**

## 集群规划

|        |                                             |
| ------ | ------------------------------------------- |
| node01 | nginx， memcached                           |
| node02 | httpd，apache-tomcat-7.0.42-windows-x64.zip |
| node03 | httpd，apache-tomcat-7.0.42-windows-x64.zip |

## 安装

参考 04软件安装

## 启停脚本

将脚本加入到环境变量中调用

```shell
#!/bin/sh 
# 
# nginx - this script starts and stops the nginx daemon 
# 
# chkconfig:   - 85 15 
# description: Nginx is an HTTP(S) server, HTTP(S) reverse \
#               proxy and IMAP/POP3 proxy server 
# processname: nginx 
# config:      /etc/nginx/nginx.conf 
# config:      /etc/sysconfig/nginx 
# pidfile:     /var/run/nginx.pid 
 
# Source function library. 
. /etc/rc.d/init.d/functions 
 
# Source networking configuration. 
. /etc/sysconfig/network 
 
# Check that networking is up. 
[ "$NETWORKING" = "no" ] && exit 0 
#根据实际安装目录修改
nginx="/opt/stanlong/tengine/sbin/nginx" 
prog=$(basename $nginx) 
#根据实际安装目录修改
NGINX_CONF_FILE="/opt/stanlong/tengine/conf/nginx.conf" 
 
[ -f /etc/sysconfig/nginx ] && . /etc/sysconfig/nginx 
 
lockfile=/var/lock/subsys/nginx 
 
start() { 
    [ -x $nginx ] || exit 5 
    [ -f $NGINX_CONF_FILE ] || exit 6 
    echo -n $"Starting $prog: " 
    daemon $nginx -c $NGINX_CONF_FILE 
    retval=$? 
    echo 
    [ $retval -eq 0 ] && touch $lockfile 
    return $retval 
} 
 
stop() { 
    echo -n $"Stopping $prog: " 
    killproc $prog -QUIT 
    retval=$? 
    echo 
    [ $retval -eq 0 ] && rm -f $lockfile 
    return $retval 
    killall -9 nginx 
} 
 
restart() { 
    configtest || return $? 
    stop 
    sleep 1 
    start 
} 
 
reload() { 
    configtest || return $? 
    echo -n $"Reloading $prog: " 
    killproc $nginx -HUP 
    RETVAL=$? 
    echo 
} 
 
force_reload() { 
    restart 
} 
 
configtest() { 
    $nginx -t -c $NGINX_CONF_FILE 
} 
 
rh_status() { 
    status $prog 
} 
 
rh_status_q() { 
    rh_status >/dev/null 2>&1 
} 
 
case "$1" in 
    start) 
        rh_status_q && exit 0 
        $1 
        ;; 
    stop) 
        rh_status_q || exit 0 
        $1 
        ;; 
    restart|configtest) 
        $1 
        ;; 
    reload) 
        rh_status_q || exit 7 
        $1 
        ;; 
    force-reload) 
        force_reload 
        ;; 
    status) 
        rh_status 
        ;; 
    condrestart|try-restart) 
        rh_status_q || exit 0 
        ;; 
    *)    
      echo $"Usage: $0 {start|stop|status|restart|condrestart|try-restart|reload|force-reload|configtest}" 
        exit 2 
esac
```

## 配置文件

```shell
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
	# 在设置了反向代理的情况下， max_clients=worker_processes * worker_connections / 4
    worker_connections  1024; 
}

# load modules compiled as Dynamic Shared Object (DSO)
#
#dso {
#    load ngx_http_fastcgi_module.so;
#    load ngx_http_rewrite_module.so;
#}

http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```

## location配置规则

```
location  = / {  
  # 只匹配"/".  
  [ configuration A ]   
}  
location  / {  
  # 匹配任何请求，因为所有请求都是以"/"开始  
  # 但是更长字符匹配或者正则表达式匹配会优先匹配  
  [ configuration B ]   
}  
location /documents/ {  
  [ configuration C ]   
}  
location ^~ /images/ {  
  # 匹配任何以 /images/ 开始的请求，并停止匹配 其它location  
  [ configuration C ]   
}  
location ~* \.(gif|jpg|jpeg)$ {  
  # 匹配以 gif, jpg, or jpeg结尾的请求.   
  # 但是所有 /images/ 目录的请求将由 [Configuration C]处理.     
  [ configuration D ]   
}  

 可以看到上面的例子中有5种不同类型的location，其中第4个带 “~” 号前缀的为需要正则匹配的location，nginx在进行url解析时对这5种不同类型的location具有不同的优先级规则，大致的规则如下：

1，字符串精确匹配到一个带 “=” 号前缀的location，则停止，且使用这个location的配置；

2，字符串匹配剩下的非正则和非特殊location，如果匹配到某个带 "^~" 前缀的location，则停止；

3，正则匹配，匹配顺序为location在配置文件中出现的顺序。如果匹配到某个正则location，则停止，并使用这个location的配置；否则，使用步骤2中得到的具有最大字符串匹配的location配置。

例如，对下面的请求有:

1， /   ->   精确匹配到第1个location，匹配停止，使用configuration A
2，/some/other/url    ->  首先前缀部分字符串匹配到了第2个location，然后进行正则匹配，显然没有匹配上，则使用第2个location的配置configurationB
3，/images /1.jpg  ->  首先前缀部分字符串匹配到了第2个location，但是接着对第3个location也前缀匹配上了，而且这时已经是配置文件里面对这个url的最大字符串匹配了，并且location带有 "^~" 前缀，则不再进行正则匹配，最终使用configuration C
4，/some/other/path/to/1.jpg  -> 首先前缀部分同样字符串匹配到了第2个location，然后进行正则匹配，这时正则匹配成功，则使用congifuration D

优先级:  = > ^~ > ~|~* > /|/dir/
```

## 配置反向代理

```shell
54         location /baidu {
55             proxy_pass https://www.baidu.com/;
56         }

```

![](./doc/04.png)

##  基于反向代理的负载均衡

```shell
36     keepalive_timeout  0; # 观察实验现象，把这行注释去掉
37     #keepalive_timeout  65;
38 
39     #gzip  on;
40 
41     upstream bula { # 设置负载均衡， bula 是随便起的名字
42         server 192.168.235.12;
43         server 192.168.235.13;
44     }
45     
46     server {
47         listen       80;
48         server_name  localhost;
49         
50         #charset koi8-r;
51         
52         #access_log  logs/host.access.log  main;
53         
54         location / {
55             root   html;
56             index  index.html index.htm;
57         }
58         
59         location /baidu {
60             proxy_pass https://www.baidu.com/; 
61         }
62         
63         location /ooxx.go {
64             proxy_pass http://bula/; # 基于反向代理的负载均衡
65         }
```

启动node02和node03上的httpd

```shell
[root@node02 ~]# service httpd start
```

观察实验现象

![](./doc/05.png)

## session一致性

**nginx+memcached+tomcat**

1. 安装tomcat

2. 配置index.jsp

   ```shell
   [root@node02 ROOT]# pwd
   /opt/stanlong/tomcat/apache-tomcat-7.0.42/webapps/ROOT
   [root@node02 ROOT]# cp index.jsp index.jsp_bak
   [root@node02 ROOT]# vi index.jsp
   # 将index.jsp 清空，写这一句
   from 192.168.235.12<br>session=<%=session.getId()%>
   ```

   启动tomcat, 观察到 session 不一致

   ![](./doc/06.png)

   ![](./doc/07.png)

3. memcached 的安装与启动

   ```shell
   [root@node01 ~]# yum install memcached -y # 安装
   [root@node01 ~]# memcached  -d -m 1024 -u root -l 192.168.235.11 -p 11211 -c 1024 -P /tmp/memcached.pid # 启动
   # 参数说明
   -d            以daemon方式运行
   -m <num>      允许最大内存用量，单位M (默认: 64 MB)
   -u <username> 绑定使用指定用于运行进程<username>
   -l <ip_addr>  绑定地址(默认:所有都允许,无论内外网或者本机更换IP，有安全隐患，若设置为127.0.0.1就只能本机访问)
   -p <num>      设置TCP端口号(默认设置为: 11211)
   -P <file>     将PID写入文件<file>，这样可以使得后边进行快速进程终止, 需要与-d 一起使用
   -U <num>      UDP监听端口(默认: 11211, 0 时关闭) 
   -c <num>      max simultaneous connections (default: 1024)
   ```

4. 配置两台tomcat上的配置文件 context.xml

   ```shell
   [root@node02 conf]# pwd
   /opt/stanlong/tomcat/apache-tomcat-7.0.42/conf
   ```

   ```xml
   <?xml version='1.0' encoding='utf-8'?>
   <!--
     Licensed to the Apache Software Foundation (ASF) under one or more
     contributor license agreements.  See the NOTICE file distributed with
     this work for additional information regarding copyright ownership.
     The ASF licenses this file to You under the Apache License, Version 2.0
     (the "License"); you may not use this file except in compliance with
     the License.  You may obtain a copy of the License at
   
         http://www.apache.org/licenses/LICENSE-2.0
   
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
   -->
   <!-- The contents of this file will be loaded for each web application -->
   <Context>
   
       <!-- Default set of monitored resources -->
       <WatchedResource>WEB-INF/web.xml</WatchedResource>
   
       <!-- Uncomment this to disable session persistence across Tomcat restarts -->
       <!--
       <Manager pathname="" />
       -->
   
       <!-- Uncomment this to enable Comet connection tacking (provides events
            on session expiration as well as webapp lifecycle) -->
       <!--
       <Valve className="org.apache.catalina.valves.CometConnectionManagerValve" />
       -->
       <!-- 将这段内容复制到context.xml中 -->
       <Manager className="de.javakaffee.web.msm.MemcachedBackupSessionManager"  
           memcachedNodes="n1:192.168.235.11:11211"  
           sticky="false"  
           sessionBackupAsync="false"  
           sessionBackupTimeout="1000"
           lockingMode="auto"  
           requestUriIgnorePattern=".*\.(ico|png|gif|jpg|css|js)$"  
           transcoderFactoryClass="de.javakaffee.web.msm.serializer.kryo.KryoTranscoderFactory" 
       />
   
   </Context>
   ```

5. 导入tomcat与memcached通信的jar包到两台tomcat 的 lib 目录下

   ```
   [root@node02 lib]# pwd
   /opt/stanlong/tomcat/apache-tomcat-7.0.42/lib
   asm-3.2.jar
   kryo-1.04.jar
   kryo-serializers-0.11.jar
   memcached-session-manager-1.7.0.jar
   memcached-session-manager-tc7-1.8.1.jar
   minlog-1.2.jar
   msm-kryo-serializer-1.7.0.jar
   reflectasm-1.01.jar
   spymemcached-2.7.3.jar
   ```

6. 配置nginx

   ```shell
    46     upstream tomcat { # 配置tomcat 负载均衡
    47         server 192.168.235.12:8080;
    48         server 192.168.235.13:8080; 
    49     }
    50 
    51     server {
    52         listen       80;
    53         server_name  localhost;
    54 
    55         #charset koi8-r;
    56 
    57         #access_log  logs/host.access.log  main;
    58 
    59         location / {
    60             root   html;
    61             index  index.html index.htm;
    62         }
    63 
    64         location /baidu {
    65             proxy_pass https://www.baidu.com/;
    66         }
    67 
    68         location /ooxx.go {
    69             proxy_pass http://bula/;
    70         }
    71 
    72         location /tomcat { 
    73             proxy_pass http://tomcat/; # tomcat 反向代理
    74         }
   ```

7. 启动nginx观察session一致性

![](./doc/08.png)

![](./doc/09.png)

