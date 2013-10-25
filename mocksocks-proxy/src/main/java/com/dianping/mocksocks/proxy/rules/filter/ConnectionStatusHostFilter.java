package com.dianping.mocksocks.proxy.rules.filter;

import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatusHostFilter implements Filter<ConnectionStatus> {

	private HostFilter hostFilter;

	public ConnectionStatusHostFilter(String filterExpr) {
		this.hostFilter = new HostFilter(filterExpr);
	}

	@Override
	public boolean preserve(ConnectionStatus connectionStatus) {
		if (connectionStatus.getAddress() != null) {
			return hostFilter.preserve(connectionStatus.getAddress());
		}
		return false;
	}
}
