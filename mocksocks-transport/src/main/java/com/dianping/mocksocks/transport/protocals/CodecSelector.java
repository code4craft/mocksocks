package com.dianping.mocksocks.transport.protocals;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author yihua.huang@dianping.com
 */
public class CodecSelector {

	public static ChannelUpstreamHandler decoder(InetSocketAddress remoteAddress, Object msg) {
		if (remoteAddress.getPort() >= 2200 && remoteAddress.getPort() < 2300) {
			return new HessianDecoder();
//            return new StringDecoder();
		}
//		if (remoteAddress.getPort() == 80) {
//			return new StringDecoder();
//		}
		return new StringDecoder();
	}

	public static ChannelDownstreamHandler encoder(InetSocketAddress remoteAddress, Object msg) {
		if (remoteAddress.getPort() >= 2200 && remoteAddress.getPort() < 2300) {
			return new HessianEncoder();
		}
        if (remoteAddress.getPort() == 80) {
            return new StringEncoder();
        }
        return new StringEncoder();
	}

}
