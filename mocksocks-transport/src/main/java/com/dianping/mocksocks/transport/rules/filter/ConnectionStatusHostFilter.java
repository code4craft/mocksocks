package com.dianping.mocksocks.transport.rules.filter;

import com.dianping.mocksocks.transport.Connection;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatusHostFilter implements Filter<Connection> {

	private HostFilter hostFilter;

	public ConnectionStatusHostFilter(String filterExpr) {
		this.hostFilter = new HostFilter(filterExpr);
	}

	@Override
	public boolean preserve(Connection connection) {
		if (connection.getAddress() != null) {
			return hostFilter.preserve(connection.getAddress());
		}
		return false;
	}
}
