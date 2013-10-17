package com.dianping.mocksocks.proxy.socks;

import com.dianping.mocksocks.proxy.SocksServerHandler;
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

	public SocksProxyPipelineFactory(ClientSocketChannelFactory cf) {
		this.cf = cf;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast(SocksInitRequestDecoder.getName(),new SocksInitRequestDecoder());
        pipeline.addLast(com.dianping.mocksocks.proxy.socks.SocksMessageEncoder.getName(),new com.dianping.mocksocks.proxy.socks.SocksMessageEncoder());
        pipeline.addLast(SocksServerHandler.getName(),new SocksServerHandler(cf));
		return pipeline;
	}
}
