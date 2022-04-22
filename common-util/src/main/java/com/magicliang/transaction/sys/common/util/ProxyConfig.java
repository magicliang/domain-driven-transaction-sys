package com.magicliang.transaction.sys.common.util;

import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 代理配置
 *
 * @author magicliang
 * <p>
 * date: 2022-04-22 19:28
 */
@Data
public class ProxyConfig {

    /**
     * 超时告警的阈值，单位毫秒，默认为3秒
     */
    private long timeoutThreshold = 3000L;
}
