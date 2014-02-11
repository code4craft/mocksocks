package com.dianping.mocksocks.transport.socks;

import com.dianping.mocksocks.transport.Connection;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

/**
 * Replay handler with status capture.
 * 
 * @author yihua.huang@dianping.com
 */
public class ForwardHandler extends OutboundHandler implements ChannelDownstreamHandler {

	private final Connection connection;

	public ForwardHandler(Channel inboundChannel, String name, Connection connection) {
		super(inboundChannel, name);
		this.connection = connection;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		final ChannelBuffer msg = (ChannelBuffer) e.getMessage();
		connection.response(msg);
		super.messageReceived(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelClosed(ctx, e);
		connection.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
		connection.close();
	}

	public Connection getConnection() {
		return connection;
	}

	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof MessageEvent) {
			Object message = ((MessageEvent) e).getMessage();
			if (message instanceof ChannelBuffer) {
				connection.request((ChannelBuffer) message);
			}
		}
		ctx.sendDownstream(e);
	}
}
