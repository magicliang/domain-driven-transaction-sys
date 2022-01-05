package com.magicliang.transaction.sys.biz.service.impl.request;

import com.magicliang.transaction.sys.biz.service.impl.enums.OperationEnum;
import com.magicliang.transaction.sys.common.enums.TransSysConfigEnum;
import com.magicliang.transaction.sys.common.type.IIdentifiableType;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 处理器请求
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 14:17
 */
@Data
public abstract class HandlerRequest implements IIdentifiableType<OperationEnum> {

    /**
     * 支付订单来源系统，上游系统必填
     *
     * @see TransSysConfigEnum
     */
    private String sysCode;

    /**
     * 业务识别码，上游系统必填
     */
    private String bizIdentifyNo;

    /**
     * 上游业务号，语义同 out_biz_no，与业务识别码联合后，必须全局唯一，可以作为分表键，上游系统必填
     */
    private String bizUniqueNo;

    /**
     * 返回幂等键
     *
     * @return 幂等键
     */
    public String getIdempotentKey() {
        return bizIdentifyNo + ":" + bizUniqueNo;
    }

}