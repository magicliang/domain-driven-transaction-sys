package com.magicliang.transaction.sys.biz.shared.request.payment;

import com.magicliang.transaction.sys.biz.shared.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.shared.request.HandlerQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 未支付订单查询请求
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 14:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UnPaidOrderQuery extends HandlerQuery {

    /**
     * 批次大小
     */
    private int batchSize;

    /**
     * 环境
     */
    private int env;

    /**
     * 标识自己的类型。
     *
     * @return 类型
     */
    @Override
    public OperationEnum identify() {
        return OperationEnum.QUERY_UNPAID_ORDERS;
    }
}