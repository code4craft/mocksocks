package com.dianping.dproxy.socks;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.codec.socks.SocksCmdRequest;
import org.jboss.netty.handler.codec.socks.SocksCmdResponse;
import org.jboss.netty.handler.codec.socks.SocksMessage;

import java.net.InetSocketAddress;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksServerConnectHandler extends SimpleChannelUpstreamHandler {

	private static final String name = "SOCKS_SERVER_CONNECT_HANDLER";

	public static String getName() {
		return name;
	}

	private final ClientSocketChannelFactory cf;

	private volatile Channel outboundChannel;

	final Object trafficLock = new Object();

	public SocksServerConnectHandler(ClientSocketChannelFactory cf) {
		this.cf = cf;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof SocksCmdRequest) {
			final SocksCmdRequest socksCmdRequest = (SocksCmdRequest) e.getMessage();
			if (socksCmdRequest.getCmdType() == SocksMessage.CmdType.CONNECT) {

				final Channel inboundChannel = e.getChannel();
				inboundChannel.setReadable(false);

				// Start the connection attempt.
				ClientBootstrap cb = new ClientBootstrap(cf);

				cb.getPipeline().addLast("outboundChannel", new OutboundHandler(e.getChannel(), "out"));
				ChannelFuture f = cb
						.connect(new InetSocketAddress(socksCmdRequest.getHost(), socksCmdRequest.getPort()));

				outboundChannel = f.getChannel();
				ctx.getPipeline().addLast("outboundChannel", new OutboundHandler(outboundChannel, "in"));
                ctx.getPipeline().remove(getName());
				f.addListener(new ChannelFutureListener() {
					public void operationComplete(ChannelFuture future) throws Exception {
						if (future.isSuccess()) {
							// Connection attempt succeeded:
							// Begin to accept incoming traffic.
							inboundChannel.write(new SocksCmdResponse(SocksMessage.CmdStatus.SUCCESS, socksCmdRequest
									.getAddressType()));
							inboundChannel.setReadable(true);
						} else {
							// Close the connection if the connection attempt
							// has
							// failed.
							inboundChannel.write(new SocksCmdResponse(SocksMessage.CmdStatus.FAILURE, socksCmdRequest
                                    .getAddressType()));
							inboundChannel.close();
						}
					}
				});
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
	}

	private class OutboundHandler extends SimpleChannelUpstreamHandler {

		private final Channel inboundChannel;

		private String name;

		OutboundHandler(Channel inboundChannel, String name) {
			this.inboundChannel = inboundChannel;
			this.name = name;
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
			ChannelBuffer msg = (ChannelBuffer) e.getMessage();
			// System.out.println("<<< " + ChannelBuffers.hexDump(msg));
			synchronized (trafficLock) {
				inboundChannel.write(msg);
				// If inboundChannel is saturated, do not read until notified in
				// HexDumpProxyInboundHandler.channelInterestChanged().
				if (!inboundChannel.isWritable()) {
					e.getChannel().setReadable(false);
				}
			}
		}

		@Override
		public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			// If outboundChannel is not saturated anymore, continue accepting
			// the incoming traffic from the inboundChannel.
			synchronized (trafficLock) {
				if (e.getChannel().isWritable()) {
					inboundChannel.setReadable(true);
				}
			}
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			closeOnFlush(inboundChannel);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
			e.getCause().printStackTrace();
			closeOnFlush(e.getChannel());
		}
	}

	/**
	 * Closes the specified channel after all queued write requests are flushed.
	 */
	static void closeOnFlush(Channel ch) {
		if (ch.isConnected()) {
			ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
