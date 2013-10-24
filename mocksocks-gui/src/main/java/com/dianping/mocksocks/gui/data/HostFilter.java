package com.dianping.mocksocks.gui.data;

import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;
import java.util.regex.Pattern;

/**
 * @author yihua.huang@dianping.com
 */
public class HostFilter implements Filter<ConnectionStatus> {

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
	public boolean preserve(ConnectionStatus connectionStatus) {
		if (portFilter != -1 && portFilter != connectionStatus.getAddress().getPort()) {
			return false;
		}
		if (hostFilter.matcher(connectionStatus.getAddress().getHostName()).find()) {
			return true;
		}
		if (hostFilter.matcher(connectionStatus.getAddress().getAddress().getHostAddress()).find()) {
			return true;
		}
		return false;
	}
}
