package com.dianping.mocksocks.transport.monitor.config;

import com.dianping.mocksocks.transport.dao.BaseDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author yihua.huang@dianping.com
 */
public class RulesDao extends BaseDao {

	public static final String TYPE_REDIRECT = "redirect";

	public RulesDao() {
		initDatabase();
	}

	public String getByType(String type) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(getJdbcDataSource());
		Map<String, Object> query = queryRunner.query("select content from Rules where type = ? ", new MapHandler(),
				type);
		if (query == null || query.get("content") == null) {
			return "";
		}
		return (String) query.get("content");
	}

	public void setByType(String type, String content) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(getJdbcDataSource());
		queryRunner.update("merge into Rules key(type) values (?,?)", type, content);
	}

}
