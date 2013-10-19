package com.dianping.mocksocks.client;

import org.objectweb.asm.ClassVisitor;

/**
 * @author yihua.huang@dianping.com
 */
public class MyClassVisitor extends ClassVisitor {
	public MyClassVisitor(int api) {
		super(api);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		System.out.println(name + " " + signature + " " + superName);
	}
}
