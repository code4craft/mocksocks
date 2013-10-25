package com.dianping.mocksocks.proxy.dao;

import org.junit.Test;

import java.sql.SQLException;

/**
 * @author yihua.huang@dianping.com
 */
public class RulesDaoTest {

    @Test
    public void test() throws SQLException {
        RulesDao rulesDao = new RulesDao();
        rulesDao.setByType("数据","测试");
        String 数据 = rulesDao.getByType("数据");
        System.out.println(数据);
    }
}
