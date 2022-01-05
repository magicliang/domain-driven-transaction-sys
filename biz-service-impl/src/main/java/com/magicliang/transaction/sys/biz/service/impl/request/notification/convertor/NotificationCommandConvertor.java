package com.magicliang.transaction.sys.biz.service.impl.request.notification.convertor;

import com.magicliang.transaction.sys.biz.service.impl.request.notification.NotificationCommand;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付命令转换器
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 14:48
 */
public class NotificationCommandConvertor {

    /**
     * 私有构造器
     */
    private NotificationCommandConvertor() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从领域模型转化成命令
     *
     * @param payOrder 领域模型
     * @return 命令
     */
    public static NotificationCommand fromDomainEntity(final TransPayOrderEntity payOrder) {
        if (null == payOrder) {
            return null;
        }
        return null;
    }
}
