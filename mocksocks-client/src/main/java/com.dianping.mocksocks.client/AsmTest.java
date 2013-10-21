package com.dianping.mocksocks.client;

import java.lang.reflect.Method;

/**
 * @author yihua.huang@dianping.com
 */
public class AsmTest {

	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Class<?> aClass = Class.forName("sun.nio.ch.SocketChannelImpl");
		while (aClass != null) {
			Method[] declaredMethods = aClass.getDeclaredMethods();
			for (Method declaredMethod : declaredMethods) {
				System.out.println(declaredMethod);
			}
			aClass = aClass.getSuperclass();
		}
	}
}
