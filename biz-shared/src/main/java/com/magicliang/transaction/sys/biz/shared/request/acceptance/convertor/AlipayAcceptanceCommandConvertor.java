package com.magicliang.transaction.sys.biz.shared.request.acceptance.convertor;

import com.magicliang.transaction.sys.biz.shared.request.acceptance.AlipayAcceptanceCommand;
import com.magicliang.transaction.sys.core.model.entity.TransSubOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝余额受理命令转换器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 14:27
 */
public class AlipayAcceptanceCommandConvertor {

    /**
     * 私有构造器
     */
    private AlipayAcceptanceCommandConvertor() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从原始命令转换到领域模型
     *
     * @param command 原始命令
     * @return 领域模型
     */
    public static TransSubOrderEntity toDomainEntity(AlipayAcceptanceCommand command) {
        return null;
    }
}
