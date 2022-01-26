package com.magicliang.transaction.sys.core.model.entity;

import com.magicliang.transaction.sys.common.enums.TransEnvEnum;
import com.magicliang.transaction.sys.common.enums.TransFundAccountingEntryTypeEnum;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransSysConfigEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单实体，聚合根
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 16:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransPayOrderEntity extends BaseEntity {

    /**
     * 支付订单号，业务主键，全局唯一
     */
    private Long payOrderNo;

    /**
     * 支付订单来源系统，上游系统必填
     *
     * @see TransSysConfigEnum
     */
    private String sysCode;

    /**
     * 业务标识码，上游系统必填
     */
    private String bizIdentifyNo;

    /**
     * 上游业务号，语义同 out_biz_no，与业务标识码联合后，必须全局唯一，可以作为分表键，上游系统必填
     */
    private String bizUniqueNo;

    /**
     * 支付单金额，单位为分，全为正数，上游系统必填
     */
    private Long money;

    /**
     * 会计账目条目 1 借 debit 2 贷 credit，上游系统必填
     *
     * @see TransFundAccountingEntryTypeEnum
     */
    private Short accountingEntry;

    /**
     * 受理时间
     */
    private Date gmtAcceptedTime;

    /**
     * 支付时间
     */
    private Date gmtPaymentBeginTime;

    /**
     * 支付成功时间
     */
    private Date gmtPaymentSuccessTime;

    /**
     * 支付失败时间
     */
    private Date gmtPaymentFailureTime;

    /**
     * 支付关闭时间
     * 这种基于时间的记录法则也可以靠增加一个支付 log 的方式实现，支付 log 的方式可扩展性更好
     */
    private Date gmtPaymentClosedTime;

    /**
     * 支付退票时间
     */
    private Date gmtPaymentBouncedTime;

    /**
     * 支付状态，1 初始化，2 支付中，3 支付成功，4 支付失败，5 支付退票 6 关闭
     *
     * @see TransPayOrderStatusEnum
     */
    private Short status;

    /**
     * 版本
     */
    private Long version;

    /**
     * 支付备注，上游系统选填
     */
    private String memo;

    /**
     * 支付通道的支付流水号，单一通道必须全局唯一
     */
    private String channelPaymentTraceNo;

    /**
     * 支付通道的退票流水号，单一通道必须全局唯一
     */
    private String channelDishonorTraceNo;

    /**
     * 支付错误码
     */
    private String channelErrorCode;

    /**
     * 支付主体，上游系统选填
     */
    private String businessEntity;

    /**
     * 回调地址，上游系统必填
     */
    private String notifyUri;

    /**
     * 扩展信息，平台能力抽象，用于日后本服务特定的平台能力和流程打标用，不透传到链路的其他系统中，上游系统选填
     * 必须是 json 格式
     */
    private Map<String, String> extendInfo;

    /**
     * 扩展信息，业务能力抽象，透传到链路的其他系统中，本系统不理解，上游系统选填
     * 必须是 json 格式
     */
    private Map<String, String> bizInfo;

    /**
     * 环境
     *
     * @see TransEnvEnum
     */
    private Short env;

    // -------------------------------------------------- 聚合根附属对象的分割线 --------------------------------------------------

    /**
     * 子订单
     * 当前一个主订单只关联一个子订单，未来如果要支持多个子订单要支持新成员
     */
    private TransSubOrderEntity subOrder;

    /**
     * 支付请求
     */
    private TransRequestEntity paymentRequest;

    /**
     * 通知请求，通常只有一个通知请求，退票有 2 个
     */
    private List<TransRequestEntity> notificationRequests;

    /**
     * 设置当前对象的新状态
     *
     * @param newStatus 新状态
     */
    public void updateStatus(final Integer newStatus) {
        if (null == this.status) {
            setStatus(newStatus.shortValue());
            return;
        }
        TransPayOrderStatusEnum.validateStatusBeforeUpdate(this.status.intValue(), newStatus);
        setStatus(newStatus.shortValue());
    }
}
