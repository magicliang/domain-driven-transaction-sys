package com.magicliang.transaction.sys.common.service.integration.param;

import com.magicliang.transaction.sys.common.enums.TransSysConfigEnum;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付通道支付结果回调请求
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 17:30
 */
@Data
public class PaymentResultNotifyRequest {

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
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;
}
