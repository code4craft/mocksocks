package com.dianping.mocksocks.gui.gui;

import com.dianping.mocksocks.gui.data.ConnectionStatusListModel;
import com.dianping.mocksocks.gui.data.MessageListModel;
import com.dianping.mocksocks.transport.Transmit;
import com.dianping.mocksocks.transport.Connection;
import com.dianping.mocksocks.transport.monitor.config.Configs;
import com.dianping.mocksocks.transport.monitor.ConnectionMonitor;
import com.dianping.mocksocks.transport.rules.filter.ConnectionStatusHostFilter;
import com.dianping.mocksocks.transport.socks.SocksProxy;

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
	private JToolBar toolBar;
	private Menu menu;
	private RedirectRulesDialog redirectRulesDialog;
	private MessageDetailDialog messageDetailDialog;

	private ConnectionStatusListModel listModel;
	final SocksProxy socksProxy = new SocksProxy();

	public Main() {
		listModel = new ConnectionStatusListModel(REFRESH_TIME);
		listConnection.setModel(listModel);
        initToolbar();
        initFilter();
		menu = new Menu();
		menu.getRedirectRules().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getRedirectRulesDialog().setVisible(true);
			}
		});
		listConnection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
					Connection connection = listModel.getConnectionStatus(listConnection.getSelectedIndex());
					MessageListModel defaultListModel = new MessageListModel(connection);
					listMessage.setModel(defaultListModel);
					tabbedPane1.setSelectedIndex(1);
				}
			}
		});
		listMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
					Transmit transmit = (Transmit) listMessage.getSelectedValue();
					getMessageDetailDialog().setTransmit(transmit);
					getMessageDetailDialog().setVisible(true);
				}
			}
		});

		startProxy();
	}

    private void initFilter() {
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

    private void initToolbar() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!socksProxy.isRunning()) {
                    if (!startProxy()) {
                        return;
                    }
                }
                Configs.getInstance().setRecord(true);
                startButton.setVisible(false);
                stopButton.setVisible(true);
                stopButton.grabFocus();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Configs.getInstance().setRecord(false);
                stopButton.setVisible(false);
                startButton.setVisible(true);
                startButton.grabFocus();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.clear();
                ConnectionMonitor.getInstance().clear();
            }
        });
    }

    private boolean startProxy() {
		try {
			socksProxy.start();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panel, e);
			startButton.setVisible(true);
			stopButton.setVisible(false);
			return false;
		}
	}

	public RedirectRulesDialog getRedirectRulesDialog() {
		if (redirectRulesDialog == null) {
			redirectRulesDialog = new RedirectRulesDialog();
			redirectRulesDialog.pack();
			redirectRulesDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
		return redirectRulesDialog;
	}

	public MessageDetailDialog getMessageDetailDialog() {
		if (messageDetailDialog == null) {
			messageDetailDialog = new MessageDetailDialog();
			messageDetailDialog.pack();
			messageDetailDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
		return messageDetailDialog;
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
