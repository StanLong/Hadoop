# 集群环境信息

- 操作系统版本：CentOS 7.4

- 内存：物理内存16G。

  共虚拟机四个节点

  - node01 8G、 node02 4G、 node03 2G、 node04 2G。每个节点交换分区大小16G

- 硬盘：各虚拟机节点硬盘大小200G

https://zaixianwangyebianji.bmcx.com/

各应用环境变量都配置在 ~/.bashrc 下

<table style="width:100%;" cellpadding="2" cellspacing="0" border="1" bordercolor="#000000">
	<tbody>
		<tr>
			<td>
				<span><span><span><span><span><span>组件</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>node01</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>node02</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>node03</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>node04</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td rowspan="8">
				<span><span><span><span><span><span>hadoop3.4.0<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>NN(主)<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>N</span><span>N</span><span>(</span><span>备</span><span>)</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>DN<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>DN<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>D</span><span>N</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>ZK<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>Z</span><span>K</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>Z</span><span>K</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span>ZKFC<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>Z</span><span>K</span><span>F</span><span>C</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><span>J</span><span>N</span><span>N</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>JNN<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>JNN<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>R</span><span>S</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>R</span><span>S</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>NM<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>NM<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>N</span><span>M</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<br />
			</td>
			<td>
				<br />
			</td>
			<td>
				JobHistory
			</td>
			<td>
				<br />
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span>apache-hive-3.1.3-bin<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>服务端</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>客户端</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span>kafka_2.13-2.5.0，2.13是scala的版本号，2.5.0是kafka的版本号<span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>kafka<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>kafka<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span>spark-3.0.0<span id="__kindeditor_bookmark_start_22__"></span></span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span>YARN模式</span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span>Java&nbsp;1.8.0_331</span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span>java</span></span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>java</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>java</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>java</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span>mysql 5.7</span></span></span></span></span></span> 
			</td>
			<td>
				<span><span><span><span><span><span>Server/Client</span></span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span style="background-color:#FFFFFF;">netcat&nbsp;7.50</span><span></span></span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>nc</span></span></span></span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span>zookeeper-3.4.11<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				zk
			</td>
			<td>
				<span><span><span><span><span><span>zk</span></span></span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span>zk<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span>apache-flume-1.9.0<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span id="__kindeditor_bookmark_start_134__"></span>flume 采集<span id="__kindeditor_bookmark_end_135__"></span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>flume消费</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span>sqoop-1.4.7.bin__hadoop-2.6.0<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span>sqoop</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<br />
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span>hbase-1.3.6<br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#FFFFFF;">HMaster(主)</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;"><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#FFFFFF;">H</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#FFFFFF;">Master(主)</span></span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">HRegionServer</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;"></span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">HRegio</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">n</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">S</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">e</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">r</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">v</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">e</span><span style="color:#333333;font-family:&quot;font-size:16px;background-color:#F8F8F8;">r</span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
		<tr>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
			<td>
				<span><span><span><span><span><span><br />
</span> </span> </span> </span> </span> </span> 
			</td>
		</tr>
	</tbody>
</table>