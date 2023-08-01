package com.magicliang.transaction.sys.core.domain.strategy.acceptance;

import com.magicliang.transaction.sys.common.dal.mybatis.po.TransAlipaySubOrderPo;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransChannelRequestPoWithBLOBs;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransPayOrderPo;
import com.magicliang.transaction.sys.core.domain.enums.AcceptanceStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.BaseStrategy;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.manager.PayOrderManager;
import com.magicliang.transaction.sys.core.model.entity.TransAlipaySubOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.TransSubOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.convertor.TransAlipaySubOrderConvertor;
import com.magicliang.transaction.sys.core.model.entity.convertor.TransPayOrderConvertor;
import com.magicliang.transaction.sys.core.model.entity.convertor.TransRequestConvertor;
import com.magicliang.transaction.sys.core.model.request.acceptance.AcceptanceRequest;
import com.magicliang.transaction.sys.core.model.response.acceptance.AcceptanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 同步受理策略
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 16:51
 */
@Slf4j
@Component
public class SyncAcceptanceStrategy extends BaseStrategy implements
        DomainStrategy<AcceptanceRequest, AcceptanceResponse, AcceptanceStrategyEnum> {

    /**
     * payOrderManager
     */
    @Autowired
    private PayOrderManager payOrderManager;

    /**
     * 标识自己的类型
     *
     * @return 类型
     */
    @Override
    public AcceptanceStrategyEnum identify() {
        return AcceptanceStrategyEnum.SYNC;
    }

    /**
     * 执行领域请求，生成领域响应
     *
     * @param acceptanceRequest 领域请求
     * @param acceptanceResponse 领域响应
     */
    @Override
    public void execute(final AcceptanceRequest acceptanceRequest, final AcceptanceResponse acceptanceResponse) {
        TransPayOrderEntity payOrder = acceptanceRequest.getTransPayOrder();
        TransSubOrderEntity subOrder = payOrder.getSubOrder();
        TransRequestEntity paymentRequest = payOrder.getPaymentRequest();

        TransPayOrderPo payOrderPo = TransPayOrderConvertor.toPo(payOrder);
        TransChannelRequestPoWithBLOBs paymentRequestPo = TransRequestConvertor.toPo(paymentRequest);

        // 在一个事务里插入三条记录，不漏不重地生成子实体
        if (subOrder instanceof TransAlipaySubOrderEntity) {
            TransAlipaySubOrderPo subOrderPo = TransAlipaySubOrderConvertor.toPo((TransAlipaySubOrderEntity) subOrder);
            payOrderManager.insertPayOrder(payOrderPo, paymentRequestPo, subOrderPo);
            subOrder.setId(subOrderPo.getId());
        }
        payOrder.setId(payOrderPo.getId());
        paymentRequest.setId(paymentRequestPo.getId());

        acceptanceResponse.setAcceptedPayOrderNo(payOrder.getPayOrderNo());
    }
}
