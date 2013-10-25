package com.dianping.mocksocks.proxy.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author yihua.huang@dianping.com
 */
public class AddressUtils {

    public static InetSocketAddress parse(String hostAndPort) throws UnknownHostException {
        String[] strings = hostAndPort.split(":");
        return new InetSocketAddress(InetAddress.getByName(strings[0]),Integer.parseInt(strings[1]));
    }
}
