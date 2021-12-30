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

