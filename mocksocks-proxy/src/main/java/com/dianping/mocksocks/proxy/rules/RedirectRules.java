package com.dianping.mocksocks.proxy.rules;

import com.dianping.mocksocks.proxy.rules.filter.HostFilter;
import com.dianping.mocksocks.proxy.utils.AddressUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yihua.huang@dianping.com
 */
public class RedirectRules {

	private static final RedirectRules INSTANCE = new RedirectRules();

	public static RedirectRules getInstance() {
		return INSTANCE;
	}

	private Map<HostFilter, InetSocketAddress> filters = new ConcurrentHashMap<HostFilter, InetSocketAddress>();

	public InetSocketAddress getRedirectAddress(InetSocketAddress rawAddress) {
		for (Map.Entry<HostFilter, InetSocketAddress> addressEntry : filters.entrySet()) {
			if (addressEntry.getKey().preserve(rawAddress)) {
				return addressEntry.getValue();
			}
		}
		return rawAddress;
	}

    public void setFilters(Map<HostFilter, InetSocketAddress> filters) {
        this.filters = filters;
    }

    public static Map<HostFilter, InetSocketAddress> parse(String exprs) {
		Map<HostFilter, InetSocketAddress> filters = new ConcurrentHashMap<HostFilter, InetSocketAddress>();
        if (StringUtils.isBlank(exprs)){
            return filters;
        }
		String[] lines = exprs.split("\n");
		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			HostFilter hostFilter = new HostFilter(tokens[0]);
			try {
				InetSocketAddress inetSocketAddress = AddressUtils.parse(tokens[1]);
				filters.put(hostFilter, inetSocketAddress);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException("Error in line:\""+line+"\"",e);
			}
		}
		return filters;
	}

}
