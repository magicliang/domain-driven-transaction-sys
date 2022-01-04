package com.magicliang.transaction.sys.common.dal.po;

import com.magicliang.transaction.sys.common.enums.TransEnvEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestCloseReasonEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestTypeEnum;

import java.util.Date;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 14:11
 */
public class TransRequestWithBLOBPo {

    /**
     * 引用支付订单号，业务主键，全局唯一
     */
    private Long payOrderNo;

    /**
     * 业务标识码，上游系统必填
     */
    private String bizIdentifyNo;

    /**
     * 请求类型
     *
     * @see TransRequestTypeEnum
     */
    private Short requestType;

    /**
     * 上游业务号，语义同 out_biz_no，与业务标识联合后，必须全局唯一，可以作为分表键
     */
    private String bizUniqueNo;

    /**
     * 调用次数
     */
    private Long retryCount;

    /**
     * 请求地址
     */
    private String requestAddr;

    /**
     * 支付状态，1 初始化 2 请求中 3 请求成功 4 请求失败 5请求被关闭
     *
     * @see TransRequestStatusEnum
     */
    private Short status;

    /**
     * 下次预定执行时间，任务的优先级调度逻辑会间接影响这一列，任务调度框架主要理解这一列
     */
    private Date gmtNextExecution;

    /**
     * 上次执行时间
     * 通道请求没有专门的状态变更时间，只有最后一次执行时间，其他时间全部挂靠到支付订单上
     */
    private Date gmtLastExecution;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求响应
     */
    private String requestResponse;

    /**
     * 回调参数
     */
    private String callbackParams;

    /**
     * 请求异常
     */
    private String requestException;

    /**
     * 关闭原因
     *
     * @see TransRequestCloseReasonEnum
     */
    private Integer closeReason;

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
     * get the value of requestType
     *
     * @return the value of requestType
     */
    public Short getRequestType() {
        return requestType;
    }

    /**
     * set the value of the requestType
     *
     * @param requestType the value of requestType
     */
    public void setRequestType(Short requestType) {
        this.requestType = requestType;
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
     * get the value of retryCount
     *
     * @return the value of retryCount
     */
    public Long getRetryCount() {
        return retryCount;
    }

    /**
     * set the value of the retryCount
     *
     * @param retryCount the value of retryCount
     */
    public void setRetryCount(Long retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * get the value of requestAddr
     *
     * @return the value of requestAddr
     */
    public String getRequestAddr() {
        return requestAddr;
    }

    /**
     * set the value of the requestAddr
     *
     * @param requestAddr the value of requestAddr
     */
    public void setRequestAddr(String requestAddr) {
        this.requestAddr = requestAddr;
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
     * get the value of gmtNextExecution
     *
     * @return the value of gmtNextExecution
     */
    public Date getGmtNextExecution() {
        return gmtNextExecution;
    }

    /**
     * set the value of the gmtNextExecution
     *
     * @param gmtNextExecution the value of gmtNextExecution
     */
    public void setGmtNextExecution(Date gmtNextExecution) {
        this.gmtNextExecution = gmtNextExecution;
    }

    /**
     * get the value of gmtLastExecution
     *
     * @return the value of gmtLastExecution
     */
    public Date getGmtLastExecution() {
        return gmtLastExecution;
    }

    /**
     * set the value of the gmtLastExecution
     *
     * @param gmtLastExecution the value of gmtLastExecution
     */
    public void setGmtLastExecution(Date gmtLastExecution) {
        this.gmtLastExecution = gmtLastExecution;
    }

    /**
     * get the value of requestParams
     *
     * @return the value of requestParams
     */
    public String getRequestParams() {
        return requestParams;
    }

    /**
     * set the value of the requestParams
     *
     * @param requestParams the value of requestParams
     */
    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    /**
     * get the value of requestResponse
     *
     * @return the value of requestResponse
     */
    public String getRequestResponse() {
        return requestResponse;
    }

    /**
     * set the value of the requestResponse
     *
     * @param requestResponse the value of requestResponse
     */
    public void setRequestResponse(String requestResponse) {
        this.requestResponse = requestResponse;
    }

    /**
     * get the value of callbackParams
     *
     * @return the value of callbackParams
     */
    public String getCallbackParams() {
        return callbackParams;
    }

    /**
     * set the value of the callbackParams
     *
     * @param callbackParams the value of callbackParams
     */
    public void setCallbackParams(String callbackParams) {
        this.callbackParams = callbackParams;
    }

    /**
     * get the value of requestException
     *
     * @return the value of requestException
     */
    public String getRequestException() {
        return requestException;
    }

    /**
     * set the value of the requestException
     *
     * @param requestException the value of requestException
     */
    public void setRequestException(String requestException) {
        this.requestException = requestException;
    }

    /**
     * get the value of closeReason
     *
     * @return the value of closeReason
     */
    public Integer getCloseReason() {
        return closeReason;
    }

    /**
     * set the value of the closeReason
     *
     * @param closeReason the value of closeReason
     */
    public void setCloseReason(Integer closeReason) {
        this.closeReason = closeReason;
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
}
