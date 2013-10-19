package com.dianping.mocksocks.client.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksProxySocketChannel extends SocketChannel {

	public SocksProxySocketChannel(SelectorProvider provider, SocketChannel innerSocketChannel) {
		super(provider);
		this.innerSocketChannel = innerSocketChannel;
	}

	private SocketChannel innerSocketChannel;

	private SocketAddress remote;

	@Override
	public Socket socket() {
		return null;
	}

	@Override
	public boolean isConnected() {
		return innerSocketChannel.isConnected();
	}

	@Override
	public boolean isConnectionPending() {
		return innerSocketChannel.isConnectionPending();
	}

	@Override
	public boolean connect(SocketAddress remote) throws IOException {
		this.remote = remote;
        System.out.println("connect to "+remote);
        SocketAddress socksProxy = getSocksProxy();
        if (socksProxy == null) {
			return innerSocketChannel.connect(remote);
		} else {
            innerSocketChannel.connect(socksProxy);
            System.out.println("connect to "+remote);
            //SEND REQUEST
            return true;
		}
	}

	protected SocketAddress getSocksProxy() {
		SocketAddress proxySocketAddress;
		String host = System.getProperty("socksProxyHost");
		String port = System.getProperty("socksProxyPort");
		if (host == null || port == null) {
			return null;
		}
		try {
			proxySocketAddress = new InetSocketAddress(host, Integer.parseInt(port));
		} catch (Exception e) {
			return null;
		}
		return proxySocketAddress;
	}

	@Override
	public boolean finishConnect() throws IOException {
		return innerSocketChannel.finishConnect();
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		return innerSocketChannel.read(dst);
	}

	@Override
	public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
		return innerSocketChannel.read(dsts, offset, length);
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		return innerSocketChannel.write(src);
	}

	@Override
	public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
		return innerSocketChannel.write(srcs, offset, length);
	}

	@Override
	protected void implCloseSelectableChannel() throws IOException {
		// TODO
	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {
		// TODO
	}
}
