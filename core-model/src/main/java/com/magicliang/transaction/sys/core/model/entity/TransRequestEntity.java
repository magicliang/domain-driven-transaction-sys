package com.magicliang.transaction.sys.core.model.entity;

import com.magicliang.transaction.sys.common.enums.TransEnvEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestCloseReasonEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易请求实体
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 14:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransRequestEntity extends BaseEntity {

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
     * 设置当前对象的新状态
     *
     * @param newStatus 新状态
     */
    public void updateStatus(final Integer newStatus) {
        if (null == this.status) {
            setStatus(newStatus.shortValue());
            return;
        }
        TransRequestStatusEnum.validateStatusBeforeUpdate(this.status.intValue(), newStatus);
        setStatus(newStatus.shortValue());
    }
}
