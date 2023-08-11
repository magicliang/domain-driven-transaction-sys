package com.magicliang.transaction.sys.core.domain.strategy.notification;

import com.magicliang.transaction.sys.core.domain.enums.NotificationStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.BaseStrategy;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.request.notification.NotificationRequest;
import com.magicliang.transaction.sys.core.model.response.notification.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: kafka 通知策略
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 17:09
 */
@Slf4j
@Component
public class KafkaNotificationStrategy extends BaseStrategy implements
        DomainStrategy<NotificationRequest, NotificationResponse, NotificationStrategyEnum> {

    /**
     * 标识自己的类型
     *
     * @return 类型
     */
    @Override
    public NotificationStrategyEnum identify() {
        return NotificationStrategyEnum.KAFKA;
    }

    /**
     * 执行领域请求，生成领域响应
     *
     * @param notificationRequest 领域请求
     * @param notificationResponse 领域响应
     */
    @Override
    public void execute(final NotificationRequest notificationRequest,
            final NotificationResponse notificationResponse) {
        throw new UnsupportedOperationException();
    }
}
