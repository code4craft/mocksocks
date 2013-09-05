package com.dianping.monkeysocks.manipulate;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-6 <br>
 * Time: 下午8:43 <br>
 */
public class HttpHandler implements ProtocalHandler {

    private int port;

    public HttpHandler(int port) {
        this.port = port;
    }

    @Override
    public boolean isSupport(byte[] initWindow, String ip, int port) {
        if (port==80||port==8080){
            return true;
        }
        return false;
    }

    @Override
    public ProtocalHandler init(byte[] initWindow, String ip, int port) {
        return new HttpHandler(port);
    }

    @Override
    public byte[] handle(byte[] window, int length) {
        return window;
    }
}
