package sun.nio.ch;

import java.io.FileDescriptor;
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
public class SocksProxySocketChannel extends SocketChannelImpl2 {

	public SocksProxySocketChannel(SelectorProvider provider, SocketChannel innerSocketChannel) throws IOException {
		super(provider);
		this.innerSocketChannel = (sun.nio.ch.SocketChannelImpl2) innerSocketChannel;
	}

	private sun.nio.ch.SocketChannelImpl2 innerSocketChannel;

	private SocketAddress remote;

	@Override
	public Socket socket() {
		return innerSocketChannel.socket();
	}

	@Override
	public FileDescriptor getFD() {
		return innerSocketChannel.getFD();
	}

	@Override
	public int getFDVal() {
		return innerSocketChannel.getFDVal();
	}

	@Override
	public boolean translateAndUpdateReadyOps(int i, SelectionKeyImpl selectionKey) {
		return translateReadyOps(i, selectionKey.nioReadyOps(), selectionKey);
	}

	@Override
	public boolean translateAndSetReadyOps(int i, SelectionKeyImpl selectionKey) {
		return translateReadyOps(i, 0, selectionKey);
	}

	@Override
	public void translateAndSetInterestOps(int i, SelectionKeyImpl selectionKey) {
		innerSocketChannel.translateAndSetInterestOps(i, selectionKey);
	}

	// @Override
	// public boolean translateReadyOps(int ops, int initialOps,
	// SelectionKeyImpl sk) {
	// return innerSocketChannel.translateReadyOps(ops, initialOps, sk);
	// }

	public boolean translateReadyOps(int ops, int initialOps, SelectionKeyImpl sk) {
		int intOps = sk.nioInterestOps(); // Do this just once, it synchronizes
		int oldOps = sk.nioReadyOps();
		int newOps = initialOps;

		if ((ops & PollArrayWrapper.POLLNVAL) != 0) {
			// This should only happen if this channel is pre-closed while a
			// selection operation is in progress
			// ## Throw an error if this channel has not been pre-closed
			return false;
		}

		return innerSocketChannel.translateReadyOps(ops, initialOps, sk);

	}

	@Override
	public void kill() throws IOException {
		innerSocketChannel.kill();
	}

	private class SocketWrapper extends Socket {

		private SocketChannel innerSocketChannel;

		private SocketAddress remoteAddress;

		public SocketWrapper(SocketChannel innerSocketChannel, SocketAddress remoteAddress) {
			this.innerSocketChannel = innerSocketChannel;
			this.remoteAddress = remoteAddress;
		}

		@Override
		public void connect(SocketAddress endpoint, int timeout) throws IOException {
			SocksProxySocketChannel.this.connect(endpoint);
		}

		@Override
		public synchronized void close() throws IOException {
			innerSocketChannel.socket().close();
		}

		@Override
		public void connect(SocketAddress endpoint) throws IOException {
			connect(endpoint, 0);
		}

		@Override
		public void bind(SocketAddress bindpoint) throws IOException {
			innerSocketChannel.socket().bind(bindpoint);
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return innerSocketChannel.socket().getOutputStream();
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return innerSocketChannel.socket().getInputStream();
		}

		@Override
		public boolean getKeepAlive() throws SocketException {
			return innerSocketChannel.socket().getKeepAlive();
		}

		@Override
		public boolean getOOBInline() throws SocketException {
			return innerSocketChannel.socket().getOOBInline();
		}

		@Override
		public boolean getReuseAddress() throws SocketException {
			return innerSocketChannel.socket().getReuseAddress();
		}

		@Override
		public boolean getTcpNoDelay() throws SocketException {
			return innerSocketChannel.socket().getTcpNoDelay();
		}

		@Override
		public InetAddress getLocalAddress() {
			return innerSocketChannel.socket().getLocalAddress();
		}

		@Override
		public int getLocalPort() {
			return innerSocketChannel.socket().getLocalPort();
		}

		@Override
		public int getPort() {
			return innerSocketChannel.socket().getPort();
		}

		@Override
		public synchronized int getReceiveBufferSize() throws SocketException {
			return innerSocketChannel.socket().getReceiveBufferSize();
		}

		@Override
		public synchronized int getSendBufferSize() throws SocketException {
			return innerSocketChannel.socket().getSendBufferSize();
		}

		@Override
		public int getSoLinger() throws SocketException {
			return innerSocketChannel.socket().getSoLinger();
		}

		@Override
		public synchronized int getSoTimeout() throws SocketException {
			return innerSocketChannel.socket().getSoTimeout();
		}

		@Override
		public int getTrafficClass() throws SocketException {
			return innerSocketChannel.socket().getTrafficClass();
		}

		@Override
		public SocketAddress getLocalSocketAddress() {
			return innerSocketChannel.socket().getLocalSocketAddress();
		}

		@Override
		public SocketAddress getRemoteSocketAddress() {
			return remoteAddress;
		}

		@Override
		public SocketChannel getChannel() {
			return innerSocketChannel.socket().getChannel();
		}

		@Override
		public void setKeepAlive(boolean on) throws SocketException {
			innerSocketChannel.socket().setKeepAlive(on);
		}

