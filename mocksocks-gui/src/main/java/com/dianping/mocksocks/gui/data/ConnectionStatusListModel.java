package com.dianping.mocksocks.gui.data;

import com.dianping.mocksocks.transport.Connection;
import com.dianping.mocksocks.transport.monitor.ConnectionMonitor;
import com.dianping.mocksocks.transport.rules.filter.Filter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihua.huang@dianping.com
 */
public class ConnectionStatusListModel extends AbstractListModel {

	private List<String> display = new ArrayList<String>();

    private List<Connection> connectionStatuses = new ArrayList<Connection>();

	private List<Filter<Connection>> filters = new ArrayList<Filter<Connection>>();

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
		display.clear();
        connectionStatuses.clear();
		connectionLoop: for (Connection connection : ConnectionMonitor.getInstance().status()) {
			if (filters.size() > 0) {
				for (Filter<Connection> filter : filters) {
					if (!filter.preserve(connection)) {
						continue connectionLoop;
					}
				}
			}
            connectionStatuses.add(connection);
			display.add(connection.toString());
		}
		fireContentsChanged(this, 0, display.size());
	}

	@Override
	public int getSize() {
		return display.size();
	}

	public void setFilters(List<Filter<Connection>> filters) {
		this.filters = filters;
	}

	public void setFilter(Filter<Connection> filter) {
		this.filters.clear();
		filters.add(filter);
	}

	@Override
	public Object getElementAt(int index) {
		return display.get(index);
	}

    public Connection getConnectionStatus(int index) {
        return connectionStatuses.get(index);
    }

    public void clear() {
		display.clear();
		fireContentsChanged(this, 0, 1);
	}
}
