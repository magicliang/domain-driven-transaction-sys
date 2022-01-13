package com.magicliang.transaction.sys.biz.shared.request.callback;

import com.magicliang.transaction.sys.biz.shared.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.shared.request.HandlerRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 回调命令
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 14:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackCommand extends HandlerRequest {

    /**
     * 回调业务号，在退票场景下等于退票流水号
     */
    private String callbackBizNo;

    /**
     * 支付订单状态
     */
    private Integer payOrderStatus;

    /**
     * 回调时间
     */
    private Date callBackTime;

    /**
     * 回调中还可以携带业务时间
     */
    private Date callBackBizTime;

    /**
     * 回调参数
     */
    private String callBackParams;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 标识自己的类型。
     *
     * @return 类型
     */
    @Override
    public OperationEnum identify() {
        return OperationEnum.CALLBACK;
    }

}