package com.dianping.mocksocks.gui.gui;

import com.dianping.mocksocks.transport.monitor.config.RulesDao;
import com.dianping.mocksocks.transport.rules.RulesContainer;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class RedirectRulesDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextArea textArea1;

	public RedirectRulesDialog() {
        try {
            String redirectRules = new RulesDao().getByType(RulesDao.TYPE_REDIRECT);
            textArea1.setText(redirectRules);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

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
                    RulesContainer.getInstance().setRedirectRules(RulesContainer.parse(textArea1.getText()));
                    new RulesDao().setByType(RulesDao.TYPE_REDIRECT, textArea1.getText());
                    dispose();
				} catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(contentPane, ex);
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
