package com.magicliang.transaction.sys.biz.service.impl.facade;

import com.magicliang.transaction.sys.biz.shared.request.callback.CallbackCommand;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 回调门面
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:47
 */
public interface ICallbackFacade {

    /**
     * 执行回调，更新支付订单
     *
     * @param callbackCommand 回调命令
     * @return 交易模型
     */
    TransactionModel callback(CallbackCommand callbackCommand);
}
