package com.magicliang.transaction.sys.biz.service.impl.job;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.BATCH_PAYMENT_JOB_ERROR;

import com.magicliang.transaction.sys.biz.service.impl.facade.INotificationFacade;
import com.magicliang.transaction.sys.biz.shared.request.notification.UnSentNotificationQuery;
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
 * description: 专门被抽取出来的通知任务
 * 专门设计出来的任务有个好处：调度模型可以被本服务控制
 * 否则可以考虑引入一个最大通知型分布式事务补偿框架
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 17:37
 */
@Slf4j
public class NotificationJob {

    /**
     * 通用配置
     */
    @Autowired
    protected CommonConfig commonConfig;

    /**
     * 通知门面
     */
    @Autowired
    private INotificationFacade notificationFacade;

    /**
     * 任务调度服务，下游任务失败会导致任务重跑，依赖于任务处理框架的自动化调度
     * 定时查询未未通知订单，进行批量通知
     * 生命周期执行钩子
     * 这种钩子妙就秒在连接口都无耦合，类似 init-method 的配置即可驱动任务执行
     */
    public void start() {
        // given
        final UnSentNotificationQuery unSentNotificationQuery = buildQuery();
        // when
        boolean result = notificationFacade.batchNotify(unSentNotificationQuery);
        // then
        AssertUtils.isTrue(result, BATCH_PAYMENT_JOB_ERROR, BATCH_PAYMENT_JOB_ERROR.getErrorMsg());
    }

    /**
     * 产生当前环境下的批量通知请求
     *
     * @return 当前环境下的批量通知请求
     */
    private UnSentNotificationQuery buildQuery() {
        final UnSentNotificationQuery unPaidOrderQuery = new UnSentNotificationQuery();
        // 使用支付订单的查询批次大小
        unPaidOrderQuery.setBatchSize(DynamicConfig.UNPAID_ORDER_QUERY_BATCH_SIZE);
        // 大批量调度一定要区分环境
        unPaidOrderQuery.setEnv(Objects.requireNonNull(TransEnvEnum.getByDesc(commonConfig.getEnv())).getCode());
        return unPaidOrderQuery;
    }
}
