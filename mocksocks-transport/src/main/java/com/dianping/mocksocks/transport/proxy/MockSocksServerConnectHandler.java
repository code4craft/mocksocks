package com.dianping.mocksocks.transport.proxy;

import com.dianping.mocksocks.transport.Connection;
import com.dianping.mocksocks.transport.protocals.CodecSelector;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.socks.SocksCmdRequest;
import org.jboss.netty.handler.codec.socks.SocksCmdResponse;
import org.jboss.netty.handler.codec.socks.SocksMessage;

import java.net.InetSocketAddress;

/**
 * @author yihua.huang@dianping.com
 */
public class MockSocksServerConnectHandler extends SimpleChannelUpstreamHandler {

	private static final String name = "MOCK_SOCKS_SERVER_CONNECT_HANDLER";

	public static String getName() {
		return name;
	}

	public MockSocksServerConnectHandler() {
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		final SocksCmdRequest socksCmdRequest = (SocksCmdRequest) e.getMessage();
		final Channel inboundChannel = e.getChannel();
		inboundChannel.setReadable(false);
		final InetSocketAddress remoteAddress = new InetSocketAddress(socksCmdRequest.getHost(),
				socksCmdRequest.getPort());

		// decide protocol by port
		ChannelUpstreamHandler decoder = CodecSelector.decoder(remoteAddress, null);
		final Connection connection = new Connection();
		// client数据转发到外部server
		inboundChannel.write(new SocksCmdResponse(SocksMessage.CmdStatus.SUCCESS, socksCmdRequest.getAddressType()));
		inboundChannel.setReadable(true);
		decoder = CodecSelector.decoder(remoteAddress, null);
		if (decoder != null) {
			inboundChannel.getPipeline().addLast("decoder" + decoder.getClass().getName(), decoder);
		}
		ctx.getPipeline().remove(getName());
		// inboundChannel.getPipeline().addLast("output", new
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
	}

}
