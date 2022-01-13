package com.magicliang.transaction.sys.biz.shared.handler;

import com.magicliang.transaction.sys.biz.shared.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.shared.request.callback.CallbackCommand;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.factory.TransTransactionContextFactory;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_MODEL_ERROR;
import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_PAY_ORDER_STATUS_ERROR;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 回调处理器
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:04
 */
@Slf4j
@Service
public class CallbackHandler extends BaseHandler<CallbackCommand, TransactionModel, TransTransactionContext<CallbackCommand, TransactionModel>> {

    /**
     * 标识自己的类型。
     *
     * @return 类型
     */
    @Override
    public OperationEnum identify() {
        return OperationEnum.CALLBACK;
    }

    /**
     * 初始化上下文，上下文级别的幂等检查要在这里执行
     *
     * @param request 处理器上下文
     */
    @Override
    public TransTransactionContext<CallbackCommand, TransactionModel> initContext(final CallbackCommand request) {
        // 1. 初始化交易上下文
        TransTransactionContext<CallbackCommand, TransactionModel> context = TransTransactionContextFactory.getStandardTransactionContext();
        // 2. 设置原始请求
        context.setRequest(request);
        // 3. 依据主库校验幂等并弹出领域模型
        populateModel(context);
        // 4. 返回上下文
        return context;
    }

    /**
     * 真处理操作
     *
     * @param context 处理器上下文
     */
    @Override
    public void realExecute(final TransTransactionContext<CallbackCommand, TransactionModel> context) {
        final TransactionModel model = context.getModel();
        payOrderService.updateDomainModels(model.getPayOrder());
        context.getModel().setSuccess(true);
    }

    /**
     * 填充全模型
     *
     * @param bizIdentifyNo 业务标识码
     * @param bizUniqueNo   业务唯一标识
     * @return 全领域模型
     */
    @Override
    protected TransactionModel populateNecessaryModel(final String bizIdentifyNo, final String bizUniqueNo) {
        TransPayOrderEntity payOrderEntity = payOrderService.populateWholePayOrder(bizIdentifyNo, bizUniqueNo);
        if (null == payOrderEntity) {
            return null;
        }
        TransactionModel model = new TransactionModel();
        model.setPayOrder(payOrderEntity);
        return model;
    }

    /**
     * 校验幂等规则，基本思路：
     * 查询是否已存在处理过的领域模型，如果有处理过的模型，校验状态，填充交易模型的值，用旧模型的值直接进行接下来的上下文
     *
     * @param context 处理器上下文
     */
    private void populateModel(final TransTransactionContext<CallbackCommand, TransactionModel> context) {
        CallbackCommand command = context.getRequest();
        // 业务标识码
        String bizIdentifyNo = command.getBizIdentifyNo();
        // 业务唯一标识
        String bizUniqueNo = command.getBizUniqueNo();
        // 填充全模型。注意，回调的时候上游业务系统不一定知道 payorderno，所以这里实际上是强依赖于业务标识码和业务号的。
        TransactionModel transactionModel = populateNecessaryModel(bizIdentifyNo, bizUniqueNo);
        // 模型必须填充成功，否则接下来的流程无法执行
        AssertUtils.assertNotNull(transactionModel, INVALID_MODEL_ERROR, "invalid transactionModel: " + transactionModel);
        // 填充 context
        context.setModel(transactionModel);

        // 被幂等
        final TransPayOrderEntity payOrder = transactionModel.getPayOrder();

        // 退票不受其他终态约束，只要是非退票状态都可退票，只要是退票状态不可继续执行
        if (checkIsBounced(transactionModel)) {
            transactionModel.setIdempotent(true);
            context.setComplete(true);
            // 凡是提前结束的操作必须成功
            transactionModel.setSuccess(true);
            return;
        }
        // 还可以加一条检验，只有异步支付才允许回调

        assemblePayOrderBeforeUpdate(command, payOrder);
    }

    /**
     * 在更新支付订单以前填充数据
     *
     * @param command  回调命令
     * @param payOrder 支付订单
     */
    private void assemblePayOrderBeforeUpdate(final CallbackCommand command, final TransPayOrderEntity payOrder) {
        Date now = new Date();
        payOrder.setGmtModified(now);
        Date callBackTime = command.getCallBackTime();
        if (null == callBackTime) {
            callBackTime = now;
        }
        Date callBackBizTime = command.getCallBackBizTime();
        final Integer payOrderStatus = command.getPayOrderStatus();
        final TransPayOrderStatusEnum orderStatusEnum = TransPayOrderStatusEnum.getByCode(payOrderStatus);
        AssertUtils.assertNotNull(orderStatusEnum, INVALID_PAY_ORDER_STATUS_ERROR, "invalid payOrder status：" + payOrderStatus);

        // 先给支付订单设状态
        payOrder.updateStatus(payOrderStatus);
        if (TransPayOrderStatusEnum.SUCCESS == orderStatusEnum) {
            // 设置成功时间
            payOrder.setGmtPaymentSuccessTime(callBackTime);
            // 如果业务有指定额外的业务时间，则以业务时间为成功时间
            if (null != callBackBizTime) {
                payOrder.setGmtPaymentSuccessTime(callBackBizTime);
            }
            // 覆盖支付流水号，也可以考虑在有旧值的时候不覆盖
            payOrder.setChannelPaymentTraceNo(command.getCallbackBizNo());
        } else {
            // 设值错误码
            payOrder.setChannelErrorCode(command.getErrorCode());
            // 设置相关失败时间
            if (TransPayOrderStatusEnum.FAILED == orderStatusEnum) {
                payOrder.setGmtPaymentFailureTime(callBackTime);
            } else if (TransPayOrderStatusEnum.CLOSED == orderStatusEnum) {
                payOrder.setGmtPaymentClosedTime(callBackTime);
            } else if (TransPayOrderStatusEnum.BOUNCED == orderStatusEnum) {
                payOrder.setGmtPaymentBouncedTime(callBackTime);
                payOrder.setChannelDishonorTraceNo(command.getCallbackBizNo());
            } else {
                throw new BaseTransException(INVALID_PAY_ORDER_STATUS_ERROR, payOrderStatus + "");
            }
        }

        // 更新支付请求，且只更新支付请求
        TransRequestEntity paymentRequest = payOrder.getPaymentRequest();
        paymentRequest.setCallbackParams(command.getCallBackParams());
        paymentRequest.setGmtModified(now);
        // 如果支付请求未到达终态，追平支付请求状态
        if (!TransRequestStatusEnum.isFinalStatus(paymentRequest.getStatus().intValue())) {
            if (TransPayOrderStatusEnum.isSuccessFinalStatus(payOrder.getStatus().intValue())) {
                // 如果支付订单是成功终态，则支付请求是成功终态
                paymentRequest.updateStatus(TransRequestStatusEnum.SUCCESS.getCode());
            } else {
                // 如果支付订单不是成功终态，则支付请求是关闭终态
                paymentRequest.updateStatus(TransRequestStatusEnum.CLOSED.getCode());
            }
        }
    }
}