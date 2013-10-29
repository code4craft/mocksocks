package com.dianping.mocksocks.gui.data;

import com.dianping.mocksocks.proxy.monitor.ConnectionStatus;

import javax.swing.*;

/**
 * @author yihua.huang@dianping.com
 */
public class MessageListModel extends AbstractListModel {

    private ConnectionStatus connectionStatus;

    public MessageListModel(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @Override
    public int getSize() {
        return connectionStatus.getMessages().size();
    }

    @Override
    public Object getElementAt(int index) {
        return connectionStatus.getMessages().get(index);
    }
}
