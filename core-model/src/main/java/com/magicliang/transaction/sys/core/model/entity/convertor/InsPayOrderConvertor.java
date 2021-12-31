package com.magicliang.transaction.sys.core.model.entity.convertor;

import com.magicliang.transaction.sys.common.dal.po.TransAlipaySubOrderPo;
import com.magicliang.transaction.sys.common.dal.po.TransPayOrderPo;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单转换器
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 16:36
 */
public class InsPayOrderConvertor {

    /**
     * 私有构造器
     */
    private InsPayOrderConvertor() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从领域模型转化为持久层对象
     * 注意，所有的非 insert 的 update 流程在调用 toPo 之前必须调用 PayOrderHelper.updatePayOrder
     *
     * @param insPayOrderEntity 领域模型
     * @return 持久层对象
     */
    public static TransPayOrderPo toPo(TransPayOrderEntity insPayOrderEntity) {
        return null;
    }

    /**
     * 从持久层对象转化为领域模型
     *
     * @param payOrder 持久层对象
     * @return 领域模型
     */
    public static TransPayOrderEntity toDomainEntity(TransPayOrderPo payOrder) {
        return null;
    }

    /**
     * 转换支付订单 + 钱袋宝支付子订单持久层对象到领域模型实体，本方法包含子订单
     *
     * @param payOrder 支付订单持久层对象
     * @param subOrder 钱袋宝支付子订单持久层对象
     * @return 领域模型实体
     */
    public static TransPayOrderEntity toDomainEntity(final TransPayOrderPo payOrder, final TransAlipaySubOrderPo subOrder) {
        TransPayOrderEntity insPayOrderEntity = toDomainEntity(payOrder);
        insPayOrderEntity.setSubOrder(TransAlipaySubOrderConvertor.toDomainEntity(subOrder));
        return insPayOrderEntity;
    }

}
