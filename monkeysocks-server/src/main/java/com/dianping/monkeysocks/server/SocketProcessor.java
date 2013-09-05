package com.dianping.monkeysocks.server;

import java.net.Socket;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-7 <br>
 * Time: 下午7:22 <br>
 */
public interface SocketProcessor extends Runnable{

    public SocketProcessor init(Socket socket);

}
