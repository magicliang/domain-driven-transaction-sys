package com.magicliang.transaction.sys.biz.service.impl.job;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.BATCH_PAYMENT_JOB_ERROR;

import com.magicliang.transaction.sys.biz.service.impl.facade.IPaymentFacade;
import com.magicliang.transaction.sys.biz.shared.request.payment.UnPaidOrderQuery;
import com.magicliang.transaction.sys.common.enums.TransEnvEnum;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.config.CommonConfig;
import com.magicliang.transaction.sys.core.config.DynamicConfig;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 发起支付任务
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 17:55
 */
@Slf4j
public class PaymentJob {

    /**
     * 通用配置
     */
    @Autowired
    protected CommonConfig commonConfig;

    /**
     * 支付门面
     */
    @Autowired
    private IPaymentFacade paymentFacade;

    /**
     * 任务调度服务，下游任务失败会导致任务重跑，依赖于任务处理框架的自动化调度
     * 定时查询未支付订单，进行批量支付
     */
    public void start() {
        // given
        final UnPaidOrderQuery unPaidOrderQuery = buildQuery();
        // when
        boolean result = paymentFacade.batchPay(unPaidOrderQuery);
        // then
        AssertUtils.isTrue(result, BATCH_PAYMENT_JOB_ERROR, BATCH_PAYMENT_JOB_ERROR.getErrorMsg());
    }


    /**
     * 产生当前环境下的批量支付请求
     *
     * @return 当前环境下的批量支付请求
     */
    private UnPaidOrderQuery buildQuery() {
        final UnPaidOrderQuery unPaidOrderQuery = new UnPaidOrderQuery();
        unPaidOrderQuery.setBatchSize(DynamicConfig.UNPAID_ORDER_QUERY_BATCH_SIZE);
        // 大批量调度一定要区分环境
        unPaidOrderQuery.setEnv(Objects.requireNonNull(TransEnvEnum.getByDesc(commonConfig.getEnv())).getCode());
        return unPaidOrderQuery;
    }
}
