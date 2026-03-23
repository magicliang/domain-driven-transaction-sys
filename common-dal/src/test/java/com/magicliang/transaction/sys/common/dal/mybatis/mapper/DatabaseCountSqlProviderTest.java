package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabaseCountSqlProvider 单元测试
 *
 * @author magicliang
 * date: 2026-03-23
 */
class DatabaseCountSqlProviderTest {

    @Test
    void testCountByTableName() {
        DatabaseCountSqlProvider provider = new DatabaseCountSqlProvider();
        String sql = provider.countByTableName("tb_trans_pay_order");

        assertNotNull(sql);
        assertTrue(sql.contains("COUNT(*)"));
        assertTrue(sql.contains("tb_trans_pay_order"));
        assertTrue(sql.contains("SELECT"));
        assertTrue(sql.contains("FROM"));
    }

    @Test
    void testCountByTableNameWithUnderscore() {
        DatabaseCountSqlProvider provider = new DatabaseCountSqlProvider();
        String sql = provider.countByTableName("tb_test_table");

        assertTrue(sql.contains("COUNT(*)"));
        assertTrue(sql.contains("tb_test_table"));
    }
}
