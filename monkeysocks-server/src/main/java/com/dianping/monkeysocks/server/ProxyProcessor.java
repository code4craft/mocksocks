package com.dianping.monkeysocks.server;

import java.io.IOException;
import java.net.Socket;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-11 <br>
 * Time: 上午8:04 <br>
 */
public abstract class ProxyProcessor implements SocketProcessor {

    public static final int BUFFER_SIZE = 1024;
    private String remoteHost;

    private int remotePort;

    private Socket remoteSocket;

    private Socket socket;

    private Thread threadReadAndFoward;

    private Thread threadRemoteReadAndWrite;

    @Override
    public SocketProcessor init(Socket socket) {
        this.socket = socket;
        return this;
    }

    @Override
    public void run() {
        threadReadAndFoward = Thread.currentThread();
        //start remote connection
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read remote connection &
        threadRemoteReadAndWrite = new Thread() {
            @Override
            public void run() {
                byte[] buffer = new byte[BUFFER_SIZE];
                try {
                    int len;
                    while ((len = socket.getInputStream().read(buffer)) > 0) {
                        remoteSocket.getOutputStream().write(buffer, 0, len);
                        remoteSocket.getOutputStream().flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        threadRemoteReadAndWrite.start();
        //read local input and forward to remote
        //stop when socket close
    }

    private void connect() throws IOException {
        remoteSocket = new Socket(remoteHost, remotePort);
    }
}
