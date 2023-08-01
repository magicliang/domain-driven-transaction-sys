package com.magicliang.transaction.sys.core.model.response.notification;

import com.magicliang.transaction.sys.core.model.response.IResponse;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通知响应
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:23
 */
@Data
public class NotificationResponse implements IResponse {

    /**
     * 通知是否成功
     */
    private boolean notificationSuccess;
}
