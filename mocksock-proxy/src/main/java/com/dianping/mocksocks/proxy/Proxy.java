package com.dianping.mocksocks.proxy;

/**
 * @author yihua.huang@dianping.com
 */
public interface Proxy {

    public void start();

    public void stop();

    public void loadCache(String cacheFile);
}
