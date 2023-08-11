package com.magicliang.transaction.sys.core.model.entity.validator;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_PAY_ORDER_ERROR;

import com.magicliang.transaction.sys.common.enums.TransFundAccountingEntryTypeEnum;
import com.magicliang.transaction.sys.common.enums.TransSysConfigEnum;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易订单校验器
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:10
 */
public class TransPayOrderValidator {

    /**
     * 私有构造器
     */
    private TransPayOrderValidator() {
        throw new UnsupportedOperationException();
    }

    /**
     * 插入前校验支付订单实体完整性
     *
     * @param payOrder 支付订单
     */
    public static void validateBeforeInsert(TransPayOrderEntity payOrder) {
        AssertUtils.assertNotNull(payOrder, INVALID_PAY_ORDER_ERROR, "invalid payOrder：" + payOrder);
        AssertUtils.assertNotNull(payOrder.getPayOrderNo(), INVALID_PAY_ORDER_ERROR, "invalid payOrderNo：" + payOrder);
        AssertUtils.assertNotNull(TransSysConfigEnum.getByCode(payOrder.getSysCode()), INVALID_PAY_ORDER_ERROR,
                "invalid sysCode：" + payOrder);

        AssertUtils.assertNotBlank(payOrder.getBizIdentifyNo(), INVALID_PAY_ORDER_ERROR,
                "invalid bizIdentifyNo：" + payOrder);
        AssertUtils.assertNotBlank(payOrder.getBizUniqueNo(), INVALID_PAY_ORDER_ERROR,
                "invalid bizUniqueNo：" + payOrder);

        final Long money = payOrder.getMoney();
        AssertUtils.isTrue(null != money && money > 0L, INVALID_PAY_ORDER_ERROR, "invalid money：" + payOrder);

        AssertUtils.assertNotNull(TransFundAccountingEntryTypeEnum.getByCode(payOrder.getAccountingEntry().intValue()),
                INVALID_PAY_ORDER_ERROR, "invalid accountingEntry：" + payOrder);
        final String notifyUri = payOrder.getNotifyUri();
        AssertUtils.assertNotBlank(notifyUri, INVALID_PAY_ORDER_ERROR, "invalid notifyUri：" + payOrder);
    }
}
