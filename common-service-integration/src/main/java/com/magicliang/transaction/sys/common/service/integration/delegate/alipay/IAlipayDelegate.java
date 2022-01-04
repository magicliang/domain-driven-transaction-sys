package com.magicliang.transaction.sys.common.service.integration.delegate.alipay;

import com.magicliang.transaction.sys.common.service.integration.param.AlipayReqParam;
import com.magicliang.transaction.sys.common.service.integration.param.AlipayResDto;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝委托接口
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 14:16
 */
public interface IAlipayDelegate {

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
    AlipayResDto standardPay(final AlipayReqParam param, final String signatureKey, final String notifyUri);
}
