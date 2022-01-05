package com.magicliang.transaction.sys.biz.service.impl.locator;

import com.magicliang.transaction.sys.biz.service.impl.request.HandlerRequest;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 总线接口
 *
 * @param <T> 交易模型类型参数
 * @param <R> 请求类型参数
 * @author magicliang
 * <p>
 * date: 2022-01-05 15:01
 */
public interface IBus<T extends TransactionModel, R extends HandlerRequest> {
    /**
     * 分发请求，获取交易模型
     *
     * @param req 请求实参
     * @return 交易模型
     */
    T send(R req);
}