package com.magicliang.transaction.sys.biz.service.impl.facade;

import com.magicliang.transaction.sys.biz.shared.request.notification.NotificationCommand;
import com.magicliang.transaction.sys.biz.shared.request.notification.UnSentNotificationQuery;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;

import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通知门面
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:45
 */
public interface INotificationFacade {

    /**
     * 查询并发送全部未完成通知
     *
     * @param unSentNotificationQuery 未完成通知
     * @return 操作结果
     */
    boolean batchNotify(UnSentNotificationQuery unSentNotificationQuery);

    /**
     * 批量发送全部未完成通知，使用线程池来执行发送任务
     *
     * @param payOrders 待发送通知的支付订单列表
     */
    void batchNotify(final List<TransPayOrderEntity> payOrders);

    /**
     * 执行通知
     *
     * @param notificationCommand 通知命令
     * @return 交易模型
     */
    TransactionModel notify(NotificationCommand notificationCommand);
}
