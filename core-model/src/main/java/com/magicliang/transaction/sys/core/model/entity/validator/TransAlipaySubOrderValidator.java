package com.magicliang.transaction.sys.core.model.entity.validator;

import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.model.entity.TransAlipaySubOrderEntity;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_SUB_ORDER_ERROR;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝子订单校验器
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 11:27
 */
public class TransAlipaySubOrderValidator {

    /**
     * 私有构造器
     */
    private TransAlipaySubOrderValidator() {
        throw new UnsupportedOperationException();
    }

    /**
     * 插入前校验支付订单实体完整性
     *
     * @param subOrder 支付订单
     */
    public static void validateBeforeInsert(TransAlipaySubOrderEntity subOrder) {
        AssertUtils.assertNotBlank(subOrder.getAliPayAccount(), INVALID_SUB_ORDER_ERROR, "invalid aliPayAccount:" + subOrder);
        // 其他差异化校验
    }
}
