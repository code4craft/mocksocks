package com.dianping.mocksocks.proxy.socks;

import com.dianping.mocksocks.proxy.Proxy;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksProxy implements Proxy {

	private ServerBootstrap sb;

	private AtomicBoolean running = new AtomicBoolean(false);

	public void run() {

		// Configure the bootstrap.
		Executor executor = Executors.newCachedThreadPool();
		Executor executorWorker = Executors.newCachedThreadPool();
		sb = new ServerBootstrap(new NioServerSocketChannelFactory(executor, executorWorker));

		// Set up the event pipeline factory.
		ClientSocketChannelFactory cf = new NioClientSocketChannelFactory(executor, executorWorker);

		sb.setPipelineFactory(new SocksProxyPipelineFactory(cf));

		// Start up the server.
		sb.bind(new InetSocketAddress(13721));
	}

	public static void main(String[] args) {
		new SocksProxy().run();
	}

	@Override
	public void start() {
		if (running.compareAndSet(false, true)) {
			try {
				run();
			} catch (RuntimeException e) {
				running.compareAndSet(true, false);
				throw e;
			}
		}
	}

	@Override
	public void stop() {
		if (running.compareAndSet(true, false)) {
			sb.shutdown();
		}
	}

	@Override
	public void loadCache(String cacheFile) {

	}

	@Override
	public boolean isRunning() {
		return running.get();
	}
}
