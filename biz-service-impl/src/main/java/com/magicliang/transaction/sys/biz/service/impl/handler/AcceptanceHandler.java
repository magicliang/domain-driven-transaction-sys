package com.magicliang.transaction.sys.biz.service.impl.handler;

import com.magicliang.transaction.sys.biz.service.impl.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.service.impl.request.acceptance.AcceptanceCommand;
import com.magicliang.transaction.sys.biz.service.impl.request.acceptance.AlipayAcceptanceCommand;
import com.magicliang.transaction.sys.biz.service.impl.request.acceptance.convertor.AlipayAcceptanceCommandConvertor;
import com.magicliang.transaction.sys.common.enums.TransRequestTypeEnum;
import com.magicliang.transaction.sys.core.factory.TransTransactionContextFactory;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.TransSubOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理处理器
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 15:40
 */
@Slf4j
@Service
public class AcceptanceHandler extends BaseHandler<AcceptanceCommand, TransactionModel, TransTransactionContext<AcceptanceCommand, TransactionModel>> {

    /**
     * 标识自己的类型
     *
     * @return 类型
     */
    @Override
    public OperationEnum identify() {
        return OperationEnum.ACCEPTANCE;
    }

    /**
     * 初始化上下文，上下文级别的幂等检查要在这里执行
     *
     * @param request 业务请求
     */
    @Override
    public TransTransactionContext<AcceptanceCommand, TransactionModel> initContext(final AcceptanceCommand request) {
        // 1. 初始化交易上下文
        TransTransactionContext<AcceptanceCommand, TransactionModel> context = TransTransactionContextFactory.getStandardTransactionContext();
        // 2. 设置原始请求
        context.setRequest(request);
        // 3. 依据主库校验幂等并弹出领域模型 TODO：实现一个 mock 的 forceMaster
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
    public void realExecute(final TransTransactionContext<AcceptanceCommand, TransactionModel> context) {
        // 1. 生成 id
        idGenerationActivity.execute(context);
        // 2. 执行受理
        acceptanceActivity.execute(context);
        // 3. 执行成功
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
        // 受理场景不需要关注全领域模型，只要已经查出订单就认为填充了全模型
        TransPayOrderEntity payOrderEntity = payOrderService.populateLitePayOrder(bizIdentifyNo, bizUniqueNo);
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
    private void populateModel(final TransTransactionContext<AcceptanceCommand, TransactionModel> context) {
        AcceptanceCommand command = context.getRequest();
        // 业务标识码
        String bizIdentifyNo = command.getBizIdentifyNo();
        // 业务唯一标识
        String bizUniqueNo = command.getBizUniqueNo();
        // 填充全模型
        TransactionModel transactionModel = populateNecessaryModel(bizIdentifyNo, bizUniqueNo);
        // 如果存在领域模型则幂等命中
        if (null != transactionModel) {
            // 每一个 handler 被幂等，都要设置交易模型幂等标志位
            transactionModel.setIdempotent(true);
            // 凡是涉及写支付订单的操作，都要校验已经进入终态的支付订单
            getErrorMessage(transactionModel);
            context.setModel(transactionModel);
            // 如果发生幂等上下文必须结束，否则接下来的活动会读不到完整模型而出问题
            context.setComplete(true);
            // 凡是提前结束的操作必须成功
            transactionModel.setSuccess(true);
            return;
        }
        context.setModel(generateModel(command));
    }

    /**
     * 由原始请求生成领域模型
     *
     * @param request 原始请求
     * @return 领域模型
     */
    private TransactionModel generateModel(final AcceptanceCommand request) {
        TransactionModel acceptanceModel = new TransactionModel();
        final TransPayOrderEntity payOrder = generatePayOrder(request);
        acceptanceModel.setPayOrder(payOrder);
        return acceptanceModel;
    }

    /**
     * 由原始请求生成支付订单（带有支付子订单）
     *
     * @param command 原始请求
     * @return 支付订单
     */
    private TransPayOrderEntity generatePayOrder(final AcceptanceCommand command) {
        Date now = new Date();
        TransPayOrderEntity transPayOrdtraerEntity = new TransPayOrderEntity();
        transPayOrdtraerEntity.setSysCode(command.getSysCode());
        transPayOrdtraerEntity.setBizIdentifyNo(command.getBizIdentifyNo());
        transPayOrdtraerEntity.setBizUniqueNo(command.getBizUniqueNo());
        transPayOrdtraerEntity.setMoney(command.getMoney());
        // 上游可以指定支付通道类型 todo

        transPayOrdtraerEntity.setAccountingEntry(command.getAccountingEntry());
        transPayOrdtraerEntity.setMemo(command.getMemo());
        transPayOrdtraerEntity.setBusinessEntity(command.getBusinessEntity());

        transPayOrdtraerEntity.setNotifyUri(command.getNotifyUri());
        transPayOrdtraerEntity.setExtendInfo(command.getExtendInfo());
        transPayOrdtraerEntity.setBizInfo(command.getBizInfo());

        transPayOrdtraerEntity.setSubOrder(generateSubOrder(command, now));
        transPayOrdtraerEntity.setPaymentRequest(generatePayRequest(transPayOrdtraerEntity));
        return transPayOrdtraerEntity;
    }

    /**
     * 由原始请求生成支付子订单列表
     *
     * @param command 原始请求
     * @param date    业务日期
     * @return 支付子订单列表
     */
    private TransSubOrderEntity generateSubOrder(final AcceptanceCommand command, final Date date) {
        if (command instanceof AlipayAcceptanceCommand) {
            return generateAlipaySubOrder((AlipayAcceptanceCommand) command, date);
        }
        return null;
    }

    /**
     * 由原始请求生成支付宝余额支付子订单
     *
     * @param command 原始请求
     * @param date    业务日期
     * @return 支付子订单
     */
    private TransSubOrderEntity generateAlipaySubOrder(final AlipayAcceptanceCommand command, final Date date) {
        TransSubOrderEntity subOrder = AlipayAcceptanceCommandConvertor.toDomainEntity(command);
        subOrder.setGmtCreated(date);
        subOrder.setGmtModified(date);
        return subOrder;
    }

    /**
     * 由支付订单生成支付请求
     *
     * @param payOrder 支付订单
     * @return 支付请求
     */
    private TransRequestEntity generatePayRequest(final TransPayOrderEntity payOrder) {
        TransRequestEntity paymentRequest = new TransRequestEntity();
        paymentRequest.setRequestType(TransRequestTypeEnum.PAYMENT.getCode().shortValue());
        paymentRequest.setBizIdentifyNo(payOrder.getBizIdentifyNo());
        paymentRequest.setBizUniqueNo(payOrder.getBizUniqueNo());
        // 由支付订单来确定调用地址
//        InsUnderlyingPayChannelTypeEnum payChannelType = InsUnderlyingPayChannelTypeEnum.getByCode(payOrder.getPayChannelType().intValue());
//        paymentRequest.setRequestAddr(null == payChannelType ? "" : payChannelType.getAddr());
        // 暂时不设置设置其他空参数
        return paymentRequest;
    }
}

