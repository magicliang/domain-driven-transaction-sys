package com.magicliang.transaction.sys.core.service;

/**
 * 数据库表计数查询服务
 *
 * @author magicliang
 * date: 2026-03-23
 */
public interface IDatabaseCountService {

    /**
     * 查询指定表的总记录数
     *
     * @param tableName 表名
     * @return 表总记录数
     */
    long getTotalCount(String tableName);
}
