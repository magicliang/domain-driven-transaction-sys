package com.magicliang.transaction.sys.common.service.integration.delegate.alipay.impl;

import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.common.service.integration.delegate.alipay.IAlipayDelegate;
import com.magicliang.transaction.sys.common.service.integration.dto.AlipayReqParam;
import com.magicliang.transaction.sys.common.service.integration.dto.AlipayResDto;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.PAYMENT_PLATFORM_BIZ_ERROR;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝委托实现
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 14:28
 */
@Slf4j
@Component
public class AlipayDelegateImpl implements IAlipayDelegate {

    // 要准备本子域请求和响应专有的静态常量

    // 远端 client 的 skeleton

    /**
     * 标准支付：还可以衍生出支付动作的变种：支付到银行卡和支付到余额
     *
     * @param param        转账参数
     * @param signatureKey 签名密钥
     * @param notifyUri    回调 uri
     * @return 转账结果
     * @see {@link 一个链接}
     * 一般转账接口都需要考虑跨支付通道支付时的鉴权和回调问题
     * 用鉴权签名防止请求被篡改，在清结算和支付领域的标准做法
     */
    @Override
    public AlipayResDto standardPay(AlipayReqParam param, String signatureKey, String notifyUri) {
        // 1. 校验参数，使用 AssertUtils
        // 2. 构造并签名请求
        // 3. 真请求流程
        AlipayResDto ret = null;
        try {
//            return AlipayResDto.Builder.buildFailureResult(signRes.getTaskId(), signRes.getErrorCode(), signRes.getErrorMessage());
        } finally {
            log.info("{}, {}", JsonUtils.toJson(param), JsonUtils.toJson(ret));
        }

        throw new BaseTransException(PAYMENT_PLATFORM_BIZ_ERROR, "付款平台付款错误，invalid res：");
    }
}
