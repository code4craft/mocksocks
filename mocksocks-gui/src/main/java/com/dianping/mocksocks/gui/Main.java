package com.dianping.mocksocks.gui;

import javax.swing.*;

/**
 * @author yihua.huang@dianping.com
 */
public class Main {
    private JList list1;
    private JPanel panel;
    private JFormattedTextField asFormattedTextField;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
