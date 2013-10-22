package com.dianping.mocksocks.proxy.protocals;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;

import java.net.InetSocketAddress;

/**
 * @author yihua.huang@dianping.com
 */
public class CodecSelector {

    public ChannelUpstreamHandler decoder(ChannelHandlerContext ctx, Channel channel, Object msg){
        InetSocketAddress remoteAddress = (InetSocketAddress)channel.getRemoteAddress();
        if (remoteAddress.getPort()>=2200&&remoteAddress.getPort()<2300){
            return new HessianDecoder();
        }
        return null;
    }

    public ChannelDownstreamHandler encoder(ChannelHandlerContext ctx, Channel channel, Object msg){
        InetSocketAddress remoteAddress = (InetSocketAddress)channel.getRemoteAddress();
        if (remoteAddress.getPort()>=2200&&remoteAddress.getPort()<2300){
            return new HessianEncoder();
        }
        return null;
    }

}
