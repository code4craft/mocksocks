package com.dianping.mocksocks.client.agent;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksChannelTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println(className);
        ClassPool cp = ClassPool.getDefault();
        byte[] b = classfileBuffer;
        String name = className.replace("/", ".");
        cp.insertClassPath(new ByteArrayClassPath(name, b));
        try {
            CtClass cc = cp.get(className);
            System.out.println(cc);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return classfileBuffer;
    }
}
