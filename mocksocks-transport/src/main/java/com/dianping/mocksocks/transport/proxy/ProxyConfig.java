package com.dianping.mocksocks.transport.proxy;

/**
 * @author yihua.huang@dianping.com
 */
public class ProxyConfig {

	public enum Mode {
		Proxy, Mock;
	}

    private Mode mode;

    private boolean record;

    public static ProxyConfig custom(){
        return new ProxyConfig();
    }

    public ProxyConfig setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public ProxyConfig setRecord(boolean record) {
        this.record = record;
        return this;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean isRecord() {
        return record;
    }
}
