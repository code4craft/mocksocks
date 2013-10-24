package com.dianping.mocksocks.gui.data;

import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import javax.swing.*;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatusListModel extends AbstractListModel {

	public ConnectionStatusListModel(final int refreshTime) {
		new Thread(new Runnable() {
			@Override
			public void run() {
                while (true){
                    try {
                        Thread.sleep(refreshTime);
                    } catch (InterruptedException e) {
						e.printStackTrace();
                    }
                    fireContentsChanged(this, 0, ConnectionMonitor.status().size());
                }
			}
		}).start();
	}

	@Override
	public int getSize() {
		return ConnectionMonitor.status().size();
	}

	@Override
	public Object getElementAt(int index) {
		return ConnectionMonitor.status().get(index);
	}
}
