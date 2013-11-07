MockSocks
========
> MockSocks多功能抓包和回放工具。它基于socks代理，可以拦截任何应用层协议，并做解析。此外还包括重定向、录制、回放等功能。

![proxy][1]

## 目标

* ### Java后端程序的抓包工具
	
	MockSocks最初的定位是Java后端开发时，用于调试网络连接情况，以及远程请求内容的工具。因此需要达到在不修改任何业务代码的情况下，对Java程序进行抓包，并可对请求进行重定向、回放以及直接构造响应体。

* ### 多种协议扩展

	MockSocks基于socks协议，可以拦截所有基于TCP的应用协议，包括HTTP、FTP、SMTP等公用协议，也支持mysql、mongo、memcached等私有协议，以及一些自定义的RPC协议和序列化方式。希望在程序内部以可扩展的方式，因此多种编码/解码器，从而达到对协议的支持。
	
## 安装

	curl http://192.168.7.74/devtools/install.sh | sh
	
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

# 诚征会设计的童鞋加入！

![gui][3]

目前只能查看纯文本，hessian解析会近日推出！

## 开发计划

MockSocks分为几个部分：

* ### Java客户端(完成)

	通过替换NIO SocketChannel实现，达到为NIO设置代理的效果。此部分使用javaagent达到不修改业务代码的目的，目前已开发完成，代码在`mocksocks-client`模块内。只需在程序启动时添加JVM参数即可：
	
		-javaagent:/path/to/mocksocks-client.jar -DmockFile=/path/to/mocksocks-client.jar
	
* ### Socks代理服务器(完成)

	提供Socks代理服务，保证一定性能的情况下，提供良好的扩展接口。此部分基于Netty开发，目前已开发完成，代码在`mocksocks-proxy`模块内。
	
* ### 用户界面及功能(70%)

	提供界面，让用户可以更方便的监控及修改。这部分使用swing实现，目前完成了连接显示、过滤、重定向模块。引入了h2做数据存储。完成度:40%?
	
	* ####流量录制
	  按照目标地址=>连接=>协议内容来区分。
	  
	  某些协议是无状态的，例如Http/RPC/大部分nosql，这种是不是一个连接无所谓。
	  
	  有些协议是有状态的，例如ftp/mysql，这种必须要对同一连接，并且有时间顺序。
	  
	  一些已有的解决方案：
	  
	  wiredshark是显示TCP包，并可以follow sequence。Charles/fiddler是基于HTTP，每个HTTP协议体显示一个。倾向于这一种。可以先从无状态协议入手做。
	  
	  无状态协议：
	  
	  支持单连接串行Reuquest/Response，不支持pipeline。
	 
	 ![proxy][2]

* ### 协议编码/解码(未开始)

	提供可扩展的协议编解码接口，并实现常用协议的编码/解码器。主要针对目前公司使用的hessian序列化，并提供一个可映射到Java数据结构的文本结构，以供在调试时进行编辑。目前还未开始开发。

* ### 与测试框架集成(未开始)

	MockSocks的初始动机是为测试时提供一个稳定的环境。可以在测试时，对请求内容进行抓包并持久化到本地，以后在回归测试时只需使用本地文件，大大增加测试的稳定性和减少对环境的依赖。这部分涉及到JUnit以及maven相关功能。目前还未开始开发。

  [1]: http://static.oschina.net/uploads/space/2013/1025/202527_iLkr_190591.png
  [2]: http://static.oschina.net/uploads/space/2013/1026/224012_KNGE_190591.png
  [3]: http://static.oschina.net/uploads/space/2013/1107/182714_ftTa_190591.png