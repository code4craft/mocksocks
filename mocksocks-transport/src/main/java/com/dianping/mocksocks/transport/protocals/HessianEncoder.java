package com.dianping.mocksocks.transport.protocals;

/**
 *
 */

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;


/**
 * <p>
 * Title: HessianEncoder.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 *
 * @author saber miao
 * @version 1.0
 * @created 2010-9-13 上午09:54:01
 */
public class HessianEncoder extends OneToOneEncoder {

    private static final Logger log = Logger.getLogger(SerializerFactory.class.getName());

    private final int estimatedLength = 512;

    protected static final byte[] LENGTH_PLACEHOLDER = new byte[7];

    protected void afterDo(ChannelBuffer cb){
        cb.setInt(3, cb.writerIndex() - 7);
    }

    static {
        // Eliminate Hessian SerializerFactory class's warning log, in order to cooperate with pigeon.net framework
        log.setLevel(Level.SEVERE);
    }

    public Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof ChannelBuffer){
            return msg;
        }
        ChannelBufferOutputStream bout = new ChannelBufferOutputStream(dynamicBuffer(estimatedLength, ctx.getChannel().getConfig().getBufferFactory()));
        bout.write(LENGTH_PLACEHOLDER);
        Hessian2Output h2out = new Hessian2Output(bout);
        try {
            h2out.writeObject(msg);
            h2out.flush();
        } finally {
            h2out.close();
        }
        ChannelBuffer encoded = bout.buffer();
        afterDo(encoded);
        return encoded;
    }

}
