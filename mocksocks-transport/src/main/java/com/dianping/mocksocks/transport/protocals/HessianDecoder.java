package com.dianping.mocksocks.transport.protocals;

import com.caucho.hessian.io.Hessian2Input;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;

public class HessianDecoder extends ReplayingDecoder<VoidEnum> {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, VoidEnum state)
			throws Exception {
        ChannelBuffer buffer1 = buffer.readBytes(buffer.readInt());
        Hessian2Input h2in = new Hessian2Input(new ChannelBufferInputStream(buffer1));
		try {
            Object o = h2in.readObject();
            return o;
		} finally {
			h2in.close();
		}
	}
}
