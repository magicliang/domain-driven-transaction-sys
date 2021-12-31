package com.magicliang.transaction.sys.core.model.entity.helper;

import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestCloseReasonEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestTypeEnum;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单帮助器
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 16:55
 */
public class PayOrderHelper {

    /**
     * 私有构造器
     */
    private PayOrderHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * 是否轻量级支付订单（待填充）
     *
     * @param payOrder 支付订单
     * @return 是否轻量级支付订单
     */
    public static boolean isLite(final TransPayOrderEntity payOrder) {
        final List<TransRequestEntity> notificationRequests = payOrder.getNotificationRequests();
        return null == payOrder.getSubOrder()
                || null == payOrder.getPaymentRequest()
                // 终态无通知请求
                || (TransPayOrderStatusEnum.isFinalStatus(payOrder.getStatus().intValue())
                && CollectionUtils.isEmpty(notificationRequests))
                // 退票无第二个通知请求
                || (TransPayOrderStatusEnum.isBounced(payOrder.getStatus().intValue())
                && CollectionUtils.isNotEmpty(notificationRequests)
                && 1 == notificationRequests.size());
    }

    /**
     * 关闭支付订单和支付请求
     *
     * @param payOrder   支付订单
     * @param closedTime 关闭时间
     */
    public static void closePayOrder(final TransPayOrderEntity payOrder, final Date closedTime) {
        payOrder.updateStatus(TransPayOrderStatusEnum.FAILED.getCode());
        payOrder.setGmtPaymentFailureTime(closedTime);
        final TransRequestEntity payRequest = payOrder.getPaymentRequest();
        payRequest.updateStatus(TransRequestStatusEnum.CLOSED.getCode());
        payRequest.setCloseReason(TransRequestCloseReasonEnum.NOT_RETRYABLE_REQUEST.getCode());
    }

    /**
     * 更新支付订单
     *
     * @param payOrder 支付订单
     * @return 支付订单
     */
    public static TransPayOrderEntity updatePayOrder(final TransPayOrderEntity payOrder) {
        return updatePayOrder(payOrder, new Date());
    }

    /**
     * 更新支付订单
     *
     * @param payOrder     支付订单
     * @param modifiedTime 修改时间
     * @return 支付订单
     */
    public static TransPayOrderEntity updatePayOrder(final TransPayOrderEntity payOrder, final Date modifiedTime) {
        if (null != modifiedTime) {
            payOrder.setGmtModified(modifiedTime);
        }
        payOrder.setVersion(payOrder.getVersion() + 1);
        return payOrder;
    }

    /**
     * 是否处于可基础通知的状态
     *
     * @param payOrder 支付订单
     * @return 是否处于可基础通知的状态
     */
    public static boolean needBasicNotification(final TransPayOrderEntity payOrder) {
        final List<TransRequestEntity> notificationRequests = payOrder.getNotificationRequests();
        final int status = payOrder.getStatus().intValue();
        // 非退票
        return !TransPayOrderStatusEnum.isBounced(status)
                // 终态
                && TransPayOrderStatusEnum.isFinalStatus(status)
                // 无基础通知
                && CollectionUtils.isEmpty(notificationRequests);
    }

    /**
     * 是否处于可退票通知的状态
     *
     * @param payOrder 支付订单
     * @return 是否处于可退票通知的状态
     */
    public static boolean needBouncedNotification(final TransPayOrderEntity payOrder) {
        final List<TransRequestEntity> notificationRequests = payOrder.getNotificationRequests();
        final int status = payOrder.getStatus().intValue();
        // 被退票
        boolean isBounced = TransPayOrderStatusEnum.isBounced(status);

        // 且没有可以发送的通知请求
        boolean noBouncedNotification = true;
        if (CollectionUtils.isNotEmpty(notificationRequests)) {
            for (TransRequestEntity notificationRequest : notificationRequests) {
                TransRequestTypeEnum requestType = TransRequestTypeEnum.getByCode(notificationRequest.getRequestType().intValue());
                if (TransRequestTypeEnum.BOUNCED_NOTIFICATION == requestType) {
                    // 一旦有一个退票请求，则不允许产生新的退票请求
                    noBouncedNotification = false;
                }
            }
        }

        return isBounced && noBouncedNotification;
    }

    /**
     * 初始化支付订单的基础通知请求
     *
     * @param payOrder 支付订单
     * @return 通知请求
     */
    public static TransRequestEntity buildBasicNotificationRequest(final TransPayOrderEntity payOrder) {
        return buildInitialNotificationRequest(payOrder, TransRequestTypeEnum.BASIC_NOTIFICATION);
    }

    /**
     * 初始化支付订单的回调通知请求
     *
     * @param payOrder 支付订单
     * @return 通知请求
     */
    public static TransRequestEntity buildBouncedNotificationRequest(final TransPayOrderEntity payOrder) {
        return buildInitialNotificationRequest(payOrder, TransRequestTypeEnum.BOUNCED_NOTIFICATION);
    }

    /**
     * 初始化支付订单的通知请求
     *
     * @param payOrder         支付订单
     * @param notificationType 通知类型
     * @return 通知请求
     */
    public static TransRequestEntity buildInitialNotificationRequest(final TransPayOrderEntity payOrder, final TransRequestTypeEnum notificationType) {
        TransRequestEntity notificationRequest = new TransRequestEntity();
        Date now = new Date();
        notificationRequest.setPayOrderNo(payOrder.getPayOrderNo());
        notificationRequest.setRequestType(notificationType.getCode().shortValue());
        notificationRequest.setBizIdentifyNo(payOrder.getBizIdentifyNo());
        notificationRequest.setBizUniqueNo(payOrder.getBizUniqueNo());
        notificationRequest.setRetryCount(0L);
        notificationRequest.setRequestAddr(payOrder.getNotifyUri());
        notificationRequest.updateStatus(TransRequestStatusEnum.INIT.getCode());
        notificationRequest.setGmtCreated(now);
        // 默认当前插入的支付请求就是可执行的
        notificationRequest.setGmtNextExecution(now);
        notificationRequest.setGmtModified(now);
        notificationRequest.setEnv(payOrder.getEnv());
        return notificationRequest;
    }

    /**
     * 过滤出支付订单中的未发送通知
     *
     * @param payOrder 支付订单
     * @return 未发送通知
     */
    public static List<TransRequestEntity> getUnsentNotificationRequests(final TransPayOrderEntity payOrder) {
        if (null == payOrder) {
            return Collections.emptyList();
        }
        List<TransRequestEntity> notificationRequests = payOrder.getNotificationRequests();
        if (CollectionUtils.isEmpty(notificationRequests)) {
            return Collections.emptyList();
        }
        return notificationRequests
                .stream()
                .filter((notificationRequest) -> !TransRequestStatusEnum.isFinalStatus(notificationRequest.getStatus().intValue()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
