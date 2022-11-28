# 遍历文件内容

```shell
while read a 

do 

echo ${a}

done < filename

while循环中，此处a为变量，filename使用定向符将文件内容传入。

------------------------------------------------------------------------
for i in `cat filename`   

do 

echo ${i}

done

for循环中，i为变量，注意反引号。反引号的意义为：插入标准输出命令。
```

