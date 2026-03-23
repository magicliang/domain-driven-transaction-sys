package com.magicliang.transaction.sys.core.service;

import com.magicliang.transaction.sys.common.util.health.HealthStatusVO;

/**
 * 自定义健康检查服务接口
 *
 * @author magicliang
 * date: 2026-03-23
 */
public interface ICustomHealthService {

    /**
     * 获取健康状态
     *
     * @param component 组件名称（可选），如果为 null 则返回所有组件
     * @return 健康状态 VO
     */
    HealthStatusVO getHealthStatus(String component);
}
