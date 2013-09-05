package com.dianping.monkeysocks.socket;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A stream buffer in producer&consumer pattern.<br>
 *
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-9 <br>
 * Time: 下午8:21 <br>
 */
public class BlockingStreamBuffer {

    private byte[] buffer;

    private int capacity;

    private int readPointer;

    private int writePointer;

    private int flushPointer = Integer.MAX_VALUE;

    private ReentrantLock lock;

    private Condition full;

    private Condition empty;

    private boolean readClosed = false;

    private boolean writeClosed = false;

    public static BlockingStreamBuffer allocate(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("capacity must greater than 0");
        return new BlockingStreamBuffer(capacity);
    }

    BlockingStreamBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new byte[capacity];
        this.lock = new ReentrantLock();
        this.full = lock.newCondition();
        this.empty = lock.newCondition();
    }

    public int read() throws IOException {
        if (readPointer >= flushPointer) {
            flushPointer = Integer.MAX_VALUE;
            return -1;
        }
        try {
            lock.lock();
            while (readPointer >= writePointer) {
                if (writeClosed) {
                    return -1;
                }
                empty.await();
            }
            byte b = buffer[readPointer % capacity];
            readPointer++;
            full.signal();
            return b & 0xff;
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
        return 0;
    }

    public void write(int b) throws IOException {
        try {
            lock.lock();
            while (writePointer - readPointer >= capacity) {
                if (readClosed) {
                    throw new IOException("Buffer is full");
                }
                full.await();
            }
            buffer[writePointer % capacity] = (byte) b;
            writePointer++;
            empty.signal();
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

    public void closeRead() throws IOException {
        readClosed = true;
    }

    public void closeWrite() throws IOException {
        flushPointer = writePointer;
        writeClosed = true;
    }

    public void flush() throws IOException {
        flushPointer = writePointer;
    }

}
