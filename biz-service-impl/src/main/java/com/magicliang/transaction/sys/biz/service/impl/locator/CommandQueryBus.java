package com.magicliang.transaction.sys.biz.service.impl.locator;

import com.magicliang.transaction.sys.biz.service.impl.handler.BaseHandler;
import com.magicliang.transaction.sys.biz.service.impl.request.HandlerRequest;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_OP_ERROR;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 命令查询总线
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 15:03
 */
@Slf4j
@Service
public class CommandQueryBus {

    /**
     * 处理器列表
     */
    @Autowired
    private List<BaseHandler<? extends HandlerRequest, ? extends TransactionModel, ? extends TransTransactionContext>> handlers;

    /**
     * 分发请求，获取交易模型
     *
     * @param req 请求实参
     * @param <R> 请求类型参数
     * @return 交易模型
     */
    @SuppressWarnings("unchecked")
    public <R extends HandlerRequest> TransactionModel send(R req) {
        TransactionModel fallbackModel = new TransactionModel();
        fallbackModel.setSuccess(false);
        String errorCode = INVALID_OP_ERROR.getSynthesizedErrorCode();
        String errorMsg = INVALID_OP_ERROR.getErrorMsg();
        TransTransactionContext context = null;
        long begin = System.currentTimeMillis();
        for (BaseHandler handler : handlers) {
            if (req.identify() == handler.identify()) {
                try {
                    context = handler.execute(req);
                    return context.getModel();
                } catch (BaseTransException ex) {
                    // 如果发生异常，用异常的错误码和错误信息填充模型
                    errorCode = ex.getErrorCode();
                    errorMsg = ex.getErrorMsg();
                    Throwable cause = ex.getCause();
                    String causeMsg = cause == null ? "" : cause.toString();
                    String errorLog = String.format("handler exception，code：%s，message：%s，cause：%s", errorCode, errorMsg, causeMsg);
                    log.error(errorLog, ex);
                } catch (Exception ex) {
                    log.error("unknown exception", ex);
                } finally {
                    long elapsed = System.currentTimeMillis() - begin;
                    // TODO：对请求进行打点
                    log.info("handler execution finished, req：{}, model：{}, elapsed：{}ms", JsonUtils.toJson(req), JsonUtils.toJson(context.getModel()), elapsed);
                }
            }
        }
        fallbackModel.setErrorCode(errorCode);
        fallbackModel.setErrorMsg(errorMsg);
        return fallbackModel;
    }
}
