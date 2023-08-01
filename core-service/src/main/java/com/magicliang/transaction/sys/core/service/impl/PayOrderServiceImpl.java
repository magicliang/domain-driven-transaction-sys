package com.magicliang.transaction.sys.core.service.impl;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_PAYMENT_REQUEST_ERROR;
import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_SUB_ORDER_ERROR;

import com.magicliang.transaction.sys.common.constant.TimeConstant;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransAlipaySubOrderPo;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransChannelRequestPoWithBLOBs;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransPayOrderPo;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.manager.PayOrderManager;
import com.magicliang.transaction.sys.core.model.entity.TransAlipaySubOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.convertor.TransAlipaySubOrderConvertor;
import com.magicliang.transaction.sys.core.model.entity.convertor.TransPayOrderConvertor;
import com.magicliang.transaction.sys.core.model.entity.convertor.TransRequestConvertor;
import com.magicliang.transaction.sys.core.model.entity.helper.PayOrderHelper;
import com.magicliang.transaction.sys.core.service.IPayOrderService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单服务实现
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 19:23
 */
@Service
public class PayOrderServiceImpl implements IPayOrderService {

    /**
     * 支付订单管理器
     */
    @Autowired
    protected PayOrderManager payOrderManager;

    /**
     * 轻量级填充支付订单领域模型
     *
     * @param bizIdentifyNo 业务标识码
     * @param bizUniqueNo 业务唯一标识
     * @return 支付订单领域模型
     */
    @Override
    public TransPayOrderEntity populateLitePayOrder(final String bizIdentifyNo, final String bizUniqueNo) {
        TransPayOrderPo payOrder = payOrderManager.queryPayOrder(bizIdentifyNo, bizUniqueNo);
        if (null == payOrder) {
            return null;
        }
        return TransPayOrderConvertor.toDomainEntity(payOrder);
    }

