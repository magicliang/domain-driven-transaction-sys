package com.magicliang.transaction.sys.core.service.impl.health;

import com.magicliang.transaction.sys.common.enums.HealthStatusEnum;
import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;
import com.magicliang.transaction.sys.core.service.HealthIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 应用基础健康检查器
 * 检查 JVM 运行时长和内存使用率
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Slf4j
@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    /**
     * 内存使用率警告阈值（90%）
     */
    private static final double MEMORY_WARN_THRESHOLD = 0.9;

    @Override
    public String getComponentName() {
        return "application";
    }

    @Override
    public ComponentHealthVO check() {
        try {
            Map<String, Object> details = new HashMap<>();

            // 获取 JVM 运行时长（秒）
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            long uptimeMillis = runtimeMXBean.getUptime();
            long uptimeSeconds = TimeUnit.MILLISECONDS.toSeconds(uptimeMillis);
            details.put("uptime", uptimeSeconds);

            // 获取内存使用情况
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            long usedMemory = memoryMXBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryMXBean.getHeapMemoryUsage().getMax();
            double memoryUsage = (double) usedMemory / maxMemory;
            details.put("memoryUsage", Math.round(memoryUsage * 100.0) / 100.0);

            // 判断健康状态
            String status;
            if (memoryUsage > MEMORY_WARN_THRESHOLD) {
                status = HealthStatusEnum.WARN.getValue();
                details.put("warning", "内存使用率过高");
                log.warn("应用内存使用率过高: {}%", Math.round(memoryUsage * 100));
            } else {
                status = HealthStatusEnum.UP.getValue();
            }

            return new ComponentHealthVO(status, details);
        } catch (Exception e) {
            log.error("应用健康检查失败", e);
            Map<String, Object> details = new HashMap<>();
            details.put("error", e.getMessage());
            return new ComponentHealthVO(HealthStatusEnum.DOWN.getValue(), details);
        }
    }
}
