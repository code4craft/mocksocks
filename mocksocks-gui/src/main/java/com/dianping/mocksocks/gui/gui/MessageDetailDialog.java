package com.dianping.mocksocks.gui.gui;

import com.dianping.mocksocks.proxy.message.Exchange;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MessageDetailDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTabbedPane tabbedPane1;
	private JEditorPane editorRequest;
	private JEditorPane editorResponse;
	private JTabbedPane requestTab;
	private JEditorPane editorRequestHex;
	private JTabbedPane responseTab;
	private Exchange exchange;

	public MessageDetailDialog() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
		editorRequest.setText(exchange.getRequest().textOutput());
		if (exchange.getResponse() != null) {
			editorResponse.setText(exchange.getResponse().textOutput());
		}
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	private void onOK() {
		// add your code here
		dispose();
	}

	public static void main(String[] args) {
		MessageDetailDialog dialog = new MessageDetailDialog();
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
