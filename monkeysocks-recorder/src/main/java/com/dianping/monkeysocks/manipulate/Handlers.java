package com.dianping.monkeysocks.manipulate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-6 <br>
 * Time: 下午8:44 <br>
 */
public class Handlers {

    private final static List<ProtocalHandler> HANDLER_LIST = new ArrayList<ProtocalHandler>();

    static {
        HANDLER_LIST.add(new HttpHandler(0));
    }

    public static ProtocalHandler getHandler(byte[] initWindow, String ip, int port) {
        for (ProtocalHandler protocalHandler : HANDLER_LIST) {
            if (protocalHandler.isSupport(initWindow, ip, port)) {
                return protocalHandler.init(initWindow, ip, port);
            }
        }
        return null;
    }
}
