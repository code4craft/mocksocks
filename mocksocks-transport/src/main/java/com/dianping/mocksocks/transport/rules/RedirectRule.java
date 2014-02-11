package com.dianping.mocksocks.transport.rules;

import com.dianping.mocksocks.transport.rules.filter.HostFilter;
import com.dianping.mocksocks.transport.utils.AddressUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author yihua.huang@dianping.com
 */
public class RedirectRule {

	private HostFilter hostFilter;

	private InetSocketAddress inetSocketAddress;

	public RedirectRule(HostFilter hostFilter, InetSocketAddress inetSocketAddress) {
		this.hostFilter = hostFilter;
		this.inetSocketAddress = inetSocketAddress;
	}

	public HostFilter getHostFilter() {
		return hostFilter;
	}

	public void setHostFilter(HostFilter hostFilter) {
		this.hostFilter = hostFilter;
	}

	public InetSocketAddress getInetSocketAddress() {
		return inetSocketAddress;
	}

	public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
		this.inetSocketAddress = inetSocketAddress;
	}

	public static RedirectRule parse(String expr) {
		if (StringUtils.isBlank(expr)) {
			return null;
		}
		String[] tokens = expr.split("\\s+");
		HostFilter hostFilter = new HostFilter(tokens[0]);
		try {
			InetSocketAddress inetSocketAddress = AddressUtils.parse(tokens[1]);
            return new RedirectRule(hostFilter,inetSocketAddress);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("Error in line:\"" + expr + "\"", e);
		}
	}
}