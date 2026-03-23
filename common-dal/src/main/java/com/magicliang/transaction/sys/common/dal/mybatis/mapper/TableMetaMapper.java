package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据库元数据查询 Mapper
 * 用于获取表名白名单
 *
 * @author magicliang
 * date: 2026-03-23
 */
public interface TableMetaMapper {

    /**
     * 查询数据库中所有用户表名
     *
     * @return 表名列表
     */
    @Select("SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_SCHEMA = DATABASE()")
    List<String> selectAllTableNames();
}
