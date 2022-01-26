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
@Table(name = "tb_trans_channel_request")
public class TransChannelRequestDo implements Serializable {
    private static final long serialVersionUID = -9187696686893871660L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date gmtCreated;

    private Date gmtModified;

    private Long payOrderNo;

    private Integer requestType;

    private String bizIdentifyNo;

    private String bizUniqueNo;

    private String requestParams;

    private String requestResponse;

    private String callbackParams;

    private String requestException;

    private Date gmtNextExecution;

    private Long retryCount;

    private String requestAddr;

    private Integer status;

    private Integer closeReason;

    private Date gmtLastExecution;

    private Integer env;

    @Column(name = "env", nullable = false)
    public Integer getEnv() {
        return env;
    }

    @Column(name = "gmt_last_execution")
    public Date getGmtLastExecution() {
        return gmtLastExecution;
    }

    @Column(name = "close_reason")
    public Integer getCloseReason() {
        return closeReason;
    }

    @Column(name = "status", nullable = false)
    public Integer getStatus() {
        return status;
    }

    @Column(name = "request_addr", nullable = false)
    public String getRequestAddr() {
        return requestAddr;
    }

    @Column(name = "retry_count", nullable = false)
    public Long getRetryCount() {
        return retryCount;
    }

    @Column(name = "gmt_next_execution")
    public Date getGmtNextExecution() {
        return gmtNextExecution;
    }

    @Lob
    @Column(name = "request_exception")
    public String getRequestException() {
        return requestException;
    }

    @Lob
    @Column(name = "callback_params")
    public String getCallbackParams() {
        return callbackParams;
    }

    @Lob
    @Column(name = "request_response")
    public String getRequestResponse() {
        return requestResponse;
    }

    @Lob
    @Column(name = "request_params")
    public String getRequestParams() {
        return requestParams;
    }

    @Column(name = "biz_unique_no", nullable = false)
    public String getBizUniqueNo() {
        return bizUniqueNo;
    }

    @Column(name = "biz_identify_no", nullable = false)
    public String getBizIdentifyNo() {
        return bizIdentifyNo;
    }

    @Column(name = "request_type", nullable = false)
    public Integer getRequestType() {
        return requestType;
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