package com.dianping.mocksocks.transport.dao;

import org.apache.commons.dbutils.DbUtils;
import org.h2.jdbcx.JdbcDataSource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yihua.huang@dianping.com
 */
public class BaseDao {

	private static AtomicBoolean connected = new AtomicBoolean(false);

    private static JdbcDataSource jdbcDataSource;

    protected void initDatabase() {
		if (connected.compareAndSet(false, true)) {
			try {
				DbUtils.loadDriver("org.h2.Driver");
				jdbcDataSource = new JdbcDataSource();
				jdbcDataSource.setURL("jdbc:h2:~/test2");
				jdbcDataSource.setUser("sa");
				jdbcDataSource.setPassword("");
			} catch (Exception e) {
				connected.compareAndSet(true, false);
			}
		}
	}

    protected JdbcDataSource getJdbcDataSource() {
        return jdbcDataSource;
    }
}
