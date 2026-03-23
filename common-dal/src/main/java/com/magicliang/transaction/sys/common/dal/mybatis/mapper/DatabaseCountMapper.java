package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * 数据库表计数查询 Mapper
 *
 * @author magicliang
 * date: 2026-03-23
 */
public interface DatabaseCountMapper {

    /**
     * 根据表名查询总记录数
     *
     * @param tableName 表名（需经过安全校验）
     * @return 表总记录数
     */
    @SelectProvider(type = DatabaseCountSqlProvider.class, method = "countByTableName")
    long countByTableName(@Param("tableName") String tableName);
}
