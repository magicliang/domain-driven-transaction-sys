package com.magicliang.transaction.sys.core.model.entity.validator;

import com.magicliang.transaction.sys.common.enums.TransRequestTypeEnum;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_PAYMENT_REQUEST_ERROR;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易请求校验器
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 11:35
 */
public class TransRequestValidator {

    /**
     * 私有构造器
     */
    private TransRequestValidator() {
        throw new UnsupportedOperationException();
    }

    /**
     * 插入前校验支付订单实体完整性
     *
     * @param request 支付订单
     */
    public static void validateBeforeInsert(TransRequestEntity request) {
        AssertUtils.assertNotNull(request.getPayOrderNo(), INVALID_PAYMENT_REQUEST_ERROR, "invalid payOrderNo:" + request);
        AssertUtils.assertNotNull(TransRequestTypeEnum.getByCode(request.getRequestType().intValue()), INVALID_PAYMENT_REQUEST_ERROR, "invalid requestType:" + request);
        AssertUtils.assertNotBlank(request.getBizIdentifyNo(), INVALID_PAYMENT_REQUEST_ERROR, "invalid bizIdentifyNo:" + request);
        AssertUtils.assertNotBlank(request.getRequestAddr(), INVALID_PAYMENT_REQUEST_ERROR, "invalid requestAddr:" + request);
    }
}
