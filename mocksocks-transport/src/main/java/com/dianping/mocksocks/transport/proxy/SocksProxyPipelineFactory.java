package com.dianping.mocksocks.transport.proxy;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.codec.socks.SocksInitRequestDecoder;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksProxyPipelineFactory implements ChannelPipelineFactory {

	private final ClientSocketChannelFactory cf;

	private ProxyConfig proxyConfig;

	public SocksProxyPipelineFactory(ClientSocketChannelFactory cf, ProxyConfig proxyConfig) {
		this.cf = cf;
		this.proxyConfig = proxyConfig;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast(SocksInitRequestDecoder.getName(), new SocksInitRequestDecoder());
		pipeline.addLast(SocksMessageEncoder.getName(), new SocksMessageEncoder());
		pipeline.addLast(SocksServerHandler.getName(), new SocksServerHandler(proxyConfig, cf));
		return pipeline;
	}
}
