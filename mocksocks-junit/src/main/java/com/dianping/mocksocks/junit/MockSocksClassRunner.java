package com.dianping.mocksocks.junit;

import com.dianping.mocksocks.proxy.Proxy;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

/**
 * @author yihua.huang@dianping.com
 */
public class MockSocksClassRunner extends JUnit4ClassRunner {
	public MockSocksClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	public void run(RunNotifier notifier) {
		setProxy();
		super.run(notifier);
	}

	private static void setProxy() {
		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "40310");
	}

    private static void startProxy(String cacheFileName) {
        Proxy proxy = null;
//        proxy.start();
//        proxy.loadCache(cacheFileName);
    }

	public static void init(Object object) {
		setProxy();
        //TODO:file
        startProxy("");
	}
}
