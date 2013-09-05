package com.dianping.dproxy.socks;

import com.dianping.dproxy.connection.Transmission;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

/**
 * @author yihua.huang@dianping.com
 */
public interface Request extends Transmission {

    int readFrom(ReadableByteChannel channel) throws IOException;

}
