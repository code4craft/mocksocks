package com.dianping.mocksocks.gui.gui;

import com.dianping.mocksocks.proxy.rules.RedirectRules;

import javax.swing.*;
import java.awt.event.*;

public class RedirectRulesDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextArea textArea1;

	public RedirectRulesDialog() {
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
				onCancel();
			}
		});

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					RedirectRules.getInstance().setFilters(RedirectRules.parse(textArea1.getText()));
				} catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showConfirmDialog(contentPane, ex);
				}
			}
		});
	}

	private void onOK() {
		// add your code here
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	public static void main(String[] args) {
		RedirectRulesDialog dialog = new RedirectRulesDialog();
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
