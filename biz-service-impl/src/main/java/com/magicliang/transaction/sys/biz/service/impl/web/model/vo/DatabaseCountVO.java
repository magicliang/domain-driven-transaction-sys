package com.magicliang.transaction.sys.biz.service.impl.web.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据库表计数查询响应
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Data
public class DatabaseCountVO implements Serializable {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 总记录数
     */
    private Long totalCount;

    public DatabaseCountVO() {
    }

    public DatabaseCountVO(String tableName, Long totalCount) {
        this.tableName = tableName;
        this.totalCount = totalCount;
    }
}
