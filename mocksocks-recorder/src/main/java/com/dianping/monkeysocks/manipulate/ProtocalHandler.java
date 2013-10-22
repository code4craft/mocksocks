package com.dianping.monkeysocks.manipulate;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-6 <br>
 * Time: 下午8:38 <br>
 */
public interface ProtocalHandler {

    public boolean isSupport(byte[] initWindow, String ip, int port);

    public ProtocalHandler init(byte[] initWindow, String ip, int port);

    public byte[] handle(byte[] window, int length);
}