    /**
     * 根据支付订单号查出支付订单列表
     * 注意，这里返回的是轻量级支付订单，什么附属模型都没有
     *
     * @param payOrderNos 支付订单号
     * @return 支付订单列表
     */
    @Override
    public List<TransPayOrderEntity> populatePayOrdersByNos(final List<Long> payOrderNos) {
        return payOrderManager.queryPayOrders(payOrderNos)
                .stream()
                .map(TransPayOrderConvertor::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 填充支付订单领域模型，只要不满足模型实体完整性约束，立刻返回空值
     *
     * @param bizIdentifyNo 业务标识码
     * @param bizUniqueNo 业务唯一标识
     * @return 支付订单领域模型
     */
    @Override
    public TransPayOrderEntity populateWholePayOrder(final String bizIdentifyNo, final String bizUniqueNo) {
        return populateWholePayOrder(populateLitePayOrder(bizIdentifyNo, bizUniqueNo));
    }

    /**
     * 填充支付订单领域模型，尽最大努力填充其他子模型对象
     *
     * @param litePayOrderEntity 未完全填充的订单领域模型
     * @return 完全填充的订单领域模型
     */
    @Override
    public TransPayOrderEntity populateWholePayOrder(final TransPayOrderEntity litePayOrderEntity) {
        if (null == litePayOrderEntity) {
            return null;
        }

        populateAlipaySubOrder(litePayOrderEntity);

        populateRequest(litePayOrderEntity);

        return litePayOrderEntity;
    }

    /**
     * 使用支付请求查询相应的支付订单
     *
     * @param requests 支付请求
     * @return 支付订单
     */
    @Override
    public List<TransPayOrderEntity> populateWholePayOrders(final List<TransRequestEntity> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return Collections.emptyList();
        }
        // 提取支付订单号
        List<Long> payOrderNos = requests.stream().map(TransRequestEntity::getPayOrderNo)
                // request 可能涉及相同的支付订单号，要在这里先去重
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        // 使用支付单号
        List<TransPayOrderEntity> results = populatePayOrdersByNos(payOrderNos);

        // 填充支付请求
        fillPaymentRequests(results, requests);
        // 填充子订单
        fillSubOrders(results);
        // 填充通知请求
        fillNotificationRequests(results);

        return results;
    }

    /**
     * 获取当前所有未支付请求的数量
     *
     * @return 当前所有未支付请求的数量
     */
    @Override
    public long countUnPaidRequests() {
        return payOrderManager.countUnPaidRequests(1);
    }

    /**
     * 查询特定环境、特定数量的未完成的支付请求
     *
     * @param batchSize 批次大小
     * @param env 环境
     * @return 未完成的支付请求列表
     */
    @Override
    public List<TransRequestEntity> populateUnpaidRequest(final int batchSize, final int env) {
        List<TransChannelRequestPoWithBLOBs> requests = payOrderManager.queryUnpaidPaymentRequest(batchSize, env);
        return requests
                .stream()
                .map(TransRequestConvertor::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 获取当前所有未发送通知的数量
     *
     * @return 当前所有未发送通知的数量
     */
    @Override
    public long countUnSentNotifications() {
        return payOrderManager.countUnSentNotifications();
    }

    /**
     * 查询特定环境、特定数量的未完成的支付订单
     *
     * @param batchSize 批次大小
     * @param env 环境
     * @return 未完成的支付订单列表
     */
    @Override
    public List<TransRequestEntity> populateUnSentNotifications(final int batchSize, final int env) {
        List<TransChannelRequestPoWithBLOBs> requests = payOrderManager.queryUnsentNotifications(batchSize, env);
        return requests
                .stream()
                .map(TransRequestConvertor::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 弹出支付宝子订单
     *
     * @param payOrderEntity 支付订单
     */
    @Override
    public void populateAlipaySubOrder(final TransPayOrderEntity payOrderEntity) {
        final Long payOrderNo = payOrderEntity.getPayOrderNo();
        final List<TransAlipaySubOrderPo> alipaySuborders = payOrderManager.queryAlipaySubOrder(payOrderNo);
        AssertUtils.assertSingletonCollection(alipaySuborders, INVALID_SUB_ORDER_ERROR,
                INVALID_SUB_ORDER_ERROR.getErrorMsg());
        payOrderEntity.setSubOrder(TransAlipaySubOrderConvertor.toDomainEntity(alipaySuborders.get(0)));
    }

    /**
     * 装填请求
     *
     * @param payOrderEntity 支付订单
     */
    @Override
    public void populateRequest(final TransPayOrderEntity payOrderEntity) {
        // 所有的持久化化的支付订单都有支付请求
        populatePaymentRequest(payOrderEntity);
        // 所有进入终态的支付订单都有通知请求
        if (TransPayOrderStatusEnum.isFinalStatus(payOrderEntity.getStatus().intValue())) {
            populateNotificationRequest(payOrderEntity);
        }
    }

    /**
     * 装填支付订单的支付请求
     *
     * @param payOrderEntity 支付订单
     */
    @Override
    public void populatePaymentRequest(final TransPayOrderEntity payOrderEntity) {
        final Long payOrderNo = payOrderEntity.getPayOrderNo();
        List<TransChannelRequestPoWithBLOBs> paymentRequests = payOrderManager.queryPaymentRequest(payOrderNo);
        AssertUtils.assertSingletonCollection(paymentRequests, INVALID_PAYMENT_REQUEST_ERROR,
                INVALID_PAYMENT_REQUEST_ERROR.getErrorMsg());
        payOrderEntity.setPaymentRequest(TransRequestConvertor.toDomainEntity(paymentRequests.get(0)));
    }

    /**
     * 装填支付订单的通知请求
     *
     * @param payOrderEntity 支付订单
     */
    @Override
    public void populateNotificationRequest(final TransPayOrderEntity payOrderEntity) {
        final Long payOrderNo = payOrderEntity.getPayOrderNo();
        List<TransChannelRequestPoWithBLOBs> notificationRequests = payOrderManager.queryNotificationRequest(
                payOrderNo);
        AssertUtils.assertNotEmpty(notificationRequests, INVALID_PAYMENT_REQUEST_ERROR,
                INVALID_PAYMENT_REQUEST_ERROR.getErrorMsg());
        payOrderEntity.setNotificationRequests(notificationRequests.stream().map(TransRequestConvertor::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * 更新支付订单和支付请求，这个接口会同时被支付和回调使用到
     *
     * @param payOrder 支付订单
     */
    @Override
    public void updateDomainModels(final TransPayOrderEntity payOrder) {
        final Date now = new Date();
        final TransRequestEntity paymentRequest = payOrder.getPaymentRequest();
        payOrder.setGmtModified(now);
        paymentRequest.setGmtModified(now);

        // 如果支付请求未到终态
        final Short paymentRequestStatus = paymentRequest.getStatus();
        if (!TransRequestStatusEnum.isFinalStatus(paymentRequestStatus.intValue())) {
            // 更新下次执行时间，如果更新中发生严重异常（如机器重启）导致模型不被更新，则请求还是会按照老调度顺序被调度
            paymentRequest.setGmtNextExecution(DateUtils.addMinutes(paymentRequest.getGmtNextExecution(),
                    TimeConstant.DEFAULT_EXECUTION_INTERVAL_IN_MINUTES));
        }

        // 填充缺失的通知列表
        List<TransRequestEntity> notificationRequests = payOrder.getNotificationRequests();
        if (null == notificationRequests) {
            notificationRequests = new ArrayList<>();
            payOrder.setNotificationRequests(notificationRequests);
        }

        // 待创建一个新的通知请求
        TransRequestEntity notificationRequest = null;
        if (PayOrderHelper.needBouncedNotification(payOrder)) {
            notificationRequest = PayOrderHelper.buildBouncedNotificationRequest(payOrder);

        } else if (PayOrderHelper.needBasicNotification(payOrder)) {
            notificationRequest = PayOrderHelper.buildBasicNotificationRequest(payOrder);
        }

        if (null != notificationRequest) {
            // 如果需要插入新的通知请求就执行插入更新
            notificationRequests.add(notificationRequest);
            // 注意，这里暂时不使用 TransactionSynchronizationManager.registerSynchronization，免得出现顺序错乱问题
            insertNotificationAndUpdatePayOrder(PayOrderHelper.updatePayOrder(payOrder),
                    paymentRequest,
                    notificationRequest);
        } else {
            // 否则，只更新
            updatePayOrderAndRequest(PayOrderHelper.updatePayOrder(payOrder), paymentRequest);
        }
    }

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder 支付订单
     * @param payRequest 支付请求
     * @return 支付结果
     */
    @Override
    public boolean updatePayOrderAndRequest(final TransPayOrderEntity payOrder, final TransRequestEntity payRequest) {
        return payOrderManager.updatePayOrderAndRequest(TransPayOrderConvertor.toPo(payOrder),
                TransRequestConvertor.toPo(payRequest));
    }

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder 支付订单
     * @param payRequest 支付请求
     * @param notificationRequest 通知请求
     * @return 支付结果
     */
    @Override
    public boolean insertNotificationAndUpdatePayOrder(final TransPayOrderEntity payOrder,
            final TransRequestEntity payRequest,
            final TransRequestEntity notificationRequest) {
        final TransPayOrderPo payOrderPo = TransPayOrderConvertor.toPo(payOrder);
        final TransChannelRequestPoWithBLOBs payRequestPo = TransRequestConvertor.toPo(payRequest);
        final TransChannelRequestPoWithBLOBs notificationRequestPo = TransRequestConvertor.toPo(notificationRequest);
        boolean result = payOrderManager.insertNotificationAndUpdatePayOrder(payOrderPo,
                payRequestPo,
                notificationRequestPo);
        notificationRequestPo.setId(notificationRequestPo.getId());
        return result;
    }

    /**
     * 更新通道请求
     *
     * @param request 通道请求
     * @return 更新结果
     */
    @Override
    public boolean updateChannelRequest(final TransRequestEntity request) {
        return payOrderManager.updateChannelRequest(TransRequestConvertor.toPo(request));
    }

    /**
     * 将子订单填充进支付订单列表里
     *
     * @param litePayOrders 支付订单列表
     */
    private void fillSubOrders(final List<TransPayOrderEntity> litePayOrders) {
        if (CollectionUtils.isEmpty(litePayOrders)) {
            return;
        }
        fillAlipaySubOrders(litePayOrders);
        // 还可以填充其他类型的子订单
    }

    /**
     * 将支付宝子订单填充进支付订单列表里
     *
     * @param litePayOrders 支付订单列表
     */
    private void fillAlipaySubOrders(final List<TransPayOrderEntity> litePayOrders) {

        // 转换为支付订单号
        List<Long> payOrderNos = getPayOrderNos(Collections.emptyList());

        // 查询出支付宝子订单
        List<TransAlipaySubOrderEntity> subOrders = payOrderManager
                .queryAlipaySubOrder(payOrderNos)
                .stream()
                .map(TransAlipaySubOrderConvertor::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new));

        if (CollectionUtils.isNotEmpty(subOrders)) {
            for (TransPayOrderEntity litePayOrder : litePayOrders) {
                for (TransAlipaySubOrderEntity subOrder : subOrders) {
                    if (Objects.equals(litePayOrder.getPayOrderNo(), subOrder.getPayOrderNo())) {
                        litePayOrder.setSubOrder(subOrder);
                    }
                }
            }
        }
    }

    /**
     * 从支付订单列表生成支付订单号列表
     *
     * @param litePayOrders 支付订单列表
     * @return 支付订单号列表
     */
    private List<Long> getPayOrderNos(final List<TransPayOrderEntity> litePayOrders) {
        return litePayOrders.stream().map(TransPayOrderEntity::getPayOrderNo)
                // request 可能涉及相同的支付订单号，要在这里先去重
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 针对把支付请求装填进轻量级支付订单里
     *
     * @param litePayOrders 轻量级支付订单
     * @param paymentRequests 支付请求
     */
    private void fillPaymentRequests(final List<TransPayOrderEntity> litePayOrders,
            final List<TransRequestEntity> paymentRequests) {
        if (CollectionUtils.isEmpty(litePayOrders) || CollectionUtils.isEmpty(paymentRequests)) {
            return;
        }

        for (TransPayOrderEntity litePayOrder : litePayOrders) {
            for (TransRequestEntity paymentRequest : paymentRequests) {
                if (Objects.equals(paymentRequest.getPayOrderNo(), litePayOrder.getPayOrderNo())) {
                    litePayOrder.setPaymentRequest(paymentRequest);
                }
            }
        }
    }

    /**
     * 将通知请求填充进支付订单列表里
     *
     * @param litePayOrders 支付订单
     */
    private void fillNotificationRequests(final List<TransPayOrderEntity> litePayOrders) {
        if (CollectionUtils.isEmpty(litePayOrders)) {
            return;
        }

        // 转换为支付订单号
        List<Long> payOrderNos = getPayOrderNos(litePayOrders);
        // 查询出通知请求
        List<TransRequestEntity> requests = payOrderManager
                .queryNotificationRequests(payOrderNos)
                .stream()
                .map(TransRequestConvertor::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new));

        if (CollectionUtils.isNotEmpty(requests)) {
            for (TransPayOrderEntity litePayOrder : litePayOrders) {

                List<TransRequestEntity> notificationRequests = litePayOrder.getNotificationRequests();
                if (null == notificationRequests) {
                    notificationRequests = new ArrayList<>(2);
                    litePayOrder.setNotificationRequests(notificationRequests);
                }

                for (TransRequestEntity request : requests) {
                    if (Objects.equals(litePayOrder.getPayOrderNo(), request.getPayOrderNo())) {
                        notificationRequests.add(request);
                    }
                }
            }
        }
    }
}
