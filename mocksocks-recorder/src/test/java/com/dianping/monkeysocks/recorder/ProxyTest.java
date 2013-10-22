package com.dianping.monkeysocks.recorder;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Properties;

/**
 * @author yihua.huang@dianping.com <br>
 * @date: 13-7-6 <br>
 * Time: 上午8:17 <br>
 */
public class ProxyTest {

    @Test
    public void testProxy() throws IOException {
        Properties properties = System.getProperties();
        properties.setProperty("socksProxyHost", "127.0.0.1");
        properties.setProperty("socksProxyPort", "1081");
        URL url = new URL("http://ruby-china.org/");
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        String s = IOUtils.toString(urlConnection.getInputStream());
        System.out.println(s.length());
    }

    @Test
    public void byteBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }
        buffer.flip();
        buffer.put((byte)10);
        for (int i = 0; i < 5; i++) {
            System.out.println(buffer.get());
        }
        buffer.flip();
        for (int i = 0; i < 5; i++) {
            System.out.println(buffer.get());
        }
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();
    }
}
