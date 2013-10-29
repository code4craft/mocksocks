package com.dianping.mocksocks.gui.gui;

import com.dianping.mocksocks.gui.data.ConnectionStatusListModel;
import com.dianping.mocksocks.gui.data.MessageListModel;
import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;
import com.dianping.mocksocks.proxy.rules.filter.ConnectionStatusHostFilter;
import com.dianping.mocksocks.proxy.socks.SocksProxy;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author yihua.huang@dianping.com
 */
public class Main {
	public static final int REFRESH_TIME = 1000;
	private JList listConnection;
	private JPanel panel;
	private JButton startButton;
	private JButton stopButton;
	private JButton clearButton;
	private JComboBox comboBox;
	private JTextField textField1;
	private JButton filterButton;
	private JTabbedPane tabbedPane1;
	private JList listMessage;
	private Menu menu;
	private RedirectRulesDialog redirectRulesDialog;

	private ConnectionStatusListModel listModel;
	final SocksProxy socksProxy = new SocksProxy();

	public Main() {
		listModel = new ConnectionStatusListModel(REFRESH_TIME);
		listConnection.setModel(listModel);
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
				ConnectionMonitor.getInstance().clear();
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
		menu = new Menu();
		menu.getRedirectRules().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getRedirectRulesDialog().setVisible(true);
				getRedirectRulesDialog().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}
		});
		listConnection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					ConnectionStatus connectionStatus = listModel.getConnectionStatus(listConnection.getSelectedIndex());
					MessageListModel defaultListModel = new MessageListModel(connectionStatus);
					listMessage.setModel(defaultListModel);
                    tabbedPane1.setSelectedIndex(1);
                }
			}
		});
	}

	public RedirectRulesDialog getRedirectRulesDialog() {
		if (redirectRulesDialog == null) {
			redirectRulesDialog = new RedirectRulesDialog();
			redirectRulesDialog.pack();
		}
		return redirectRulesDialog;
	}

	private void fireFilter() {
		String filterType = (String) comboBox.getSelectedItem();
		String filterValue = textField1.getText();
		if ("host".equals(filterType)) {
			try {
				listModel.setFilter(new ConnectionStatusHostFilter(filterValue));
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showConfirmDialog(panel, ex);
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("MockSocks");
		Main main = new Main();
		frame.setContentPane(main.panel);
		frame.setJMenuBar(main.menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
	}
}
