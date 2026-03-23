package com.magicliang.transaction.sys.core.service;

import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;

/**
 * 健康检查器接口
 *
 * @author magicliang
 * date: 2026-03-23
 */
public interface HealthIndicator {

    /**
     * 获取组件名称
     *
     * @return 组件名称（如 "database"、"application"）
     */
    String getComponentName();

    /**
     * 执行健康检查
     *
     * @return 组件健康状态
     */
    ComponentHealthVO check();
}
