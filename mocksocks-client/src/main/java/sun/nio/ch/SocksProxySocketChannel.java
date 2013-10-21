package sun.nio.ch;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksProxySocketChannel extends SocketChannelImpl {

	private SocketAddress remote;

	public SocksProxySocketChannel(SelectorProvider sp) throws IOException {
		super(sp);
	}

	@Override
	public SocketAddress remoteAddress() {
		if (remote != null) {
			return remote;
		} else {
			return super.remoteAddress();
		}
	}

	@Override
	public boolean connect(SocketAddress sa) throws IOException {
		this.remote = sa;
		boolean connect;
		SocketAddress socksProxy = getSocksProxy();
		if (socksProxy != null) {
			boolean blocking = isBlocking();
			if (!blocking) {
				// change to blocking mode for easy
				configureBlocking(true);
			}
			connect = socksConnect(socksProxy);
			if (!blocking) {
				configureBlocking(false);
			}
		} else {
			connect = super.connect(sa);
		}
		return connect;
	}

	private boolean socksConnect(SocketAddress socksProxy) throws IOException {
		boolean connect;
		connect = super.connect(socksProxy);
		if (!connect) {
			throw new IOException("Connect to proxy " + socksProxy + " fail!");
		}
		return connect;
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
}
