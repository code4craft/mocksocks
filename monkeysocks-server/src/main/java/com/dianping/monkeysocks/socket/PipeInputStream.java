package com.dianping.monkeysocks.socket;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-8 <br>
 * Time: 下午8:35 <br>
 */
public class PipeInputStream extends InputStream {

    private BlockingStreamBuffer blockingStreamBuffer;

    private boolean closed = false;

    public PipeInputStream(BlockingStreamBuffer blockingStreamBuffer) {
        this.blockingStreamBuffer = blockingStreamBuffer;
    }

    @Override
    public int read() throws IOException {
        if (closed){
            throw new IOException("Stream is closed.");
        }
        return blockingStreamBuffer.read();
    }

    @Override
    public void close() throws IOException {
        closed = true;
        blockingStreamBuffer.closeRead();
        blockingStreamBuffer = null;
    }
}
