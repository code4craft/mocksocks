package com.dianping.mocksocks.proxy.config;

/**
 * @author yihua.huang@dianping.com
 */
public class Configs {

	private final static Configs INSTANCE = new Configs();

	private boolean record = true;

	public static Configs getInstance() {
		return INSTANCE;
	}

	public boolean isRecord() {
		return record;
	}

	public boolean switchRecord() {
		this.record = !this.record;
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public String filePath() {
		return "/data/appdatas/mocksocks/";
	}
}
