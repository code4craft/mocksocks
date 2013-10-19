package com.dianping.mocksocks.client;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author yihua.huang@dianping.com
 */
public class AsmTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> aClass = Class.forName("sun.nio.ch.SocketChannelImpl");
        try {
            ClassReader classReader = new ClassReader(SocketChannel.class.getName());
            classReader.accept(new MyClassVisitor(Opcodes.ASM4),0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
