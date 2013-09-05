package com.dianping.monkeysocks.socket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-8 <br>
 * Time: 下午8:36 <br>
 */
public class PipeOutputStream extends OutputStream{

    private BlockingStreamBuffer blockingStreamBuffer;

    private boolean closed = false;

    public PipeOutputStream(BlockingStreamBuffer blockingStreamBuffer) {
        this.blockingStreamBuffer = blockingStreamBuffer;
    }

    @Override
    public void write(int b) throws IOException {
        if (closed){
            throw new IOException("Stream is closed.");
        }
        blockingStreamBuffer.write((byte)b);
    }

    @Override
    public void flush() throws IOException {
        blockingStreamBuffer.flush();
    }

    @Override
    public void close() throws IOException {
        closed = true;
        blockingStreamBuffer.closeWrite();
        blockingStreamBuffer = null;
    }
}
