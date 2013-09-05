package com.dianping.dproxy.socks;

import com.dianping.dproxy.connection.Transmission;

import java.io.IOException;
import java.nio.channels.GatheringByteChannel;

/**
 * @author yihua.huang@dianping.com
 */
public interface Response extends Transmission {

    int writeTo(GatheringByteChannel channel) throws IOException;
}
