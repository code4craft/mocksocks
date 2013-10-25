package com.dianping.mocksocks.gui.data;

import com.dianping.mocksocks.proxy.monitor.ConnectionMonitor;
import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;
import com.dianping.mocksocks.proxy.rules.filter.Filter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatusListModel extends AbstractListModel {

	private List<String> display = new ArrayList<String>();

	private List<Filter<ConnectionStatus>> filters = new ArrayList<Filter<ConnectionStatus>>();

	public ConnectionStatusListModel(final int refreshTime) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(refreshTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					update();
				}
			}

		}).start();
	}

	public synchronized void update() {
		long time = System.currentTimeMillis();
		display.clear();
		connectionLoop: for (ConnectionStatus connectionStatus : ConnectionMonitor.getInstance().status()) {
			if (filters.size() > 0) {
				for (Filter<ConnectionStatus> filter : filters) {
					if (!filter.preserve(connectionStatus)) {
						continue connectionLoop;
					}
				}
			}
			display.add(connectionStatus.toString());
		}
		fireContentsChanged(this, 0, display.size());
		System.out.println("time takes " + (System.currentTimeMillis() - time));
	}

	@Override
	public int getSize() {
		return display.size();
	}

	public void setFilters(List<Filter<ConnectionStatus>> filters) {
		this.filters = filters;
	}

	public void setFilter(Filter<ConnectionStatus> filter) {
		this.filters.clear();
		filters.add(filter);
	}

	@Override
	public Object getElementAt(int index) {
		return display.get(index);
	}

	public void clear() {
		display.clear();
		fireContentsChanged(this, 0, 1);
	}
}
