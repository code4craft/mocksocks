package com.dianping.mocksocks.gui.gui;

import com.dianping.mocksocks.gui.data.ConnectionStatusListModel;
import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.rules.filter.ConnectionStatusHostFilter;
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
    public static final int REFRESH_TIME = 1000;
    private JList list1;
	private JPanel panel;
	private JButton startButton;
	private JButton stopButton;
	private JButton clearButton;
	private JComboBox comboBox;
	private JTextField textField1;
	private JButton filterButton;
	private Menu menu;
	private RedirectRules redirectRules;

	private ConnectionStatusListModel listModel;
	final SocksProxy socksProxy = new SocksProxy();

	public Main() {
		listModel = new ConnectionStatusListModel(REFRESH_TIME);
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
                getRedirectRules().setVisible(true);
                getRedirectRules().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}
		});
	}

	public RedirectRules getRedirectRules() {
		if (redirectRules == null) {
			redirectRules = new RedirectRules();
            redirectRules.pack();
		}
		return redirectRules;
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
}
