package com.dianping.mocksocks.client.agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksAgent {

    public static void premain(String agentArgs, Instrumentation inst){
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "13720");
        System.setProperty("java.nio.channels.spi.SelectorProvider", "com.dianping.mocksocks.client.proxy.SocksProxySelectorProvider");
//        inst.addTransformer(new SocksChannelTransformer());
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get("sun.nio.ch.SelChImpl");
            CtClass ctClass2 = classPool.get("com.dianping.mocksocks.client.proxy.SocksProxySocketChannel");
            ctClass.setModifiers(Modifier.PUBLIC);
            ctClass.writeFile();
            ctClass2.setSuperclass(ctClass);
            ctClass2.writeFile();
            ctClass.detach();
            ctClass2.detach();
            ctClass = classPool.get("sun.nio.ch.SelChImpl");
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            Class<?> aClass = Class.forName("sun.nio.ch.SelChImpl");
//            System.out.println(aClass);
//            System.out.println(aClass.getClassLoader());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        //fot nio

    }

    public static void agentmain(String args, Instrumentation inst) throws Exception
    {
        premain(args, inst);
    }
}
