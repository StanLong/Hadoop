# zabbix6 安装部署脚本

参考文档

https://blog.csdn.net/m0_72244695/article/details/128904111
https://blog.csdn.net/weixin_52906737/article/details/128331225

组件版本

```shell
mysql 8.0.30
nginx-1.24.0
php 7.2.34
zabbix-server 6.0
```

离线安装包下载地址

```shell
https://www.123pan.com/s/Lc1ZVv-nmCA3.html
```

安装脚本

```shell
# 安装配置mysql8
cd /opt/package/mysql
rpm -ivh mysql-community-* --force --nodeps
mkdir -p /data/mysql
chown mysql:mysql /data/mysql

cat > /etc/my.cnf <<'EOF'
[mysqld]
datadir=/data/mysql
socket=/var/lib/mysql/mysql.sock

log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid
character-set-server=utf8mb4
collation-server=utf8mb4_general_ci
port=3306
lower_case_table_names=1
max_connections=500
EOF

systemctl start mysqld

password=`cat /var/log/mysqld.log | grep password | awk '{print $NF}'`
mysql -uroot -p$password --connect-expired-password <<'EOF'
alter user 'root'@'localhost' identified by '2wsx#EDC';
set global validate_password.policy=0;
set global validate_password.length=4;
set global validate_password.mixed_case_count=0;
alter user 'root'@'localhost' identified by '123456';
flush privileges;
create database zabbix character set utf8 collate utf8_bin;
create user 'zabbix'@'localhost' identified with mysql_native_password by '123456';
grant all privileges on zabbix.* to 'zabbix'@'localhost';
flush privileges;
EOF

systemctl status mysqld

# 安装nginx
rpm -Uvh /opt/package/nginx/pcre2-10.23-2.el7.x86_64.rpm
rpm -Uvh /opt/package/nginx/nginx-1.24.0-1.el7.ngx.x86_64.rpm
mkdir /data/web
mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.bak
cat > /etc/nginx/conf.d/zabbix.conf <<'EOF'
server {
    listen       80;
    server_name  localhost;
    root         /data/web;
    location / {
        index  index.php index.html index.htm;
    }
    location ~ \.php$ {
        #root    /data/web;
        fastcgi_pass   127.0.0.1:9000;
        fastcgi_index  index.php;
        fastcgi_param  SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include        fastcgi_params;
    }
}
EOF

systemctl start nginx
systemctl status nginx

# 安装php72W
rpm -Uvh /opt/package/epel-release/epel-release-7-11.noarch.rpm
rpm -Uvh /opt/package/webtatic-release/webtatic-release.rpm
rpm -Uvh /opt/package/php/mpfr-3.1.1-4.el7.x86_64.rpm
rpm -Uvh /opt/package/php/lib*
rpm -Uvh /opt/package/php/cpp-4.8.5-44.el7.x86_64.rpm
rpm -Uvh /opt/package/php/glibc-* --force --nodeps
rpm -Uvh /opt/package/php/kernel-headers-3.10.0-1160.83.1.el7.x86_64.rpm 
rpm -Uvh /opt/package/php/php72w-*
rpm -Uvh /opt/package/php/gcc-*

sed -i 's/max_execution_time = 30/max_execution_time = 300/' /etc/php.ini
sed -i 's/max_input_time = 60/max_input_time = 300/' /etc/php.ini
sed -i 's/post_max_size = 8M/post_max_size = 16M/' /etc/php.ini

cd /data/web/
cat > index.php <<'EOF'
<?php
phpinfo();
?>
EOF

systemctl start php-fpm
systemctl status php-fpm

# 安装zabbix
groupadd zabbix
useradd -g zabbix -M -s /sbin/nologin zabbix
rpm -Uvh /opt/package/zabbix-server/yilai/* --force --nodeps
cd /opt/package/zabbix-server/zabbix-6.0.1
bash ./configure --sysconfdir=/etc/zabbix --enable-server --with-mysql --with-net-snmp --with-libxml2 --with-ssh2 --with-openipmi --with-zlib --with-libpthread --with-libevent --with-openssl --with-ldap --with-libcurl --with-libpcre

make install

sed -i 's/# ListenPort=10051/ListenPort=10051/' /etc/zabbix/zabbix_server.conf
sed -i 's/# DBHost=localhost/DBHost=localhost/' /etc/zabbix/zabbix_server.conf
sed -i 's/# DBPassword=/DBPassword=123456/' /etc/zabbix/zabbix_server.conf

grep -n '^[a-Z]' /etc/zabbix/zabbix_server.conf


mysql -uzabbix -p123456 zabbix < /opt/package/zabbix-server/zabbix-6.0.1/database/mysql/schema.sql

mysql -uzabbix -p123456 zabbix < /opt/package/zabbix-server/zabbix-6.0.1/database/mysql/images.sql

mysql -uzabbix -p123456 zabbix < /opt/package/zabbix-server/zabbix-6.0.1/database/mysql/data.sql

\cp -rp -f /opt/package/zabbix-server/zabbix-6.0.1/ui/* /data/web/

cat > /usr/lib/systemd/system/zabbix-server.service <<'EOF'
[Unit]
Description=Zabbix Server with MySQL DB
After=syslog.target network.target mysqld.service
[Service]
Type=simple
ExecStart=/usr/local/sbin/zabbix_server -f
User=zabbix
[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl start zabbix-server
systemctl status zabbix-server

systemctl enable mysqld
systemctl enable nginx
systemctl enable php-fpm
systemctl enable zabbix-server

cp /data/web/conf/zabbix.conf.php.example /data/web/conf/zabbix.conf.php
chown zabbix:zabbix /data/web/conf/zabbix.conf.php
sed -i '/PASSWORD/d' /data/web/conf/zabbix.conf.php
sed -i "/.*USER*./a\$DB[\'PASSWORD\']                 = \'123456\';" /data/web/conf/zabbix.conf.php
```

