package com.magicliang.transaction.sys.core.model.entity.validator;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_SUB_ORDER_ERROR;

import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.model.entity.TransAlipaySubOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransSubOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付子订单校验器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 11:26
 */
public class TransSubOrderValidator {

    /**
     * 私有构造器
     */
    private TransSubOrderValidator() {
        throw new UnsupportedOperationException();
    }

    /**
     * 插入前校验支付订单实体完整性
     *
     * @param subOrder 支付订单
     */
    public static void validateBeforeInsert(TransSubOrderEntity subOrder) {
        AssertUtils.assertNotNull(subOrder, INVALID_SUB_ORDER_ERROR, "invalid payOrderNo:" + subOrder);
        AssertUtils.assertNotNull(subOrder.getPayOrderNo(), INVALID_SUB_ORDER_ERROR, "invalid payOrderNo:" + subOrder);
        AssertUtils.assertNotNull(subOrder.getGmtCreated(), INVALID_SUB_ORDER_ERROR, "invalid gmtCreated:" + subOrder);
        AssertUtils.assertNotNull(subOrder.getGmtModified(), INVALID_SUB_ORDER_ERROR,
                "invalid gmtModified:" + subOrder);

        if (subOrder instanceof TransAlipaySubOrderEntity) {
            TransAlipaySubOrderValidator.validateBeforeInsert((TransAlipaySubOrderEntity) subOrder);
        }
    }
}
