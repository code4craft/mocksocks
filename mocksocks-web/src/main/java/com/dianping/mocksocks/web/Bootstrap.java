package com.dianping.mocksocks.web;

import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.socks.SocksProxy;
import us.codecraft.express.WebServer;
import us.codecraft.express.controller.AjaxController;
import us.codecraft.express.controller.ParamMap;

/**
 * @author code4crafter@gmail.com
 */
public class Bootstrap {

	final SocksProxy socksProxy = new SocksProxy();

	public static void main(String[] args) throws Exception {
		Bootstrap bootstrap = new Bootstrap();
        bootstrap.startProxy();
        bootstrap.startServer();
	}

	private boolean startProxy() {
		try {
			socksProxy.start();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void startServer() throws Exception {
		WebServer.jettyServer().get("/connectionList", new AjaxController() {
			@Override
			public Object ajax(ParamMap params) {
                getResponse().addHeader("Access-Control-Allow-Origin","*");
				return ConnectionMonitor.getInstance().status();
			}
		}).port(8000).start();
	}
}
