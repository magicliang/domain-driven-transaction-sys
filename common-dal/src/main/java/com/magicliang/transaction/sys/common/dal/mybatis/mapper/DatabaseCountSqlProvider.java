package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import org.apache.ibatis.jdbc.SQL;

/**
 * 数据库表计数查询 SQL 提供者
 *
 * @author magicliang
 * date: 2026-03-23
 */
public class DatabaseCountSqlProvider {

    /**
     * 构建按表名计数的 SQL 语句
     * 注意：tableName 参数必须经过严格校验，防止 SQL 注入
     *
     * @param tableName 表名
     * @return COUNT SQL 语句
     */
    public String countByTableName(String tableName) {
        SQL sql = new SQL();
        sql.SELECT("COUNT(*)");
        sql.FROM(tableName);
        return sql.toString();
    }
}
