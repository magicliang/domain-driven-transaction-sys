package com.magicliang.transaction.sys.core.service;

import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;

import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单服务
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 19:17
 */
public interface IPayOrderService {

    /**
     * 根据支付订单号查出支付订单列表
     * 注意，这里返回的是轻量级支付订单，什么附属模型都没有
     *
     * @param payOrderNos 支付订单号
     * @return 支付订单列表
     */
    List<TransPayOrderEntity> populatePayOrdersByNos(List<Long> payOrderNos);

    /**
     * 填充支付订单领域模型，只要不满足模型实体完整性约束，立刻返回空值
     *
     * @param bizIdentifyNo 业务识别码
     * @param bizUniqueNo   业务唯一标识
     * @return 支付订单领域模型
     */
    TransPayOrderEntity populateWholePayOrder(String bizIdentifyNo, String bizUniqueNo);

    /**
     * 填充支付订单领域模型，只要不满足模型实体完整性约束，立刻返回空值
     *
     * @param litePayOrderEntity 未完全填充的订单领域模型
     * @return 完全填充的订单领域模型
     */
    TransPayOrderEntity populateWholePayOrder(TransPayOrderEntity litePayOrderEntity);

    /**
     * 轻量级填充支付订单领域模型
     *
     * @param bizIdentifyNo 业务识别码
     * @param bizUniqueNo   业务唯一标识
     * @return 支付订单领域模型
     */
    TransPayOrderEntity populateLitePayOrder(String bizIdentifyNo, String bizUniqueNo);

    /**
     * 使用支付请求查询相应的支付订单
     *
     * @param requests 支付请求
     * @return 支付订单
     */
    List<TransPayOrderEntity> populateWholePayOrders(List<TransRequestEntity> requests);

    /**
     * 获取当前所有未支付请求的数量
     *
     * @return 当前所有未支付请求的数量
     */
    long countUnPaidRequests();

    /**
     * 查询特定环境、特定数量的未完成的支付请求
     *
     * @param batchSize 批次大小
     * @param env       环境
     * @return 未完成的支付请求列表
     */
    List<TransRequestEntity> populateUnpaidRequest(int batchSize, int env);

    /**
     * 获取当前所有未发送通知的数量
     *
     * @return 当前所有未发送通知的数量
     */
    long countUnSentNotifications();

    /**
     * 查询特定环境、特定数量的未发送通知
     *
     * @param batchSize 批次大小
     * @param env       环境
     * @return 未发送通知
     */
    List<TransRequestEntity> populateUnSentNotifications(int batchSize, int env);

    /**
     * 弹出支付宝子订单
     *
     * @param payOrderEntity 支付订单
     */
    void populateAlipaySubOrder(TransPayOrderEntity payOrderEntity);

    /**
     * 装填请求
     *
     * @param payOrderEntity 支付订单
     */
    void populateRequest(TransPayOrderEntity payOrderEntity);

    /**
     * 装填支付订单的支付请求
     *
     * @param payOrderEntity 支付订单
     */
    void populatePaymentRequest(TransPayOrderEntity payOrderEntity);

    /**
     * 装填支付订单的通知请求
     *
     * @param payOrderEntity 支付订单
     */
    void populateNotificationRequest(TransPayOrderEntity payOrderEntity);

    /**
     * 更新支付订单和支付请求
     *
     * @param payOrderEntity 支付订单
     */
    void updateDomainModels(TransPayOrderEntity payOrderEntity);

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder   支付订单
     * @param payRequest 支付请求
     * @return 支付结果
     */
    boolean updatePayOrderAndRequest(TransPayOrderEntity payOrder, TransRequestEntity payRequest);

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder            支付订单
     * @param payRequest          支付请求
     * @param notificationRequest 通知请求
     * @return 支付结果
     */
    boolean insertNotificationAndUpdatePayOrder(TransPayOrderEntity payOrder,
                                                TransRequestEntity payRequest,
                                                TransRequestEntity notificationRequest);

    /**
     * 更新通道请求
     *
     * @param request 通道请求
     * @return 更新结果
     */
    boolean updateChannelRequest(TransRequestEntity request);

    /**
     * 弹出支付宝子订单
     *
     * @param payOrderEntity 支付订单
     */
    void populateAlipaySubOrder(TransPayOrderEntity payOrderEntity);
}

