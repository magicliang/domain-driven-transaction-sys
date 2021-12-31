package com.magicliang.transaction.sys.core.model.request.notification;

import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.request.acceptance.AcceptanceRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通知请求
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 17:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationRequest extends AcceptanceRequest {

    /**
     * 通知请求
     */
    private TransRequestEntity notifyRequest;
}