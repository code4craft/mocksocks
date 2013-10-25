package com.dianping.mocksocks.gui.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author yihua.huang@dianping.com
 */
public class Menu extends JMenuBar {

	private JMenu proxy;

	private JMenuItem startProxy;

	private JMenuItem stopProxy;

	private JCheckBoxMenuItem macOsProxy;

	private JMenu rules;

	private JMenuItem redirectRules;

	public Menu() {
		proxy = new JMenu("Proxy");
		startProxy = new JMenuItem("Start", KeyEvent.VK_S);
        stopProxy = new JMenuItem("Stop", KeyEvent.VK_T);
        macOsProxy = new JCheckBoxMenuItem("Mac OX proxy",false);
        macOsProxy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                       //networksetup -setwebproxy
                //http://hints.macworld.com/article.php?story=2003101617122867
            }
        });
        proxy.add(startProxy);
        proxy.add(stopProxy);
        proxy.add(macOsProxy);
        this.add(proxy);
		rules = new JMenu("Rule");
		redirectRules = new JMenuItem("Redirect", KeyEvent.VK_R);
		rules.add(redirectRules);
		this.add(rules);

	}

	public JMenu getRules() {
		return rules;
	}

	public JMenuItem getRedirectRules() {
		return redirectRules;
	}

    public JMenu getProxy() {
        return proxy;
    }

    public JMenuItem getStartProxy() {
        return startProxy;
    }

    public JMenuItem getStopProxy() {
        return stopProxy;
    }

    public JCheckBoxMenuItem getMacOsProxy() {
        return macOsProxy;
    }
}
