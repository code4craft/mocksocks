package com.dianping.mocksocks.proxy.forward;

import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;
import com.dianping.mocksocks.proxy.protocals.CodecSelector;
import org.jboss.netty.bootstrap.ClientBootstrap;
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

	public SocksServerConnectHandler(ClientSocketChannelFactory cf) {
		this.cf = cf;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		final SocksCmdRequest socksCmdRequest = (SocksCmdRequest) e.getMessage();
		final Channel inboundChannel = e.getChannel();
		inboundChannel.setReadable(false);

		// Start the connection attempt.
		final ClientBootstrap cb = new ClientBootstrap(cf);
		cb.setOption("keepAlive", true);
		cb.setOption("tcpNoDelay", true);
		final InetSocketAddress remoteAddress = new InetSocketAddress(socksCmdRequest.getHost(),
				socksCmdRequest.getPort());
        final ConnectionStatus connectionStatus = new ConnectionStatus();
        cb.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                // 外部server数据转发到client
                ForwardHandler handler = new ForwardHandler(inboundChannel, socksCmdRequest.getHost()
                        + ":" + socksCmdRequest.getPort() + "<<<", connectionStatus);
                pipeline.addLast(ForwardHandler.class.getName(), handler);
                return pipeline;
            }
        });

        ChannelFuture f = cb.connect(remoteAddress);
        outboundChannel = f.getChannel();
        connectionStatus.setChannel(outboundChannel);
        ConnectionMonitor.getInstance().putStatus(outboundChannel,connectionStatus);
        // System.out.println("connect to " + socksCmdRequest.getHost() + " : "
		// + socksCmdRequest.getPort());


		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
                    connectionStatus.start();
					// decide protocol by port
					ChannelUpstreamHandler decoder = CodecSelector.decoder(remoteAddress, null);
					if (decoder != null) {
						outboundChannel.getPipeline().addLast("decoder" + decoder.getClass().getName(), decoder);
					}
					// outboundChannel.getPipeline().addLast("output", new
					// OutPutHandler());

					// client数据转发到外部server
					inboundChannel.getPipeline().addLast(
							"inboundChannel",
							new OutboundHandler(outboundChannel, socksCmdRequest.getHost() + " : "
									+ socksCmdRequest.getPort() + ">>>"));
					inboundChannel.write(new SocksCmdResponse(SocksMessage.CmdStatus.SUCCESS, socksCmdRequest
							.getAddressType()));
					inboundChannel.setReadable(true);
					decoder = CodecSelector.decoder(remoteAddress, null);
					if (decoder != null) {
						inboundChannel.getPipeline().addLast("decoder" + decoder.getClass().getName(), decoder);
					}
					ctx.getPipeline().remove(getName());
					// inboundChannel.getPipeline().addLast("output", new
					// OutPutHandler());
				} else {
                    connectionStatus.setStatus(ConnectionStatus.FAIL);
					inboundChannel.write(new SocksCmdResponse(SocksMessage.CmdStatus.FAILURE, socksCmdRequest
							.getAddressType()));
					inboundChannel.close();
					ctx.getPipeline().remove(getName());
				}
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
	}

}
