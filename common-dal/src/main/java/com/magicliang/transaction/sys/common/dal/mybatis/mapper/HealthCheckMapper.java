package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * 健康检查 Mapper
 * 用于执行轻量级 SQL 查询验证数据库连接
 *
 * @author magicliang
 * date: 2026-03-23
 */
public interface HealthCheckMapper {

    /**
     * 执行健康检查 SQL（SELECT 1）
     *
     * @return 固定返回 1
     */
    @Select("SELECT 1")
    Integer executeHealthCheck();
}
