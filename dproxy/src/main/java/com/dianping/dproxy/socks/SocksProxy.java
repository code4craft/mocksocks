package com.dianping.dproxy.socks;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksProxy {

    public void run() {

        // Configure the bootstrap.
        Executor executor = Executors.newCachedThreadPool();
        ServerBootstrap sb = new ServerBootstrap(
                new NioServerSocketChannelFactory(executor, executor));

        // Set up the event pipeline factory.
        ClientSocketChannelFactory cf =
                new NioClientSocketChannelFactory(executor, executor);

        sb.setPipelineFactory(
                new SocksProxyPipelineFactory(cf));

        // Start up the server.
        sb.bind(new InetSocketAddress(40310));
    }

    public static void main(String[] args) {
        new SocksProxy().run();
    }
}
