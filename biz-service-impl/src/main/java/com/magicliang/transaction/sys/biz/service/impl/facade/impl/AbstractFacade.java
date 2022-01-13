package com.magicliang.transaction.sys.biz.service.impl.facade.impl;

import com.magicliang.transaction.sys.biz.shared.locator.CommandQueryBus;
import com.magicliang.transaction.sys.core.service.IDistributedLock;
import com.magicliang.transaction.sys.core.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 抽象门面
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:50
 */
public class AbstractFacade {

    /**
     * 命令查询总线
     */
    @Autowired
    protected CommandQueryBus commandQueryBus;

    /**
     * 分布式锁
     */
    @Autowired
    protected IDistributedLock distributedLock;

    /**
     * 支付订单服务，未来要移动进 query handler 里去
     */
    @Autowired
    protected IPayOrderService payOrderService;
}
