package com.dianping.mocksocks.client.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
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
		return new SocketWrapper(innerSocketChannel.socket(), remote);
	}

	private static class SocketWrapper extends Socket {

		private Socket innerSocket;

		private SocketAddress remoteAddress;

		public SocketWrapper(Socket innerSocket, SocketAddress remoteAddress) {
			this.innerSocket = innerSocket;
			this.remoteAddress = remoteAddress;
		}

		@Override
		public void connect(SocketAddress endpoint, int timeout) throws IOException {
			innerSocket.connect(endpoint, timeout);
		}

		@Override
		public void bind(SocketAddress bindpoint) throws IOException {
			innerSocket.bind(bindpoint);
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return innerSocket.getOutputStream();
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return innerSocket.getInputStream();
		}

		@Override
		public boolean getKeepAlive() throws SocketException {
			return innerSocket.getKeepAlive();
		}

		@Override
		public boolean getOOBInline() throws SocketException {
			return innerSocket.getOOBInline();
		}

		@Override
		public boolean getReuseAddress() throws SocketException {
			return innerSocket.getReuseAddress();
		}

		@Override
		public boolean getTcpNoDelay() throws SocketException {
			return innerSocket.getTcpNoDelay();
		}

		@Override
		public InetAddress getLocalAddress() {
			return innerSocket.getLocalAddress();
		}

		@Override
		public int getLocalPort() {
			return innerSocket.getLocalPort();
		}

		@Override
		public int getPort() {
			return innerSocket.getPort();
		}

		@Override
		public synchronized int getReceiveBufferSize() throws SocketException {
			return innerSocket.getReceiveBufferSize();
		}

		@Override
		public synchronized int getSendBufferSize() throws SocketException {
			return innerSocket.getSendBufferSize();
		}

		@Override
		public int getSoLinger() throws SocketException {
			return innerSocket.getSoLinger();
		}

		@Override
		public synchronized int getSoTimeout() throws SocketException {
			return innerSocket.getSoTimeout();
		}

		@Override
		public int getTrafficClass() throws SocketException {
			return innerSocket.getTrafficClass();
		}

		@Override
		public SocketAddress getLocalSocketAddress() {
			return innerSocket.getLocalSocketAddress();
		}

		@Override
		public SocketAddress getRemoteSocketAddress() {
			return innerSocket.getRemoteSocketAddress();
		}

		@Override
		public SocketChannel getChannel() {
			return innerSocket.getChannel();
		}

		@Override
		public void setKeepAlive(boolean on) throws SocketException {
			innerSocket.setKeepAlive(on);
		}

		@Override
		public void setOOBInline(boolean on) throws SocketException {
			innerSocket.setOOBInline(on);
		}

		@Override
		public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
			innerSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
		}

		@Override
		public synchronized void setReceiveBufferSize(int size) throws SocketException {
			innerSocket.setReceiveBufferSize(size);
		}

		@Override
		public void setReuseAddress(boolean on) throws SocketException {
			innerSocket.setReuseAddress(on);
		}

		@Override
		public synchronized void setSendBufferSize(int size) throws SocketException {
			innerSocket.setSendBufferSize(size);
		}

		@Override
		public void setSoLinger(boolean on, int linger) throws SocketException {
			innerSocket.setSoLinger(on, linger);
		}

		@Override
		public synchronized void setSoTimeout(int timeout) throws SocketException {
			innerSocket.setSoTimeout(timeout);
		}

		@Override
		public void setTcpNoDelay(boolean on) throws SocketException {
			innerSocket.setTcpNoDelay(on);
		}

		@Override
		public void setTrafficClass(int tc) throws SocketException {
			innerSocket.setTrafficClass(tc);
		}

		@Override
		public int hashCode() {
			return innerSocket.hashCode();
		}

		@Override
		public InetAddress getInetAddress() {
			if (remoteAddress instanceof InetSocketAddress) {
				return ((InetSocketAddress) remoteAddress).getAddress();
			}
			return null;
		}
	};

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
		System.out.println("connect to " + remote);
		SocketAddress socksProxy = getSocksProxy();
		if (socksProxy == null) {
			return innerSocketChannel.connect(remote);
		} else {
			innerSocketChannel.connect(socksProxy);
			System.out.println("connect to " + remote);
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

	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {

	}
}
