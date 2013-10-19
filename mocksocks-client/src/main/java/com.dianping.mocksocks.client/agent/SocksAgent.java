package com.dianping.mocksocks.client.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksAgent {

    public static void premain(String agentArgs, Instrumentation inst){
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "13720");
        System.setProperty("java.nio.channels.spi.SelectorProvider", "com.dianping.mocksocks.client.proxy.SocksProxySelectorProvider");
        //fot nio
        inst.addTransformer(new SocksChannelTransformer());
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception
    {
        premain(args, inst);
    }
}
