package com.dianping.mocksocks.gui.data;

import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatusListModel extends AbstractListModel {

    private List<String> display = new ArrayList<String>();

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
                    display.clear();
                    for (ConnectionStatus connectionStatus : ConnectionMonitor.status()) {
                        display.add(connectionStatus.toString());
                    }
                    fireContentsChanged(this, 0, display.size());
                }
			}
		}).start();
	}

	@Override
	public int getSize() {
		return display.size();
	}

	@Override
	public Object getElementAt(int index) {
		return display.get(index);
	}

    public void clear(){
        display.clear();
        fireContentsChanged(this, 0, 1);
    }
}
