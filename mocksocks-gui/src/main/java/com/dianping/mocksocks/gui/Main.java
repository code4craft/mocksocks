package com.dianping.mocksocks.gui;

import com.dianping.mocksocks.gui.data.ConnectionStatusListModel;
import com.dianping.mocksocks.proxy.socks.SocksProxy;

import javax.swing.*;

/**
 * @author yihua.huang@dianping.com
 */
public class Main {
	private JList list1;
	private JPanel panel;

    public Main() {
		final SocksProxy socksProxy = new SocksProxy();
		new Thread(new Runnable() {
			@Override
			public void run() {
				socksProxy.run();
			}
		}).start();
		ListModel listModel = new ConnectionStatusListModel(5000);
        list1.setModel(listModel);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Main");
		frame.setContentPane(new Main().panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
