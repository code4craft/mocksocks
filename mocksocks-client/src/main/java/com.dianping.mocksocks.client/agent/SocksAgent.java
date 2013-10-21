package com.dianping.mocksocks.client.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * @author yihua.huang@dianping.com
 */
public class SocksAgent {

	public static void premain(String agentArgs, Instrumentation inst) throws IOException {
		if (System.getProperty("socksProxyHost") == null) {
			System.setProperty("socksProxyHost", "127.0.0.1");
		}
		if (System.getProperty("socksProxyPort") == null) {
			System.setProperty("socksProxyPort", "13721");
		}
		String mockFile = System.getProperty("mockFile");
		if (mockFile != null) {
			inst.appendToBootstrapClassLoaderSearch(new JarFile(mockFile));
		}
		System.setProperty("java.nio.channels.spi.SelectorProvider",
				"sun.nio.ch.SocksProxySelectorProvider");

	}

	public static void agentmain(String args, Instrumentation inst) throws Exception {
		premain(args, inst);
	}
}
