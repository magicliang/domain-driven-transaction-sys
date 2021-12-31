package com.magicliang.transaction.sys.core.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易系统子订单
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 16:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransSubOrderEntity extends BaseEntity {

    /**
     * 引用 tb_pay_order 支付订单号，业务主键，全局唯一
     */
    private Long payOrderNo;
}
