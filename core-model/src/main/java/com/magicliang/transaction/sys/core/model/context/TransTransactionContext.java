package com.magicliang.transaction.sys.core.model.context;

import com.magicliang.transaction.sys.core.model.request.acceptance.AcceptanceRequest;
import com.magicliang.transaction.sys.core.model.request.idgeneration.IdGenerationRequest;
import com.magicliang.transaction.sys.core.model.request.notification.NotificationRequest;
import com.magicliang.transaction.sys.core.model.request.payment.PaymentRequest;
import com.magicliang.transaction.sys.core.model.response.acceptance.AcceptanceResponse;
import com.magicliang.transaction.sys.core.model.response.idgeneration.IdGenerationResponse;
import com.magicliang.transaction.sys.core.model.response.notification.NotificationResponse;
import com.magicliang.transaction.sys.core.model.response.payment.PaymentResponse;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易上下文
 * 单线程内只能存在一个唯一的实例，因为要求使用静态工厂类，所以不易使用继承模式
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 17:42
 */
public class TransTransactionContext<R, T extends TransactionModel> {

    /**
     * 本上下文是否已完成，默认未完成
     */
    private boolean complete = false;

    /**
     * 原始请求
     */
    private R request;

    /**
     * 交易模型
     */
    private T model;

    /**
     * id 生成请求
     */
    private IdGenerationRequest idGenerationRequest;

    /**
     * id 生成响应
     */
    private IdGenerationResponse idGenerationResponse;

    /**
     * id 生成是否已完成
     */
    private boolean idGenerationComplete = false;

    /**
     * 受理请求
     */
    private AcceptanceRequest acceptanceRequest;

    /**
     * 受理响应
     */
    private AcceptanceResponse acceptanceResponse;

    /**
     * 受理是否已完成
     */
    private boolean acceptanceComplete = false;

    /**
     * 支付请求
     */
    private PaymentRequest paymentRequest;

    /**
     * 支付响应
     */
    private PaymentResponse paymentResponse;

    /**
     * 支付是否已完成
     */
    private boolean paymentComplete = false;

    /**
     * 通知请求
     */
    private NotificationRequest notificationRequest;

    /**
     * 通知响应
     */
    private NotificationResponse notificationResponse;

    /**
     * 通知是否已完成
     */
    private boolean notificationComplete = false;

    /**
     * 缺省构造函数
     */
    public TransTransactionContext() {
        // 1. 初始化 id 生成活动领域模型
        this.idGenerationRequest = new IdGenerationRequest();
        this.idGenerationResponse = new IdGenerationResponse();
        // 2. 初始化受理活动领域模型
        this.acceptanceRequest = new AcceptanceRequest();
        this.acceptanceResponse = new AcceptanceResponse();
        // 3. 初始化支付活动领域模型
        this.paymentRequest = new PaymentRequest();
        this.paymentResponse = new PaymentResponse();
        // 4. 初始化通知活动领域模型
        this.notificationRequest = new NotificationRequest();
        this.notificationResponse = new NotificationResponse();
    }

    /**
     * 使用模型初始化构造器
     *
     * @param model 全局领域模型
     */
    public TransTransactionContext(final T model) {
        // 1. 初始化活动领域模型
        this();
        // 2. 初始化全局领域模型
        this.model = model;
    }
}
