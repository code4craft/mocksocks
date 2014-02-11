package com.dianping.mocksocks.gui.data;

import com.dianping.mocksocks.transport.Connection;

import javax.swing.*;

/**
 * @author yihua.huang@dianping.com
 */
public class MessageListModel extends AbstractListModel {

    private Connection connection;

    public MessageListModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int getSize() {
        return connection.getMessages().size();
    }

    @Override
    public Object getElementAt(int index) {
        return connection.getMessages().get(index);
    }
}
