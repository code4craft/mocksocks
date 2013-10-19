package com.dianping.mocksocks.proxy.socks;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.codec.socks.SocksCmdRequest;
import org.jboss.netty.handler.codec.socks.SocksCmdResponse;
import org.jboss.netty.handler.codec.socks.SocksMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksServerConnectHandler extends SimpleChannelUpstreamHandler {

	private static final String name = "SOCKS_SERVER_CONNECT_HANDLER";

	public static String getName() {
		return name;
	}

    private static PrintWriter printWriter;

	private final ClientSocketChannelFactory cf;

	private volatile Channel outboundChannel;

	final Object trafficLock = new Object();

    static {
        try {
            printWriter = new PrintWriter(new FileWriter("/data/socks/c.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public SocksServerConnectHandler(ClientSocketChannelFactory cf) {
		this.cf = cf;
    }

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		final SocksCmdRequest socksCmdRequest = (SocksCmdRequest) e.getMessage();
		final Channel inboundChannel = e.getChannel();
		inboundChannel.setReadable(false);

		// Start the connection attempt.
		final ClientBootstrap cb = new ClientBootstrap(cf);
		cb.setOption("keepAlive", true);
		cb.setOption("tcpNoDelay", true);
		cb.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				// 外部server数据转发到client
				pipeline.addLast("outboundChannel", new OutboundHandler(inboundChannel, socksCmdRequest.getHost()
						+ " : " + socksCmdRequest.getPort() + "<<<"));
				return pipeline;
			}
		});

		ChannelFuture f = cb.connect(new InetSocketAddress(socksCmdRequest.getHost(), socksCmdRequest.getPort()));
        System.out.println("connect to "+socksCmdRequest.getHost()+" : "+socksCmdRequest.getPort());

		outboundChannel = f.getChannel();
		ctx.getPipeline().remove(getName());
		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					// client数据转发到外部server
					inboundChannel.getPipeline().addLast("inboundChannel", new OutboundHandler(outboundChannel, socksCmdRequest.getHost()+" : "+socksCmdRequest.getPort()+">>>"));
					inboundChannel.write(new SocksCmdResponse(SocksMessage.CmdStatus.SUCCESS, socksCmdRequest
							.getAddressType()));
					inboundChannel.setReadable(true);
				} else {
					inboundChannel.write(new SocksCmdResponse(SocksMessage.CmdStatus.FAILURE, socksCmdRequest
							.getAddressType()));
					inboundChannel.close();
				}
			}
		});
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
			final ChannelBuffer msg = (ChannelBuffer) e.getMessage();
			synchronized (trafficLock) {
				inboundChannel.write(msg);
                printWriter.println(name + ChannelBuffers.hexDump(msg));
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
