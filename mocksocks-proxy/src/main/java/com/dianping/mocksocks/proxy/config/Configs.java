package com.dianping.mocksocks.proxy.config;

/**
 * @author yihua.huang@dianping.com
 */
public class Configs {

	private final static Configs INSTANCE = new Configs();

    public static Configs getInstance(){
        return INSTANCE;
    }

	public String filePath() {
		return "/data/appdatas/mocksocks/";
	}
}
