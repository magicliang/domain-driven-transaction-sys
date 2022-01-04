package com.magicliang.transaction.sys.core.domain.strategy.notification;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.magicliang.transaction.sys.common.constant.TimeConstant;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestTypeEnum;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.common.service.integration.dto.PaymentResultNotifyRequest;
import com.magicliang.transaction.sys.common.service.integration.dto.PaymentResultNotifyResponse;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.core.config.CommonConfig;
import com.magicliang.transaction.sys.core.domain.enums.NotificationStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.BaseStrategy;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.helper.PayOrderHelper;
import com.magicliang.transaction.sys.core.model.request.notification.NotificationRequest;
import com.magicliang.transaction.sys.core.model.response.notification.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: rpc 通知策略
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 17:14
 */
@Slf4j
@Component
public class RpcNotificationStrategy extends BaseStrategy implements DomainStrategy<NotificationRequest, NotificationResponse, NotificationStrategyEnum> {

    /**
     * 对每个支付订单，每种类型的通知请求只能存在一个。一个普通通知请求和一个回调通知请求等于两个请求
     */
    private static final int HAS_BOUNCED_NOTIFICATION = 2;

    /**
     * 通用配置
     */
    @Autowired
    private CommonConfig commonConfig;


    /**
     * 标识自己的类型
     *
     * @return 类型
     */
    @Override
    public NotificationStrategyEnum identify() {
        return NotificationStrategyEnum.RPC;
    }

    /**
     * 执行领域请求，生成领域响应
     *
     * @param notificationRequest  领域请求
     * @param notificationResponse 领域响应
     */
    @Override
    public void execute(final NotificationRequest notificationRequest, final NotificationResponse notificationResponse) {
        TransPayOrderEntity payOrder = notificationRequest.getInsPayOrder();
        List<TransRequestEntity> notifyRequests = PayOrderHelper.getUnsentNotificationRequests(payOrder);
        if (CollectionUtils.isEmpty(notifyRequests)) {
            return;
        }
        String notifyUri = payOrder.getNotifyUri();
        List<String> uriParts = Lists.newArrayList(Splitter.on(':')
                .trimResults()
                .omitEmptyStrings()
                .split(notifyUri));
        AssertUtils.assertNotEmpty(uriParts, INVALID_NOTIFY_URI, "invalid notify uri：" + notifyUri);
        AssertUtils.assertEquals(2, uriParts.size(), INVALID_NOTIFY_URI, "invalid notify uri：" + notifyUri);

        String appKey = uriParts.get(0);
        String remotePort = uriParts.get(1);

        // 依据请求的优先级进行排序
        notifyRequests.sort(Comparator.comparing(TransRequestEntity::getRequestType));
        notifyRequests.forEach((notifyRequest) -> {
            PaymentResultNotifyRequest resultNotifyRequest = buildBasicRequest(payOrder, notifyRequests, notifyRequest);

            // 事务1：发送前事务
            updateDomainModels(notifyRequest);
            try {
                PaymentResultNotifyResponse notifyResult;
                if (commonConfig.getMockMode()) {
                    notifyResult = PaymentResultNotifyResponse.Builder.buildSuccessResult();
                    notifyResult.setSuccess(true);
                } else {
                    // mock result
                    notifyResult = PaymentResultNotifyResponse.Builder.buildSuccessResult();
                }
                // 记录响应
                recordRes(notificationResponse, notifyRequest, notifyResult);
            } catch (BaseTransException ex) {
                // 记录异常
                recordException(notifyRequest, ex);
            } finally {
                // 事务2：发送后事务
                updateDomainModels(notifyRequest);
            }
        });
    }

    /**
     * 构造基本的回调通知请求
     *
     * @param payOrder       支付订单
     * @param sortedRequests 排序过的通知请求列表
     * @param notifyRequest  当前的通知
     * @return 基本的回调通知请求
     */
    private PaymentResultNotifyRequest buildBasicRequest(final TransPayOrderEntity payOrder, final List<TransRequestEntity> sortedRequests, final TransRequestEntity notifyRequest) {
        PaymentResultNotifyRequest resultNotifyRequest = new PaymentResultNotifyRequest();
        resultNotifyRequest.setPayOrderNo(payOrder.getPayOrderNo());
        resultNotifyRequest.setSysCode(payOrder.getSysCode());
        resultNotifyRequest.setBizIdentifyNo(payOrder.getBizIdentifyNo());
        resultNotifyRequest.setBizUniqueNo(payOrder.getBizUniqueNo());

        final TransRequestTypeEnum requestType = TransRequestTypeEnum.getByCode(notifyRequest.getRequestType().intValue());
        if (HAS_BOUNCED_NOTIFICATION == sortedRequests.size()
                && TransRequestTypeEnum.BASIC_NOTIFICATION == requestType) {
            // 如果同时存在两个通知请求，且本请求为普通回调请求，则本请求的发送状态应该为成功，且极有可能原始的任务数据被退票回调覆盖了，所以不依赖当前的支付订单生成回调请求
            resultNotifyRequest.setSuccess(true);
        } else {
            fillResultNotifyRequest(payOrder, resultNotifyRequest);
        }

        notifyRequest.setRequestParams(JsonUtils.toJson(resultNotifyRequest));
        return resultNotifyRequest;
    }

