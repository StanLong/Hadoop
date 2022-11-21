# awk整理IP地址

解析形如 : 192.168.106~109的ip地址范围

```shell
#!/bin/bash
function get_hosts_from_range()
{
        echo $1 | awk -F '~' 'BEGIN { net = ""; start = ""; end = "" }
                          {
                           split($1, array1, ".");
                           net = array1[1]  "."  array1[2] "." array1[3];
                           start = array1[4];
                           end = $2;
                           if(end <= start) { exit 1; }
                           for(i = start; i <= end; i++) { print net "." i " "; }
                          }'
}
get_hosts_from_range "$@"
```

```shell
[hadoop@hadoop101 zlf]$ bash learn.sh 192.168.6.106~109
192.168.6.106 
192.168.6.107 
192.168.6.108 
192.168.6.109
```



