package com.magicliang.transaction.sys.core.domain.strategy;

import com.magicliang.transaction.sys.core.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基础策略
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 14:03
 */
public abstract class BaseStrategy {

    /**
     * 支付订单服务
     */
    @Autowired
    protected IPayOrderService payOrderService;
}
