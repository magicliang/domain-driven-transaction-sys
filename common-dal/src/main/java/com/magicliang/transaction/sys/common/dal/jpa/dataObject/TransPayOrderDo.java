package com.magicliang.transaction.sys.common.dal.jpa.dataObject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_trans_pay_order")
public class TransPayOrderDo implements Serializable {
    private static final long serialVersionUID = 8094588370873871835L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date gmtCreated;

    private Date gmtModified;

    private Long payOrderNo;

    private String sysCode;

    private String bizIdentifyNo;

    private String bizUniqueNo;

    private Long money;

    private Integer payChannelType;

    private Integer targetAccountType;

    private Integer accountingEntry;

    private Date gmtAcceptedTime;

    private Date gmtPaymentBeginTime;

    private Date gmtPaymentSuccessTime;

    private Date gmtPaymentFailureTime;

    private Date gmtPaymentClosedTime;

    private Date gmtPaymentBouncedTime;

    private Integer status;

    private Integer version;

    private String memo;

    private String channelPaymentTraceNo;

    private String channelDishonorTraceNo;

    private String channelErrorCode;

    private String businessEntity;

    private String notifyUri;

    private String extendInfo;

    private String bizInfo;

    private Integer env;

    @Column(name = "env", nullable = false)
    public Integer getEnv() {
        return env;
    }

    @Column(name = "biz_info", nullable = false, length = 2048)
    public String getBizInfo() {
        return bizInfo;
    }

    @Column(name = "extend_info", nullable = false, length = 2048)
    public String getExtendInfo() {
        return extendInfo;
    }

    @Column(name = "notify_uri", nullable = false)
    public String getNotifyUri() {
        return notifyUri;
    }

    @Column(name = "business_entity", nullable = false)
    public String getBusinessEntity() {
        return businessEntity;
    }

    @Column(name = "channel_error_code", nullable = false)
    public String getChannelErrorCode() {
        return channelErrorCode;
    }

    @Column(name = "channel_dishonor_trace_no", nullable = false)
    public String getChannelDishonorTraceNo() {
        return channelDishonorTraceNo;
    }

    @Column(name = "channel_payment_trace_no", nullable = false)
    public String getChannelPaymentTraceNo() {
        return channelPaymentTraceNo;
    }

    @Column(name = "memo", nullable = false, length = 1024)
    public String getMemo() {
        return memo;
    }

    @Column(name = "version", nullable = false)
    public Integer getVersion() {
        return version;
    }

    @Column(name = "status", nullable = false)
    public Integer getStatus() {
        return status;
    }

    @Column(name = "gmt_payment_bounced_time")
    public Date getGmtPaymentBouncedTime() {
        return gmtPaymentBouncedTime;
    }

    @Column(name = "gmt_payment_closed_time")
    public Date getGmtPaymentClosedTime() {
        return gmtPaymentClosedTime;
    }

    @Column(name = "gmt_payment_failure_time")
    public Date getGmtPaymentFailureTime() {
        return gmtPaymentFailureTime;
    }

    @Column(name = "gmt_payment_success_time")
    public Date getGmtPaymentSuccessTime() {
        return gmtPaymentSuccessTime;
    }

    @Column(name = "gmt_payment_begin_time")
    public Date getGmtPaymentBeginTime() {
        return gmtPaymentBeginTime;
    }

    @Column(name = "gmt_accepted_time", nullable = false)
    public Date getGmtAcceptedTime() {
        return gmtAcceptedTime;
    }

    @Column(name = "accounting_entry", nullable = false)
    public Integer getAccountingEntry() {
        return accountingEntry;
    }

    @Column(name = "target_account_type", nullable = false)
    public Integer getTargetAccountType() {
        return targetAccountType;
    }

    @Column(name = "pay_channel_type", nullable = false)
    public Integer getPayChannelType() {
        return payChannelType;
    }

    @Column(name = "money", nullable = false)
    public Long getMoney() {
        return money;
    }

    @Column(name = "biz_unique_no", nullable = false)
    public String getBizUniqueNo() {
        return bizUniqueNo;
    }

    @Column(name = "biz_identify_no", nullable = false)
    public String getBizIdentifyNo() {
        return bizIdentifyNo;
    }

    @Column(name = "sys_code", nullable = false)
    public String getSysCode() {
        return sysCode;
    }

    @Column(name = "pay_order_no", nullable = false)
    public Long getPayOrderNo() {
        return payOrderNo;
    }

    @Column(name = "gmt_modified", nullable = false)
    public Date getGmtModified() {
        return gmtModified;
    }

    @Column(name = "gmt_created", nullable = false)
    public Date getGmtCreated() {
        return gmtCreated;
    }

    public Long getId() {
        return id;
    }
}