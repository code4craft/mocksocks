package com.dianping.mocksocks.gui;

import com.dianping.mocksocks.gui.data.ConnectionStatusListModel;
import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.socks.SocksProxy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author yihua.huang@dianping.com
 */
public class Main {
	private JList list1;
	private JPanel panel;
	private JButton startButton;
	private JButton stopButton;
	private JButton clearButton;

	private ConnectionStatusListModel listModel;
	final SocksProxy socksProxy = new SocksProxy();

	public Main() {
		listModel = new ConnectionStatusListModel(5000);
		list1.setModel(listModel);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				socksProxy.start();
			}
		});
		stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                socksProxy.stop();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.clear();
                ConnectionMonitor.clear();
            }
        });
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("MockSocks");
		frame.setContentPane(new Main().panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
