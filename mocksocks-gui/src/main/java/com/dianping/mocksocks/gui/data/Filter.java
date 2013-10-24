package com.dianping.mocksocks.gui.data;

/**
 * @author yihua.huang@dianping.com
 */
public interface Filter<T> {

	public boolean preserve(T t);
}
