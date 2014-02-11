package com.dianping.mocksocks.transport.monitor;

import com.dianping.mocksocks.transport.Connection;
import org.jboss.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionMonitor {

	private static final ConnectionMonitor INSTANCE = new ConnectionMonitor();

	public static ConnectionMonitor getInstance() {
		return INSTANCE;
	}

    private ConnectionMonitor(){
    }

	private final Map<Channel, Connection> connectionStatuses = new ConcurrentHashMap<Channel, Connection>();

	static {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// while (true) {
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// output();
		// }
		// }
		// }).start();
	}

	public Connection getStatus(Channel channel) {
		Connection connection = connectionStatuses.get(channel);
		return connection;
	}

    public void putStatus(Channel channel,Connection connection) {
        connectionStatuses.put(channel, connection);
    }

	public List<Connection> status() {
		return new ArrayList<Connection>(connectionStatuses.values());
	}

	public void clear() {
		connectionStatuses.clear();
	}

    @Deprecated
	public void output() {
		System.out.println("==============================================================================");
		for (Map.Entry<Channel, Connection> connectionStatusEntry : connectionStatuses.entrySet()) {
			if (connectionStatusEntry.getValue().getEndTime() != 0l
					&& connectionStatusEntry.getValue().getEndTime() > (System.currentTimeMillis() - 30000)) {
				System.out.println(connectionStatusEntry.getValue());
			}
		}
	}
}
