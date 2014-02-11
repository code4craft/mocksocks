package com.dianping.mocksocks.transport.rules;

import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihua.huang@dianping.com
 */
public class RulesContainer {

	private final static RulesContainer INSTANCE = new RulesContainer();

	public static RulesContainer getInstance() {
		return INSTANCE;
	}

	private List<RedirectRule> redirectRules = new ArrayList<RedirectRule>();

	public static List<RedirectRule> parse(String exprs) {
		List<RedirectRule> redirectRules = new ArrayList<RedirectRule>();
		if (StringUtils.isBlank(exprs)) {
			return redirectRules;
		}
		String[] lines = exprs.split("\n");
		for (String line : lines) {
			RedirectRule redirectRule = RedirectRule.parse(line);
			redirectRules.add(redirectRule);
		}
		return redirectRules;
	}

	public InetSocketAddress getRedirectAddress(InetSocketAddress rawAddress) {
        for (RedirectRule redirectRule : redirectRules) {
            if (redirectRule.getHostFilter().preserve(rawAddress)) {
                return redirectRule.getInetSocketAddress();
            }
        }
		return rawAddress;
	}

	public void setRedirectRules(List<RedirectRule> redirectRules) {
		this.redirectRules = redirectRules;
	}

}