    /**
     * 根据支付订单填充回调请求
     *
     * @param payOrder            支付订单
     * @param resultNotifyRequest 回调请求
     */
    private void fillResultNotifyRequest(final TransPayOrderEntity payOrder, final PaymentResultNotifyRequest resultNotifyRequest) {
        // 其他情况下，依据原始的支付订单上记载的因子生成回调请求
        final boolean paymentSuccess = TransPayOrderStatusEnum.isSuccessFinalStatus(payOrder.getStatus().intValue());
        // 根据支付订单的状态设置通知的错误码
        resultNotifyRequest.setSuccess(paymentSuccess);

        if (!paymentSuccess) {
            final TransPayOrderStatusEnum payOrderStatus = TransPayOrderStatusEnum.getByCode(payOrder.getStatus().intValue());
            if (TransPayOrderStatusEnum.FAILED == payOrderStatus) {
                resultNotifyRequest.setErrorCode(PAYMENT_FAILURE_ERROR.getSynthesizedErrorCode());
            } else if (TransPayOrderStatusEnum.CLOSED == payOrderStatus) {
                resultNotifyRequest.setErrorCode(PAYMENT_CLOSED_ERROR.getSynthesizedErrorCode());
            } else if (TransPayOrderStatusEnum.BOUNCED == payOrderStatus) {
                resultNotifyRequest.setErrorCode(PAYMENT_BOUNCED_ERROR.getSynthesizedErrorCode());
            }

            TransRequestEntity payRequest = payOrder.getPaymentRequest();
            String errorMsg = String.format("channelErrorCode：%s，requestResponse：%s，callbackParams：%s",
                    payOrder.getChannelErrorCode(), payRequest.getRequestResponse(), payRequest.getCallbackParams());
            resultNotifyRequest.setErrorMsg(errorMsg);
        }
    }

    /**
     * 更新支付订单通知
     *
     * @param notifyRequest 支付订单通知
     */
    private void updateDomainModels(final TransRequestEntity notifyRequest) {
        final Short notifyRequestStatus = notifyRequest.getStatus();
        if (!TransRequestStatusEnum.isFinalStatus(notifyRequestStatus.intValue())) {
            // 更新下次执行时间，如果更新中发生严重异常（如机器重启）导致模型不被更新，则请求还是会按照老调度顺序被调度
            notifyRequest.setGmtNextExecution(DateUtils.addMinutes(notifyRequest.getGmtNextExecution(),
                    TimeConstant.DEFAULT_EXECUTION_INTERVAL_IN_MINUTES));
        }
        payOrderService.updateChannelRequest(notifyRequest);
    }

    /**
     * 记录响应
     *
     * @param notificationResponse 通知领域响应
     * @param notifyRequest        通知请求
     * @param notifyResult         通知下游结果
     */
    private void recordRes(final NotificationResponse notificationResponse,
                           final TransRequestEntity notifyRequest,
                           final PaymentResultNotifyResponse notifyResult) {
        // 更新领域响应
        notificationResponse.setNotificationSuccess(notifyResult.isSuccess());

        // 更新领域模型

        // 更新状态
        notifyRequest.updateStatus(notifyResult.isSuccess() ?
                TransRequestStatusEnum.SUCCESS.getCode() : TransRequestStatusEnum.FAILED.getCode());

        // 更新响应结果
        notifyRequest.setRequestResponse(JsonUtils.toJson(notifyResult));
        // 更新时间
        notifyRequest.setGmtModified(new Date());
    }

    /**
     * 记录异常
     *
     * @param notifyRequest 通知请求
     * @param ex            请求异常
     */
    private void recordException(final TransRequestEntity notifyRequest, final BaseTransException ex) {
        Throwable cause = ex.getCause();
        if (null != cause) {
            notifyRequest.setRequestException(cause.toString());
        }
        notifyRequest.updateStatus(TransRequestStatusEnum.FAILED.getCode());
        notifyRequest.setRequestException(JsonUtils.toJson(ex));
        // 更新时间
        notifyRequest.setGmtModified(new Date());
    }
}
