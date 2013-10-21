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
	public boolean connect(SocketAddress sa) throws IOException {
		this.remote = sa;
		SocketAddress socksProxy = getSocksProxy();
		if (socksProxy != null) {
			return super.connect(socksProxy);
		} else {
			return super.connect(sa);
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
}
