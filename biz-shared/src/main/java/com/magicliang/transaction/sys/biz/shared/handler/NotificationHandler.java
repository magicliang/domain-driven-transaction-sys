package com.magicliang.transaction.sys.biz.shared.handler;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_MODEL_ERROR;

import com.magicliang.transaction.sys.biz.shared.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.shared.request.notification.NotificationCommand;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.factory.TransTransactionContextFactory;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.helper.PayOrderHelper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通知处理器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 16:12
 */
@Slf4j
@Service
public class NotificationHandler extends
        BaseHandler<NotificationCommand, TransactionModel, TransTransactionContext<NotificationCommand,
                TransactionModel>> {

    /**
     * 标识自己的类型。
     *
     * @return 类型
     */
    @Override
    public OperationEnum identify() {
        return OperationEnum.NOTIFY;
    }

    /**
     * 初始化上下文，上下文级别的幂等检查要在这里执行
     *
     * @param request 处理器上下文
     */
    @Override
    public TransTransactionContext<NotificationCommand, TransactionModel> initContext(
            final NotificationCommand request) {
        // 1. 初始化交易上下文
        TransTransactionContext<NotificationCommand, TransactionModel> context =
                TransTransactionContextFactory.getStandardTransactionContext();
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
    public void realExecute(final TransTransactionContext<NotificationCommand, TransactionModel> context) {
        notificationActivity.execute(context);
        context.getModel().setSuccess(true);
    }

    /**
     * 填充全模型
     *
     * @param bizIdentifyNo 业务标识码
     * @param bizUniqueNo 业务唯一标识
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
    private void populateModel(final TransTransactionContext<NotificationCommand, TransactionModel> context) {
        NotificationCommand command = context.getRequest();

        TransactionModel transactionModel;
        TransPayOrderEntity payOrder = command.getPayOrder();
        if (null != payOrder) {
            // 如果外部提供了支付订单则直接进入短路操作
            transactionModel = new TransactionModel();

            if (PayOrderHelper.isLite(payOrder)) {
                payOrder = payOrderService.populateWholePayOrder(payOrder);
            }
            transactionModel.setPayOrder(payOrder);
        } else {
            // 业务标识码
            String bizIdentifyNo = command.getBizIdentifyNo();
            // 业务唯一标识
            String bizUniqueNo = command.getBizUniqueNo();
            transactionModel = populateNecessaryModel(bizIdentifyNo, bizUniqueNo);
            payOrder = transactionModel.getPayOrder();
        }
        // 填充全模型
        // 模型必须填充成功，否则接下来的流程无法执行
        AssertUtils.assertNotNull(transactionModel, INVALID_MODEL_ERROR,
                "invalid transactionModel: " + transactionModel);

        // 被幂等
        final List<TransRequestEntity> notificationRequests = payOrder.getNotificationRequests();

        notificationRequests.forEach((notificationRequest) -> {
            final Long retryCount = notificationRequest.getRetryCount();
            if (retryCount > 0) {
                // 每一个 handler 被幂等，都要设置交易模型幂等标志位
                transactionModel.setIdempotent(true);
            }
        });

        // 填充 context
        context.setModel(transactionModel);
    }

}
