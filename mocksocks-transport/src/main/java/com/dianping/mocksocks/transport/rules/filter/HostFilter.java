package com.dianping.mocksocks.transport.rules.filter;

import java.net.InetSocketAddress;
import java.util.regex.Pattern;

/**
 * @author yihua.huang@dianping.com
 */
public class HostFilter implements Filter<InetSocketAddress> {

	private Pattern hostFilter;

	private int portFilter = -1;

	public HostFilter(String filterValue) {
		if (filterValue.contains(":")) {
			portFilter = Integer.parseInt(filterValue.substring(filterValue.lastIndexOf(":") + 1));
			hostFilter = compile(filterValue.substring(0, filterValue.lastIndexOf(":")));
		} else {
			this.hostFilter = compile(filterValue);
		}
	}

	private Pattern compile(String str) {
		if (str.contains("*")) {
			return Pattern.compile(str.replace(".", "\\.").replace("*", ".*"));
		} else {
			return Pattern.compile(str, Pattern.LITERAL);
		}
	}

    @Override
    public boolean preserve(InetSocketAddress address) {
        if (address == null) {
            return false;
        }
        if (portFilter != -1 && portFilter != address.getPort()) {
            return false;
        }
        String hostName = address.getHostName();
        if (hostName != null && hostFilter.matcher(hostName).find()) {
            return true;
        }
        if (hostFilter.matcher(address.getAddress().getHostAddress()).find()) {
            return true;
        }
        return false;
    }
}
