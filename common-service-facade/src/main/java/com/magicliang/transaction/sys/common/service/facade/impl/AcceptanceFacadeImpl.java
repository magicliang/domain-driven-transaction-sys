package com.magicliang.transaction.sys.common.service.facade.impl;

import com.magicliang.transaction.sys.biz.service.impl.request.acceptance.AcceptanceCommand;
import com.magicliang.transaction.sys.common.service.facade.IAcceptanceFacade;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理门面实现
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:55
 */
@Slf4j
@Service
public class AcceptanceFacadeImpl extends AbstractFacade implements IAcceptanceFacade {

    /**
     * 受理支付订单
     *
     * @param acceptanceCommand 受理命令
     * @return 交易模型
     */
    @Override
    public TransactionModel acceptPayOrder(AcceptanceCommand acceptanceCommand) {
        return commandQueryBus.send(acceptanceCommand);
    }
}
