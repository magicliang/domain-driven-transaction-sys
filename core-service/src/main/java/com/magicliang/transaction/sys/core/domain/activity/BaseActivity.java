package com.magicliang.transaction.sys.core.domain.activity;

import com.magicliang.transaction.sys.core.config.CommonConfig;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.request.IRequest;
import com.magicliang.transaction.sys.core.model.response.IResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基础领域活动类
 * <p>
 * 如何解释 activity 和 strategy 的差别？
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 13:47
 */
@Slf4j
@Setter
public abstract class BaseActivity<R extends IRequest, S extends IResponse, E extends Enum> {

    /**
     * 通用配置
     */
    @Autowired
    protected CommonConfig commonConfig;

    /**
     * 活动执行，唯一的 public method
     * TODO：这里这个泛型的 ？？应该加以充分的解释
     * @param context 交易上下文
     */
    public void execute(TransTransactionContext<?, ? extends TransactionModel> context) {
        // 1. 前置执行钩子，有可能导致活动提前 complete
        boolean isComplete = preExecution(context);
        if (isComplete) {
            return;
        }
        // 2. 真执行钩子
        realExecute(context);
        // 3. 后置执行钩子
        postExecution(context);
    }

    /**
     * 活动执行以前，执行前置的平衡性检查，并填充领域模型，活动级的幂等在这里执行，模型的旧值校验在这里执行
     *
     * @param context 交易上下文
     */
    protected boolean preExecution(TransTransactionContext<?, ? extends TransactionModel> context) {
        log.info("pre execution");
        return false;
    }

    /**
     * 真执行流程，子类必须
     *
     * @param context 交易上下文
     */
    protected void realExecute(final TransTransactionContext<?, ? extends TransactionModel> context) {
        // 1. 确认当前活动是否提前结束
        if (isComplete(context)) {
            return;
        }
        // 2. 生成领域能力请求
        R request = assembleDomainRequest(context);
        // 3. 生成领域能力响应
        S response = assembleDomainResponse(context);
        // 4. 策略点决策钩子
        for (DomainStrategy<R, S, E> strategy : getStrategies()) {
            if (strategy.isSupport(decideStrategy(context))) {
                strategy.execute(request, response);
            }
        }
    }

    /**
     * 是否本活动已完成
     *
     * @param context 交易上下文
     * @return 本活动是否已完成
     */
    protected boolean isComplete(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return context.isComplete();
    }

    /**
     * 返回当前的策略实现集合
     *
     * @return 当前的策略实现集合
     */
    protected List<DomainStrategy<R, S, E>> getStrategies() {
        return Collections.emptyList();
    }

    /**
     * 生成领域能力请求，子类型必须实现这个方法，将模型旧值转换为新值在这里实现
     *
     * @param context 交易上下文
     * @return 领域能力请求
     */
    protected abstract R assembleDomainRequest(final TransTransactionContext<?, ? extends TransactionModel> context);

    /**
     * 生成领域能力响应，子类型必须实现这个方法
     *
     * @param context 交易上下文
     * @return 领域能力响应
     */
    protected abstract S assembleDomainResponse(final TransTransactionContext<?, ? extends TransactionModel> context);

    /**
     * 决策当前活动的策略点，子类型必须实现这个方法。
     * 未来可以在框架层接入流程引擎，靠流程引擎来获取策略点。
     *
     * @param context 交易上下文
     * @return 作为决策结果的策略点
     */
    protected abstract E decideStrategy(TransTransactionContext<?, ? extends TransactionModel> context);

    /**
     * 活动执行以后，执行后置的平衡性检查和结果对领域模型的装填
     *
     * @param context 交易上下文
     */
    protected void postExecution(TransTransactionContext<?, ? extends TransactionModel> context) {
        log.info("post execution");
    }
}
