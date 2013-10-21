package com.dianping.mocksocks.client.socks;

import sun.nio.ch.SocksProxySocketChannel;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksProxySelectorProvider extends SelectorProvider {

	private static volatile SelectorProvider innerProvider;

	public SocksProxySelectorProvider() {
		if (innerProvider == null) {
			synchronized (SocksProxySelectorProvider.class) {
				if (innerProvider == null) {
					innerProvider = sun.nio.ch.DefaultSelectorProvider.create();
				}
			}
		}
	}

	@Override
	public DatagramChannel openDatagramChannel() throws IOException {
		return innerProvider.openDatagramChannel();
	}

	@Override
	public Pipe openPipe() throws IOException {
		return innerProvider.openPipe();
	}

	@Override
	public AbstractSelector openSelector() throws IOException {
		return innerProvider.openSelector();
	}

	@Override
	public ServerSocketChannel openServerSocketChannel() throws IOException {
		return innerProvider.openServerSocketChannel();
	}

	@Override
	public SocketChannel openSocketChannel() throws IOException {
        return new SocksProxySocketChannel(innerProvider);
	}
}
