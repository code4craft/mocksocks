package com.dianping.mocksocks.gui.gui;

import com.dianping.mocksocks.transport.Transmit;

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
	private JEditorPane editorResponseHex;
	private Transmit transmit;

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

	public void setTransmit(Transmit transmit) {
		this.transmit = transmit;
		if (transmit.getRequest() != null) {
			editorRequest.setText(transmit.getRequest().textOutput());
			editorRequestHex.setText(transmit.getRequest().hexOutput());
		} else {
			editorRequest.setText("");
			editorRequestHex.setText("");
		}
		if (transmit.getResponse() != null) {
			editorResponse.setText(transmit.getResponse().textOutput());
			editorResponseHex.setText(transmit.getResponse().hexOutput());
		} else {
			editorResponse.setText("");
			editorResponseHex.setText("");
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
