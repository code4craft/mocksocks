package com.dianping.mocksocks.proxy.forward;

import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

/**
 * Replay handler with status capture.
 * 
 * @author yihua.huang@dianping.com
 */
public class ForwardHandler extends OutboundHandler implements ChannelDownstreamHandler {

	private final ConnectionStatus connectionStatus;

	public ForwardHandler(Channel inboundChannel, String name, ConnectionStatus connectionStatus) {
		super(inboundChannel, name);
		this.connectionStatus = connectionStatus;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		final ChannelBuffer msg = (ChannelBuffer) e.getMessage();
		connectionStatus.addBytesReceive(msg.readableBytes());
		super.messageReceived(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelClosed(ctx, e);
		connectionStatus.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
		connectionStatus.close();
	}

	public ConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}

	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof MessageEvent) {
			Object message = ((MessageEvent) e).getMessage();
			if (message instanceof ChannelBuffer) {
				connectionStatus.addBytesSend(((ChannelBuffer) message).readableBytes());
			}
		}
        ctx.sendDownstream(e);
	}
}
