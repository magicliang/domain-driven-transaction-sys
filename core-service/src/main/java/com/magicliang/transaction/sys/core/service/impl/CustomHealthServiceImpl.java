package com.magicliang.transaction.sys.core.service.impl;

import com.magicliang.transaction.sys.common.enums.HealthStatusEnum;
import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;
import com.magicliang.transaction.sys.common.util.health.HealthStatusVO;
import com.magicliang.transaction.sys.core.service.HealthIndicator;
import com.magicliang.transaction.sys.core.service.ICustomHealthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义健康检查服务实现
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Slf4j
@Service
public class CustomHealthServiceImpl implements ICustomHealthService {

    private final List<HealthIndicator> healthIndicators;

    @Autowired
    public CustomHealthServiceImpl(List<HealthIndicator> healthIndicators) {
        this.healthIndicators = healthIndicators;
    }

    @Override
    public HealthStatusVO getHealthStatus(String component) {
        long startTime = System.currentTimeMillis();

        Map<String, ComponentHealthVO> components = new HashMap<>();
        String overallStatus = HealthStatusEnum.UP.getValue();

        // 遍历所有健康检查器
        for (HealthIndicator indicator : healthIndicators) {
            String componentName = indicator.getComponentName();

            // 如果指定了 component 参数，只检查该组件
            if (component != null && !component.equals(componentName)) {
                continue;
            }

            try {
                ComponentHealthVO componentHealth = indicator.check();
                components.put(componentName, componentHealth);

                // 聚合整体状态：DOWN > WARN > UP
                String componentStatus = componentHealth.getStatus();
                if (HealthStatusEnum.DOWN.getValue().equals(componentStatus)) {
                    overallStatus = HealthStatusEnum.DOWN.getValue();
                } else if (HealthStatusEnum.WARN.getValue().equals(componentStatus)
                        && !HealthStatusEnum.DOWN.getValue().equals(overallStatus)) {
                    overallStatus = HealthStatusEnum.WARN.getValue();
                }
            } catch (Exception e) {
                log.error("健康检查器 {} 执行失败", componentName, e);
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("error", e.getMessage());
                components.put(componentName, new ComponentHealthVO(HealthStatusEnum.DOWN.getValue(), errorDetails));
                overallStatus = HealthStatusEnum.DOWN.getValue();
            }
        }

        // 生成时间戳（ISO 8601 格式）
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now().atOffset(ZoneOffset.UTC));

        long responseTime = System.currentTimeMillis() - startTime;
        log.info("健康检查完成，整体状态: {}, 耗时: {}ms, 组件数: {}", overallStatus, responseTime, components.size());

        return new HealthStatusVO(overallStatus, timestamp, components);
    }
}
