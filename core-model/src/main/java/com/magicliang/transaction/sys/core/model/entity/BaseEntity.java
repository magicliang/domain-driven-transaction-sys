package com.magicliang.transaction.sys.core.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基础实体
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 14:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    /**
     * 自增物理主键，单表唯一
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 最后修改时间
     */
    private Date gmtModified;
}
