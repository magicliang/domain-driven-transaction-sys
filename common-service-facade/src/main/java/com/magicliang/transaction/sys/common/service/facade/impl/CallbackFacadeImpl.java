package com.magicliang.transaction.sys.common.service.facade.impl;

import com.magicliang.transaction.sys.biz.service.impl.request.callback.CallbackCommand;
import com.magicliang.transaction.sys.common.service.facade.ICallbackFacade;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:56
 */
@Slf4j
@Service
public class CallbackFacadeImpl extends AbstractFacade implements ICallbackFacade {

    /**
     * 执行回调，更新支付订单
     *
     * @param callbackCommand 回调命令
     * @return 交易模型
     */
    @Override
    public TransactionModel callback(final CallbackCommand callbackCommand) {
        return commandQueryBus.send(callbackCommand);
    }
}
