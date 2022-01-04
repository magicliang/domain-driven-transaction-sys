package com.magicliang.transaction.sys.core.domain.strategy;

import com.magicliang.transaction.sys.common.type.IIdentifiableType;
import com.magicliang.transaction.sys.core.model.request.IRequest;
import com.magicliang.transaction.sys.core.model.response.IResponse;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 领域策略接口
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 13:57
 */
public interface DomainStrategy<Req extends IRequest, Res extends IResponse, E extends Enum> extends IIdentifiableType<E> {

    /**
     * 执行领域请求，生成领域响应
     *
     * @param req 领域请求
     * @param res 领域响应
     */
    void execute(Req req, Res res);

    /**
     * 由策略点枚举确定本策略是否会被激活
     *
     * @param strategyEnum 策略点枚举
     * @return 本策略是否会被激活
     */
    default boolean isSupport(E strategyEnum) {
        return identify() == strategyEnum;
    }
}
