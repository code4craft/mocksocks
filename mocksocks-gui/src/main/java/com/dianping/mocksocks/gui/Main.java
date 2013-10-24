package com.dianping.mocksocks.gui;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author yihua.huang@dianping.com
 */
public class Main {
    private JList list1;
    private JPanel panel;
    private JFormattedTextField sdasdsadFormattedTextField;

    public Main() {
        list1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println(e.getKeyChar());
                super.keyTyped(e);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
