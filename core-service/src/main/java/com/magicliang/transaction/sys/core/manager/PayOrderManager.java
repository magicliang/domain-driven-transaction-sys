package com.magicliang.transaction.sys.core.manager;

import com.magicliang.transaction.sys.common.dal.mybatis.po.TransAlipaySubOrderPo;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransChannelRequestPoWithBLOBs;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransPayOrderPo;

import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单管理器
 * 接口是 Java 表达语义最优雅的方法，我们应该尽量把语义表达为接口
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 14:05
 */
public interface PayOrderManager {

    /**
     * 根据支付订单号列表查询支付订单
     *
     * @param payOrderNos 支付订单号列表
     * @return 支付订单列表
     */
    List<TransPayOrderPo> queryPayOrders(final List<Long> payOrderNos);

    /**
     * 根据支付订单号列表查询通道请求列表
     *
     * @param payOrderNos 支付订单号列表
     * @return 通道请求列表
     */
    List<TransChannelRequestPoWithBLOBs> queryNotificationRequests(List<Long> payOrderNos);

    /**
     * 根据业务标识码 + 业务唯一标识 查询支付订单
     *
     * @param bizIdentifyNo 业务标识码
     * @param bizUniqueNo   业务唯一标识
     * @return 支付订单
     */
    TransPayOrderPo queryPayOrder(final String bizIdentifyNo, final String bizUniqueNo);

    /**
     * 查询特定环境、全部的未完成的支付订单
     * 因为这个查询可能查出大量数据，所以实现上使用了分页查询方法。其他查询如果遇到潜在的分页问题，要自己实现相关的分页。
     *
     * @param batchSize 批次大小
     * @param env       环境
     * @return 未完成的支付订单列表
     */
    List<TransPayOrderPo> queryUnpaidPayOrder(int batchSize, int env);

    /**
     * 获取当前所有未支付请求的数量
     *
     * @return 当前所有未支付请求的数量
     */
    long countUnPaidRequests();

    /**
     * 查询特定环境、全部的未完成的支付请求
     *
     * @param batchSize 批次大小
     * @param env       环境
     * @return 未完成的支付请求列表
     */
    List<TransChannelRequestPoWithBLOBs> queryUnpaidPaymentRequest(int batchSize, int env);

    /**
     * 获取当前所有未发送通知的数量
     *
     * @return 当前所有未发送通知的数量
     */
    long countUnSentNotifications();

    /**
     * 查询特定环境、全部的未发送的通知请求列表
     *
     * @param batchSize 批次大小
     * @param env       环境
     * @return 未发送的通知请求列表
     */
    List<TransChannelRequestPoWithBLOBs> queryUnsentNotifications(int batchSize, int env);

    /**
     * 根据支付订单号查询支付宝子订单
     *
     * @param payOrderNo 支付订单号
     * @return 子订单列表
     */
    List<TransAlipaySubOrderPo> queryAlipaySubOrder(final Long payOrderNo);

    /**
     * 根据支付订单号查询支付宝子订单
     *
     * @param payOrderNos 支付订单号列表
     * @return 子订单列表
     */
    List<TransAlipaySubOrderPo> queryAlipaySubOrder(final List<Long> payOrderNos);

    /**
     * 根据支付订单号查询支付请求
     *
     * @param payOrderNo 支付订单号
     * @return 支付请求列表
     */
    List<TransChannelRequestPoWithBLOBs> queryPaymentRequest(final Long payOrderNo);


    /**
     * 根据支付订单号查询通知请求
     *
     * @param payOrderNo 支付订单号
     * @return 支付通知列表
     */
    List<TransChannelRequestPoWithBLOBs> queryNotificationRequest(final Long payOrderNo);

    /**
     * 插入支付订单和支付请求
     *
     * @param payOrder       支付订单
     * @param request        支付请求
     * @param alipaySuborder 支付宝子订单
     * @return 支付结果
     */
    boolean insertPayOrder(final TransPayOrderPo payOrder, final TransChannelRequestPoWithBLOBs request, final TransAlipaySubOrderPo alipaySuborder);

    /**
     * 插入支付订单和支付请求
     *
     * @param payOrder 支付订单
     * @param request  支付请求
     * @return 支付结果
     */
    boolean insertPayOrder(final TransPayOrderPo payOrder, final TransChannelRequestPoWithBLOBs request);

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder            支付订单
     * @param payRequest          支付请求
     * @param notificationRequest 通知请求
     * @return 支付结果
     */
    boolean insertNotificationAndUpdatePayOrder(TransPayOrderPo payOrder, TransChannelRequestPoWithBLOBs payRequest,
                                                TransChannelRequestPoWithBLOBs notificationRequest);

    /**
     * 插入支付通道请求
     *
     * @param notificationRequest 待插入请求
     * @return 插入结果
     */
    boolean insertRequest(TransChannelRequestPoWithBLOBs notificationRequest);

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder 支付订单
     * @param request  支付请求
     * @return 支付结果
     */
    boolean updatePayOrderAndRequest(TransPayOrderPo payOrder, TransChannelRequestPoWithBLOBs request);

    /**
     * 更新支付订单，在更新过程中对比并增加版本
     *
     * @param payOrder 支付订单
     * @return 更新结果
     */
    boolean updatePayOrder(TransPayOrderPo payOrder);

    /**
     * 更新通道请求
     *
     * @param request 通道请求
     * @return 更新结果
     */
    boolean updateChannelRequest(TransChannelRequestPoWithBLOBs request);
}
