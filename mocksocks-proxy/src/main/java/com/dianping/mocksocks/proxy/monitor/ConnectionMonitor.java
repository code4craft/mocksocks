package com.dianping.mocksocks.proxy.monitor;

import org.jboss.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionMonitor {

	private static final Map<Channel, ConnectionStatus> connectionStatuses = new ConcurrentHashMap<Channel, ConnectionStatus>();

	static {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					output();
//				}
//			}
//		}).start();
	}

	public static ConnectionStatus getStatus(Channel channel) {
		ConnectionStatus connectionStatus = connectionStatuses.get(channel);
		if (connectionStatus == null) {
			synchronized (ConnectionMonitor.class) {
				connectionStatus = new ConnectionStatus(channel);
				connectionStatuses.put(channel, connectionStatus);
			}
		}
		return connectionStatus;
	}

    public static List<ConnectionStatus> status(){
        return new ArrayList<ConnectionStatus>(connectionStatuses.values());
    }

    public static void clear(){
        connectionStatuses.clear();
    }

	public static void output() {
		System.out.println("==============================================================================");
		for (Map.Entry<Channel, ConnectionStatus> connectionStatusEntry : connectionStatuses.entrySet()) {
			if (connectionStatusEntry.getValue().getEndTime() != 0l
					&& connectionStatusEntry.getValue().getEndTime() > (System.currentTimeMillis() - 30000)) {
				System.out.println(connectionStatusEntry.getValue());
			}
		}
	}
}