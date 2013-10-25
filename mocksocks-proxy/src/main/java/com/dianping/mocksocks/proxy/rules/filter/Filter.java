package com.dianping.mocksocks.proxy.rules.filter;

/**
 * @author yihua.huang@dianping.com
 */
public interface Filter<T> {

	public boolean preserve(T t);
}
