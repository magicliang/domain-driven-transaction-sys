package com.magicliang.transaction.sys.core.model.entity.convertor;

import com.magicliang.transaction.sys.common.dal.po.TransAlipaySubOrderPo;
import com.magicliang.transaction.sys.core.model.entity.TransAlipaySubOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝子订单转换器
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 16:51
 */
public class TransAlipaySubOrderConvertor {

    /**
     * 私有构造器
     */
    private TransAlipaySubOrderConvertor() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从领域模型转化为持久层对象
     *
     * @param subOrder 领域模型
     * @return 持久层对象
     */
    public static TransAlipaySubOrderPo toPo(final TransAlipaySubOrderEntity subOrder) {
        return null;
    }

    /**
     * 从持久层对象转化为领域模型
     *
     * @param subOrder 持久层对象
     * @return 领域模型
     */
    public static TransAlipaySubOrderEntity toDomainEntity(final TransAlipaySubOrderPo subOrder) {
        return null;
    }
}
