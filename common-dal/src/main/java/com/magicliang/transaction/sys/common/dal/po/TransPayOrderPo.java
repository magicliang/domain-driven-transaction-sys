package com.magicliang.transaction.sys.common.dal.po;

import com.magicliang.transaction.sys.common.enums.TransEnvEnum;
import com.magicliang.transaction.sys.common.enums.TransFundAccountingEntryTypeEnum;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransSysConfigEnum;

import java.util.Date;
import java.util.Objects;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易订单持久化模型
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 16:41
 */
public class TransPayOrderPo {

    /**
     * 物理主键，单表唯一
     */
    private Long id;

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
    private String extendInfo;

    /**
     * 扩展信息，业务能力抽象，透传到链路的其他系统中，本系统不理解，上游系统选填
     * 必须是 json 格式
     */
    private String bizInfo;

    /**
     * 环境
     *
     * @see TransEnvEnum
     */
    private Short env;


    /**
     * get the value of payOrderNo
     *
     * @return the value of payOrderNo
     */
    public Long getPayOrderNo() {
        return payOrderNo;
    }

    /**
     * set the value of the payOrderNo
     *
     * @param payOrderNo the value of payOrderNo
     */
    public void setPayOrderNo(Long payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    /**
     * get the value of sysCode
     *
     * @return the value of sysCode
     */
    public String getSysCode() {
        return sysCode;
    }

    /**
     * set the value of the sysCode
     *
     * @param sysCode the value of sysCode
     */
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    /**
     * get the value of bizIdentifyNo
     *
     * @return the value of bizIdentifyNo
     */
    public String getBizIdentifyNo() {
        return bizIdentifyNo;
    }

    /**
     * set the value of the bizIdentifyNo
     *
     * @param bizIdentifyNo the value of bizIdentifyNo
     */
    public void setBizIdentifyNo(String bizIdentifyNo) {
        this.bizIdentifyNo = bizIdentifyNo;
    }

    /**
     * get the value of bizUniqueNo
     *
     * @return the value of bizUniqueNo
     */
    public String getBizUniqueNo() {
        return bizUniqueNo;
    }

    /**
     * set the value of the bizUniqueNo
     *
     * @param bizUniqueNo the value of bizUniqueNo
     */
    public void setBizUniqueNo(String bizUniqueNo) {
        this.bizUniqueNo = bizUniqueNo;
    }

    /**
     * get the value of money
     *
     * @return the value of money
     */
    public Long getMoney() {
        return money;
    }

    /**
     * set the value of the money
     *
     * @param money the value of money
     */
    public void setMoney(Long money) {
        this.money = money;
    }

    /**
     * get the value of accountingEntry
     *
     * @return the value of accountingEntry
     */
    public Short getAccountingEntry() {
        return accountingEntry;
    }

    /**
     * set the value of the accountingEntry
     *
     * @param accountingEntry the value of accountingEntry
     */
    public void setAccountingEntry(Short accountingEntry) {
        this.accountingEntry = accountingEntry;
    }

    /**
     * get the value of gmtAcceptedTime
     *
     * @return the value of gmtAcceptedTime
     */
    public Date getGmtAcceptedTime() {
        return gmtAcceptedTime;
    }

    /**
     * set the value of the gmtAcceptedTime
     *
     * @param gmtAcceptedTime the value of gmtAcceptedTime
     */
    public void setGmtAcceptedTime(Date gmtAcceptedTime) {
        this.gmtAcceptedTime = gmtAcceptedTime;
    }

    /**
     * get the value of gmtPaymentBeginTime
     *
     * @return the value of gmtPaymentBeginTime
     */
    public Date getGmtPaymentBeginTime() {
        return gmtPaymentBeginTime;
    }

    /**
     * set the value of the gmtPaymentBeginTime
     *
     * @param gmtPaymentBeginTime the value of gmtPaymentBeginTime
     */
    public void setGmtPaymentBeginTime(Date gmtPaymentBeginTime) {
        this.gmtPaymentBeginTime = gmtPaymentBeginTime;
    }

    /**
     * get the value of gmtPaymentSuccessTime
     *
     * @return the value of gmtPaymentSuccessTime
     */
    public Date getGmtPaymentSuccessTime() {
        return gmtPaymentSuccessTime;
    }

    /**
     * set the value of the gmtPaymentSuccessTime
     *
     * @param gmtPaymentSuccessTime the value of gmtPaymentSuccessTime
     */
    public void setGmtPaymentSuccessTime(Date gmtPaymentSuccessTime) {
        this.gmtPaymentSuccessTime = gmtPaymentSuccessTime;
    }

    /**
     * get the value of gmtPaymentFailureTime
     *
     * @return the value of gmtPaymentFailureTime
     */
    public Date getGmtPaymentFailureTime() {
        return gmtPaymentFailureTime;
    }

    /**
     * set the value of the gmtPaymentFailureTime
     *
     * @param gmtPaymentFailureTime the value of gmtPaymentFailureTime
     */
    public void setGmtPaymentFailureTime(Date gmtPaymentFailureTime) {
        this.gmtPaymentFailureTime = gmtPaymentFailureTime;
    }

    /**
     * get the value of gmtPaymentClosedTime
     *
     * @return the value of gmtPaymentClosedTime
     */
    public Date getGmtPaymentClosedTime() {
        return gmtPaymentClosedTime;
    }

    /**
     * set the value of the gmtPaymentClosedTime
     *
     * @param gmtPaymentClosedTime the value of gmtPaymentClosedTime
     */
    public void setGmtPaymentClosedTime(Date gmtPaymentClosedTime) {
        this.gmtPaymentClosedTime = gmtPaymentClosedTime;
    }

    /**
     * get the value of gmtPaymentBouncedTime
     *
     * @return the value of gmtPaymentBouncedTime
     */
    public Date getGmtPaymentBouncedTime() {
        return gmtPaymentBouncedTime;
    }

    /**
     * set the value of the gmtPaymentBouncedTime
     *
     * @param gmtPaymentBouncedTime the value of gmtPaymentBouncedTime
     */
    public void setGmtPaymentBouncedTime(Date gmtPaymentBouncedTime) {
        this.gmtPaymentBouncedTime = gmtPaymentBouncedTime;
    }

    /**
     * get the value of status
     *
     * @return the value of status
     */
    public Short getStatus() {
        return status;
    }

    /**
     * set the value of the status
     *
     * @param status the value of status
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * get the value of version
     *
     * @return the value of version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * set the value of the version
     *
     * @param version the value of version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * get the value of memo
     *
     * @return the value of memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * set the value of the memo
     *
     * @param memo the value of memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * get the value of channelPaymentTraceNo
     *
     * @return the value of channelPaymentTraceNo
     */
    public String getChannelPaymentTraceNo() {
        return channelPaymentTraceNo;
    }

    /**
     * set the value of the channelPaymentTraceNo
     *
     * @param channelPaymentTraceNo the value of channelPaymentTraceNo
     */
    public void setChannelPaymentTraceNo(String channelPaymentTraceNo) {
        this.channelPaymentTraceNo = channelPaymentTraceNo;
    }

    /**
     * get the value of channelDishonorTraceNo
     *
     * @return the value of channelDishonorTraceNo
     */
    public String getChannelDishonorTraceNo() {
        return channelDishonorTraceNo;
    }

    /**
     * set the value of the channelDishonorTraceNo
     *
     * @param channelDishonorTraceNo the value of channelDishonorTraceNo
     */
    public void setChannelDishonorTraceNo(String channelDishonorTraceNo) {
        this.channelDishonorTraceNo = channelDishonorTraceNo;
    }

    /**
     * get the value of channelErrorCode
     *
     * @return the value of channelErrorCode
     */
    public String getChannelErrorCode() {
        return channelErrorCode;
    }

    /**
     * set the value of the channelErrorCode
     *
     * @param channelErrorCode the value of channelErrorCode
     */
    public void setChannelErrorCode(String channelErrorCode) {
        this.channelErrorCode = channelErrorCode;
    }

    /**
     * get the value of businessEntity
     *
     * @return the value of businessEntity
     */
    public String getBusinessEntity() {
        return businessEntity;
    }

    /**
     * set the value of the businessEntity
     *
     * @param businessEntity the value of businessEntity
     */
    public void setBusinessEntity(String businessEntity) {
        this.businessEntity = businessEntity;
    }

    /**
     * get the value of notifyUri
     *
     * @return the value of notifyUri
     */
    public String getNotifyUri() {
        return notifyUri;
    }

    /**
     * set the value of the notifyUri
     *
     * @param notifyUri the value of notifyUri
     */
    public void setNotifyUri(String notifyUri) {
        this.notifyUri = notifyUri;
    }

    /**
     * get the value of extendInfo
     *
     * @return the value of extendInfo
     */
    public String getExtendInfo() {
        return extendInfo;
    }

    /**
     * set the value of the extendInfo
     *
     * @param extendInfo the value of extendInfo
     */
    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    /**
     * get the value of bizInfo
     *
     * @return the value of bizInfo
     */
    public String getBizInfo() {
        return bizInfo;
    }

    /**
     * set the value of the bizInfo
     *
     * @param bizInfo the value of bizInfo
     */
    public void setBizInfo(String bizInfo) {
        this.bizInfo = bizInfo;
    }

    /**
     * get the value of env
     *
     * @return the value of env
     */
    public Short getEnv() {
        return env;
    }

    /**
     * set the value of the env
     *
     * @param env the value of env
     */
    public void setEnv(Short env) {
        this.env = env;
    }

    /**
     * get the value of id
     *
     * @return the value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * set the value of the id
     *
     * @param id the value of id
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransPayOrderPo)) return false;
        TransPayOrderPo that = (TransPayOrderPo) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getPayOrderNo(), that.getPayOrderNo()) && Objects.equals(getSysCode(), that.getSysCode()) && Objects.equals(getBizIdentifyNo(), that.getBizIdentifyNo()) && Objects.equals(getBizUniqueNo(), that.getBizUniqueNo()) && Objects.equals(getMoney(), that.getMoney()) && Objects.equals(getAccountingEntry(), that.getAccountingEntry()) && Objects.equals(getGmtAcceptedTime(), that.getGmtAcceptedTime()) && Objects.equals(getGmtPaymentBeginTime(), that.getGmtPaymentBeginTime()) && Objects.equals(getGmtPaymentSuccessTime(), that.getGmtPaymentSuccessTime()) && Objects.equals(getGmtPaymentFailureTime(), that.getGmtPaymentFailureTime()) && Objects.equals(getGmtPaymentClosedTime(), that.getGmtPaymentClosedTime()) && Objects.equals(getGmtPaymentBouncedTime(), that.getGmtPaymentBouncedTime()) && Objects.equals(getStatus(), that.getStatus()) && Objects.equals(getVersion(), that.getVersion()) && Objects.equals(getMemo(), that.getMemo()) && Objects.equals(getChannelPaymentTraceNo(), that.getChannelPaymentTraceNo()) && Objects.equals(getChannelDishonorTraceNo(), that.getChannelDishonorTraceNo()) && Objects.equals(getChannelErrorCode(), that.getChannelErrorCode()) && Objects.equals(getBusinessEntity(), that.getBusinessEntity()) && Objects.equals(getNotifyUri(), that.getNotifyUri()) && Objects.equals(getExtendInfo(), that.getExtendInfo()) && Objects.equals(getBizInfo(), that.getBizInfo()) && Objects.equals(getEnv(), that.getEnv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPayOrderNo(), getSysCode(), getBizIdentifyNo(), getBizUniqueNo(), getMoney(), getAccountingEntry(), getGmtAcceptedTime(), getGmtPaymentBeginTime(), getGmtPaymentSuccessTime(), getGmtPaymentFailureTime(), getGmtPaymentClosedTime(), getGmtPaymentBouncedTime(), getStatus(), getVersion(), getMemo(), getChannelPaymentTraceNo(), getChannelDishonorTraceNo(), getChannelErrorCode(), getBusinessEntity(), getNotifyUri(), getExtendInfo(), getBizInfo(), getEnv());
    }

    @Override
    public String toString() {
        return "TransPayOrderPo{" +
                "id=" + id +
                ", payOrderNo=" + payOrderNo +
                ", sysCode='" + sysCode + '\'' +
                ", bizIdentifyNo='" + bizIdentifyNo + '\'' +
                ", bizUniqueNo='" + bizUniqueNo + '\'' +
                ", money=" + money +
                ", accountingEntry=" + accountingEntry +
                ", gmtAcceptedTime=" + gmtAcceptedTime +
                ", gmtPaymentBeginTime=" + gmtPaymentBeginTime +
                ", gmtPaymentSuccessTime=" + gmtPaymentSuccessTime +
                ", gmtPaymentFailureTime=" + gmtPaymentFailureTime +
                ", gmtPaymentClosedTime=" + gmtPaymentClosedTime +
                ", gmtPaymentBouncedTime=" + gmtPaymentBouncedTime +
                ", status=" + status +
                ", version=" + version +
                ", memo='" + memo + '\'' +
                ", channelPaymentTraceNo='" + channelPaymentTraceNo + '\'' +
                ", channelDishonorTraceNo='" + channelDishonorTraceNo + '\'' +
                ", channelErrorCode='" + channelErrorCode + '\'' +
                ", businessEntity='" + businessEntity + '\'' +
                ", notifyUri='" + notifyUri + '\'' +
                ", extendInfo='" + extendInfo + '\'' +
                ", bizInfo='" + bizInfo + '\'' +
                ", env=" + env +
                '}';
    }
}
