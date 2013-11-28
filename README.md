MockSocks
========
> MockSocks多功能抓包和回放工具。它基于socks代理，可以拦截任何应用层协议，并做解析。此外还包括重定向、录制、回放等功能。

![proxy][1]

## 目标

* ### (Java)后端程序的抓包工具
	
	MockSocks最初的定位是Java后端开发时，用于调试网络连接情况，以及远程请求内容的工具。因此需要达到在不修改任何业务代码的情况下，对Java程序进行抓包，并可对请求进行重定向、回放以及直接构造响应体。

* ### 多种协议扩展

	MockSocks基于socks协议，可以拦截所有基于TCP的应用协议，包括HTTP、FTP、SMTP等公用协议，也支持mysql、mongo、memcached等私有协议，以及一些自定义的RPC协议和序列化方式。希望在程序内部以可扩展的方式，因此多种编码/解码器，从而达到对协议的支持。
	
## 安装

	curl http://code4craft.qiniudn.com/install.sh | [sudo] sh
	
默认安装到`/usr/local/mocksocks`目录
	
## 使用：

### 启动代理：

	java -jar /usr/local/mocksocks/mocksocks.jar

### Java程序客户端：

在VM参数中添加
	
	-javaagent:/usr/local/mocksocks/client.jar -DmockFile=/usr/local/mocksocks/client.jar

并启动。	
	
### 查看

在界面上即可看到所有活跃连接，双击连接可查看内容！

![gui][3]

### 新UI:

![gui][4]

### 进展

![schedule][5]

  [1]: http://static.oschina.net/uploads/space/2013/1025/202527_iLkr_190591.png
  [2]: http://static.oschina.net/uploads/space/2013/1026/224012_KNGE_190591.png
  [3]: http://static.oschina.net/uploads/space/2013/1107/182714_ftTa_190591.png
  [4]: http://static.oschina.net/uploads/space/2013/1117/212244_eFUQ_190591.png
  [5]: http://static.oschina.net/uploads/space/2013/1128/165612_IeBV_190591.jpeg