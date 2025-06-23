# Superset

# 一、superset 安装

参考地址：

https://blog.csdn.net/s609727454/article/details/148082725

https://blog.csdn.net/m0_37759590/article/details/141604851

https://blog.csdn.net/chaoPerson/article/details/141129186

安装操作系统为 CentOS7.9

miniconda 的替代软件 miniforge 下载站

https://mirrors.tuna.tsinghua.edu.cn/github-release/conda-forge/miniforge/

## 1、准备miniconda

miniconda 下载站 ， https://repo.anaconda.com/miniconda/ ， 下载 sha256值为 bfe34e1fa28d6d75a7ad05fd02fa5472275673d5f5621b77380898dee1be15d2 的安装包 https://repo.anaconda.com/miniconda/Miniconda3-4.7.12.1-Linux-x86_64.sh ， 经测试其他版本在安装时会报 GLIBC 版本不满足的问题 【version `GLIBC_2.25' not found】

## 2、安装 miniconda

```shell
bash Miniconda3-4.7.12.1-Linux-x86_64.sh
```

在安装过程中，出现以下提示时，可以指定安装路径

```shell
 [/root/miniconda3] >>> /opt/module/miniconda3
```

加载环境变量配置文件，使之生效

```shell
source ~/.bashrc
```

取消激活base环境， Miniconda安装完成后，每次打开终端都会激活其默认的base环境，我们可通过以下命令，禁止激活默认base环境

```shell
conda config --set auto_activate_base false
```

## 3、安装python3.10

(1)、配置conda国内镜像

```shell
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main
```

(2)、创建superset环境并指定Python3.10

```shell
conda create --name superset python=3.10
```

- 说明：conda环境管理常用命令
- 创建环境：conda create -n env_name
- 查看所有环境：conda info --envs
- 删除一个环境：conda remove -n env_name --all

(3)、激活superset环境

```shell
conda activate superset
```

(4)、退出当前环境

```shell
conda deactivate
```

## 4、安装superset

```
# 配置pip下载源
mkdir -p ~/.pip && touch ~/.pip/pip.conf
cat > ~/.pip/pip.conf << EOF
[global]
index-url = https://pypi.tuna.tsinghua.edu.cn/simple
[install]
trusted-host = https://pypi.tuna.tsinghua.edu.cn
EOF
```

(1)、安装依赖（之后操作均在superset环境下）

```shell
yum install -y gcc gcc-c++ libffi-devel python-devel python3-pip python3-wheel python-setuptools openssl-devel cyrus-sasl-devel openldap-devel
```

```shell
# pip 安装
conda install mysqlclient
pip install redis
```

(2)、安装（更新）setuptools和pip

```shell
pip install --upgrade setuptools pip
```

(3)、安装superset

```shell
pip install apache-superset==4.0.2
```

(4)、修改配置文件

superset_config.py 配置文件， 文件可放在任意位置

生成SECRET_KEY，`openssl rand -base64 42`

```shell
# 位置随意：/opt/software/superset/superset_config.py
# Superset specific config
ROW_LIMIT = 5000
SECRET_KEY = 'W26oCWUu49V66dfgMQ8mzMTl4oCJKnhWOZVuBXLjBAEdo3PtNkwlE3UA'
# 数据库配置
SQLALCHEMY_DATABASE_URI = 'mysql://user:passwd@127.0.0.1:3306/superset'
# 暂不清除，先关掉
WTF_CSRF_ENABLED = False
# Set this API key to enable Mapbox visualizations
MAPBOX_API_KEY = ''
# Cache配置
FILTER_STATE_CACHE_CONFIG = {
    'CACHE_TYPE': 'RedisCache',
    'CACHE_DEFAULT_TIMEOUT': 86400,
    'CACHE_KEY_PREFIX': 'superset_filter_cache',
    'CACHE_REDIS_URL': 'redis://127.0.0.1:6379/1'
}

# 界面汉化
BABEL_DEFAULT_LOCALE = "zh"
LANGUAGES = {
    "en": {"flag": "us", "name": "English"},
    "es": {"flag": "es", "name": "Spanish"},
    "it": {"flag": "it", "name": "Italian"},
    "fr": {"flag": "fr", "name": "French"},
    "zh": {"flag": "cn", "name": "简体中文"},
    "ja": {"flag": "jp", "name": "Japanese"},
    "de": {"flag": "de", "name": "German"},
    "pt": {"flag": "pt", "name": "Portuguese"},
    "pt_BR": {"flag": "br", "name": "Brazilian Portuguese"},
    "ru": {"flag": "ru", "name": "Russian"},
    "ko": {"flag": "kr", "name": "Korean"},
    "sk": {"flag": "sk", "name": "Slovak"},
    "sl": {"flag": "si", "name": "Slovenian"},
    "nl": {"flag": "nl", "name": "Dutch"},
}
```

(5)、配置环境变量

```shell
export SUPERSET_CONFIG_PATH=/opt/software/superset/superset_config.py
# 配置superset环境变量，否则找不到superset命令
export FLASK_APP=superset
# 下一步数据库初始化时遇到libstdc++.so.6相关问题，参考自：https://qiita.com/katafuchix/items/5e7c05e58213608248ae
export LD_PRELOAD=/lib64/libstdc++.so.6
```

## 5、初始化数据库

这里哪一步都有可能报错，见参考链接

(1)、在MySQL中创建superset数据库

```bash
create database superset;
```

(2)、初始化数据库

```bash
superset db upgrade
```

(3)、创建管理员用户

```c
superset fab create-admin
```

(4)、superset 初始化

```bash
superset init
```

## 6、启动和停止superset

启动superset

```shell
gunicorn --workers 5 --timeout 120 --bind 172.16.11.144:8787  "superset.app:create_app()" --daemon 
```

停止superset

```shell
 ps -ef | awk '/superset/ && !/awk/{print $2}' | xargs kill -9
```

# 二、conda迁移

https://blog.csdn.net/uncle_ll/article/details/145607690

