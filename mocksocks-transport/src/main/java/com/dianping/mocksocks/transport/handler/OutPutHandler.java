package com.dianping.mocksocks.transport.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.nio.ByteBuffer;

/**
 * @author yihua.huang@dianping.com
 */
public class OutPutHandler extends SimpleChannelUpstreamHandler{

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (!(e.getMessage() instanceof ChannelBuffer)){
            System.out.println(e.getMessage());
        }
        ChannelBuffer channelBuffer = (ChannelBuffer) e.getMessage();
        ByteBuffer byteBuffer = ByteBuffer.allocate(100000);
        channelBuffer.readBytes(byteBuffer);
        super.messageReceived(ctx, e);
    }
}
