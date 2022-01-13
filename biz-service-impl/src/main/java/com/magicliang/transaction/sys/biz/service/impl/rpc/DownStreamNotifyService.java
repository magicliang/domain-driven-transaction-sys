package com.magicliang.transaction.sys.biz.service.impl.rpc;

import com.magicliang.transaction.sys.biz.service.impl.facade.ICallbackFacade;
import com.magicliang.transaction.sys.biz.shared.request.callback.CallbackCommand;
import com.magicliang.transaction.sys.common.service.facade.NotifyService;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 下游支付通道回调接口
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 19:19
 */
@Slf4j
@Service("downStreamNotifyService")
public class DownStreamNotifyService implements NotifyService {

    /**
     * 回调返回值：成功
     */
    private static final String NOTIFY_SUCCESS_FLAG = "T";

    /**
     * 回调返回值：失败
     */
    private static final String NOTIFY_FAIL_FLAG = "F";

    /**
     * 回调门面
     */
    @Autowired
    private ICallbackFacade callbackFacade;

    /**
     * 代付结果回调接口
     *
     * @param req 回调请求
     * @return 回调处理结果
     */
    public Object paymentAgentNotify(Object req) {
        Object resTo = new Object();
        try {
            // TODO：验签
            CallbackCommand callbackCommand = buildCallbackCommand(req);
            TransactionModel transactionModel = callbackFacade.callback(callbackCommand);

            if (transactionModel.isSuccess()) {
//                resTo.setData(NOTIFY_SUCCESS_FLAG);
            } else {
//                resTo.setData(NOTIFY_FAIL_FLAG);
            }

        } catch (Exception ex) {
            log.error("下游支付通道回调异常，请求参数：{}", req, ex);
//            resTo.setData(NOTIFY_FAIL_FLAG);
        } finally {
            log.info("下游支付通道回调，请求参数：{}，响应结果：{}", req, resTo);
        }
        return resTo;
    }

    /**
     * 从回调请求里构造回调命令
     *
     * @param req 回调请求
     * @return 回调命令
     */
    private CallbackCommand buildCallbackCommand(final Object req) {
        CallbackCommand callbackCommand = new CallbackCommand();
        // 构建基础回调请求
        return callbackCommand;
    }
}
