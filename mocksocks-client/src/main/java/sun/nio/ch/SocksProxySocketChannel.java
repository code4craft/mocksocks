package sun.nio.ch;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksProxySocketChannel extends SocketChannelImpl {

	public static final byte CMD_CONNECT = (byte) 0x01;
	public static final byte SCHEME_NO_AUTH = (byte) 0x00;
	public static final byte RESERVED_BYTE = (byte) 0x00;
	public static final byte ATYP_IPV4 = (byte) 0x01;
	public static final byte ATYP_IPV6 = (byte) 0x04;
	private SocketAddress remote;

	private static final int DEFAULT_ENCODER_BUFFER_SIZE = 1024;

	private static final byte SOCKS_PROTOCAL_VERSION = (byte) 0x05;

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
				try {
					configureBlocking(true);
					connect = socksConnect(socksProxy);
					if (!blocking) {
						configureBlocking(false);
					}
				} catch (IllegalBlockingModeException e) {
					System.err.println("Can't set proxy for " + sa);
					connect = super.connect(sa);
				}
			} else {
				connect = socksConnect(socksProxy);
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
		ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_ENCODER_BUFFER_SIZE);
		sendSockInitRequest(byteBuffer);
		sendSockCmdRequest(byteBuffer);
		// TODO:handle response
		return connect;
	}

	private void sendSockCmdRequest(ByteBuffer byteBuffer) throws IOException {
		byteBuffer.clear();
		byteBuffer.put(SOCKS_PROTOCAL_VERSION);
		// TODO:add more cmd support
		byteBuffer.put(CMD_CONNECT);
		byteBuffer.put(RESERVED_BYTE);
		InetSocketAddress inetSocketAddress = Net.asInetSocketAddress(remote);
		if (inetSocketAddress.getAddress() instanceof Inet6Address) {
			byteBuffer.put(ATYP_IPV6);
		} else {
			byteBuffer.put(ATYP_IPV4);
		}
		byteBuffer.put(inetSocketAddress.getAddress().getAddress());
		byteBuffer.putShort((short) (0xffff & inetSocketAddress.getPort()));
		byteBuffer.flip();
		write(byteBuffer);
		read(byteBuffer);
	}

	private void sendSockInitRequest(ByteBuffer byteBuffer) throws IOException {
		byteBuffer.clear();
		byteBuffer.put(SOCKS_PROTOCAL_VERSION);
		byteBuffer.put((byte) 0x01); // size of authschemes
		byteBuffer.put(SCHEME_NO_AUTH);
		byteBuffer.flip();
		write(byteBuffer);
		byteBuffer.clear();
		read(byteBuffer);
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
