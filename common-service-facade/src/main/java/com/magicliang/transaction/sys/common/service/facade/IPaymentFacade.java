package com.magicliang.transaction.sys.common.service.facade;

import com.magicliang.transaction.sys.biz.service.impl.request.payment.PaymentCommand;
import com.magicliang.transaction.sys.biz.service.impl.request.payment.UnPaidOrderQuery;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;

import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付门面
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:40
 */
public interface IPaymentFacade {

    /**
     * 查询并支付全部未支付订单
     *
     * @param unPaidOrderQuery 未支付订单查询
     * @return 操作结果
     */
    boolean batchPay(UnPaidOrderQuery unPaidOrderQuery);

    /**
     * 批量支付订单，使用线程池来执行支付任务
     *
     * @param payOrders 待支付订单列表
     */
    void batchPay(List<TransPayOrderEntity> payOrders);

    /**
     * 支付单一订单
     *
     * @param paymentCommand 支付命令
     * @return 交易模型
     */
    TransactionModel payAndNotify(PaymentCommand paymentCommand);

    /**
     * 支付单一订单
     *
     * @param paymentCommand 支付命令
     * @return 交易模型
     */
    TransactionModel pay(PaymentCommand paymentCommand);

    /**
     * 异步支付单一订单
     *
     * @param paymentCommand 支付命令
     */
    void asyncPay(PaymentCommand paymentCommand);
}
