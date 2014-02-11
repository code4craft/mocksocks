package com.dianping.mocksocks.transport.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author yihua.huang@dianping.com
 */
public class BaseDapTest {

    @Ignore
    @Test
    public void testDatasource() throws SQLException {
        BaseDao baseDao = new BaseDao();
        baseDao.initDatabase();
        QueryRunner queryRunner = new QueryRunner(baseDao.getJdbcDataSource());
        queryRunner.update("CREATE TABLE TEST(ID INT PRIMARY KEY,\n" +
                "   NAME VARCHAR(255));");
    }
}
