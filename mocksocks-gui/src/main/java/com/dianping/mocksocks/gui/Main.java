package com.dianping.mocksocks.gui;

import com.dianping.mocksocks.gui.data.ConnectionStatusListModel;
import com.dianping.mocksocks.gui.data.HostFilter;
import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.socks.SocksProxy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author yihua.huang@dianping.com
 */
public class Main {
	private JList list1;
	private JPanel panel;
	private JButton startButton;
	private JButton stopButton;
	private JButton clearButton;
	private JComboBox comboBox;
	private JTextField textField1;
	private JButton filterButton;

	private ConnectionStatusListModel listModel;
	final SocksProxy socksProxy = new SocksProxy();

	public Main() {
		listModel = new ConnectionStatusListModel(2000);
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
		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireFilter();
			}
		});
		textField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					fireFilter();
				}
			}
		});
	}

	private void fireFilter() {
		String filterType = (String) comboBox.getSelectedItem();
		String filterValue = textField1.getText();
		if ("host".equals(filterType)) {
			try {
				listModel.setFilter(new HostFilter(filterValue));
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showConfirmDialog(panel, ex);
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("MockSocks");
		frame.setContentPane(new Main().panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
