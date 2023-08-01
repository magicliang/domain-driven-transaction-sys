package com.magicliang.transaction.sys.biz.service.impl.facade;

import com.magicliang.transaction.sys.biz.shared.request.acceptance.AcceptanceCommand;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理门面
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 16:47
 */
public interface IAcceptanceFacade {

    /**
     * 受理支付订单
     *
     * @param acceptanceCommand 受理命令
     * @return 交易模型
     */
    TransactionModel acceptPayOrder(AcceptanceCommand acceptanceCommand);
}