		@Override
		public void setOOBInline(boolean on) throws SocketException {
			innerSocketChannel.socket().setOOBInline(on);
		}

		@Override
		public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
			innerSocketChannel.socket().setPerformancePreferences(connectionTime, latency, bandwidth);
		}

		@Override
		public synchronized void setReceiveBufferSize(int size) throws SocketException {
			innerSocketChannel.socket().setReceiveBufferSize(size);
		}

		@Override
		public void setReuseAddress(boolean on) throws SocketException {
			innerSocketChannel.socket().setReuseAddress(on);
		}

		@Override
		public synchronized void setSendBufferSize(int size) throws SocketException {
			innerSocketChannel.socket().setSendBufferSize(size);
		}

		@Override
		public void setSoLinger(boolean on, int linger) throws SocketException {
			innerSocketChannel.socket().setSoLinger(on, linger);
		}

		@Override
		public synchronized void setSoTimeout(int timeout) throws SocketException {
			innerSocketChannel.socket().setSoTimeout(timeout);
		}

		@Override
		public void setTcpNoDelay(boolean on) throws SocketException {
			innerSocketChannel.socket().setTcpNoDelay(on);
		}

		@Override
		public void setTrafficClass(int tc) throws SocketException {
			innerSocketChannel.socket().setTrafficClass(tc);
		}

		@Override
		public int hashCode() {
			return innerSocketChannel.socket().hashCode();
		}

		@Override
		public InetAddress getInetAddress() {
			if (remoteAddress instanceof InetSocketAddress) {
				return ((InetSocketAddress) remoteAddress).getAddress();
			}
			return innerSocketChannel.socket().getInetAddress();
		}
	};

	@Override
	public boolean isConnected() {
		return innerSocketChannel.isConnected();
	}

	@Override
	public int hashCode() {
		return innerSocketChannel.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SocksProxySocketChannel)) {
			return false;
		}
		return innerSocketChannel.equals(((SocksProxySocketChannel) obj).innerSocketChannel);
	}

	@Override
	public boolean isConnectionPending() {
		return innerSocketChannel.isConnectionPending();
	}

	@Override
	public boolean connect(SocketAddress remote) throws IOException {
		this.remote = remote;
		SocketAddress socksProxy = getSocksProxy();
		if (socksProxy == null) {
			innerSocketChannel.connect(remote);
		} else {
			innerSocketChannel.connect(socksProxy);
		}
        return false;
    }

//	public boolean connect(SocketAddress sa) throws IOException {
//		int trafficClass = 0; // ## Pick up from options
//		int localPort = 0;
//
//		ensureOpenAndUnconnected();
//		InetSocketAddress isa = Net.checkAddress(sa);
//		SecurityManager sm = System.getSecurityManager();
//		if (sm != null)
//			sm.checkConnect(isa.getAddress().getHostAddress(), isa.getPort());
//		synchronized (blockingLock()) {
//			int n = 0;
//			try {
//				try {
//					begin();
//					if (!isOpen()) {
//						return false;
//					}
////					readerThread = NativeThread.current();
//					for (;;) {
//						InetAddress ia = isa.getAddress();
//						if (ia.isAnyLocalAddress())
//							ia = InetAddress.getLocalHost();
//						n = Net.connect(getFD(), ia, isa.getPort(), trafficClass);
//						if ((n == IOStatus.INTERRUPTED) && isOpen())
//							continue;
//						break;
//					}
//				} finally {
//					end((n > 0) || (n == IOStatus.UNAVAILABLE));
//					assert IOStatus.check(n);
//				}
//			} catch (IOException x) {
//				// If an exception was thrown, close the channel after
//				// invoking end() so as to avoid bogus
//				// AsynchronousCloseExceptions
//				close();
//				throw x;
//			}
//			if (n > 0) {
//
//				// Connection succeeded; disallow further
//				// invocation
//                System.out.println("ST_CONNECTED");
//                return true;
//			}
//			// If nonblocking and no exception then connection
//			// pending; disallow another invocation
//			if (!isBlocking())
//                System.out.println("ST_PENDING");
//			else
//				assert false;
//		}
//		return false;
//	}

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
		innerSocketChannel.implCloseSelectableChannel();
	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {
		innerSocketChannel.implConfigureBlocking(block);
	}

	public boolean isBound() {
		return innerSocketChannel.isBound();
	}

	public boolean isInputOpen() {
		return innerSocketChannel.isInputOpen();
	}

	public boolean isOutputOpen() {
		return innerSocketChannel.isOutputOpen();
	}

	public void bind(SocketAddress local) throws IOException {
		innerSocketChannel.bind(local);
	}

	public SocketAddress remoteAddress() {
		return remote;
	}

	public SocketOpts options() {
		return innerSocketChannel.options();
	}

	public SocketAddress localAddress() {
		return innerSocketChannel.localAddress();
	}

	void ensureOpenAndUnconnected() throws IOException {
		innerSocketChannel.ensureOpenAndUnconnected();
	}

	public void shutdownInput() throws IOException {
		innerSocketChannel.shutdownInput();
	}

	public void shutdownOutput() throws IOException {
		innerSocketChannel.shutdownOutput();
	}


}
