package com.dianping.monkeysocks.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-8 <br>
 * Time: 下午8:20 <br>
 */
public class PipeLineSocket extends Socket {

    public static final int DEFAULT_CAPACITY = 10000;
    private PipeInputStream inputStream;

    private PipeOutputStream outputStream;

    private BlockingStreamBuffer blockingStreamBuffer;

    public PipeLineSocket() {
        this(DEFAULT_CAPACITY);
    }

    public PipeLineSocket(int capacity) {
        blockingStreamBuffer = BlockingStreamBuffer.allocate(capacity);
        inputStream = new PipeInputStream(blockingStreamBuffer);
        outputStream = new PipeOutputStream(blockingStreamBuffer);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public synchronized void close() throws IOException {
        super.close();
        inputStream.close();
        outputStream.close();
    }
}
