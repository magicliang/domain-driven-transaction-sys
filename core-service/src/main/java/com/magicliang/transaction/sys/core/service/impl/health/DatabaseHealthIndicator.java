package com.magicliang.transaction.sys.core.service.impl.health;

import com.magicliang.transaction.sys.common.enums.HealthStatusEnum;
import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;
import com.magicliang.transaction.sys.core.service.HealthIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库健康检查器
 * 检查 master 和 slave1 数据源的连接可用性
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Slf4j
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    /**
     * 健康检查超时时间（毫秒）
     */
    private static final long HEALTH_CHECK_TIMEOUT_MS = 3000;

    private final JdbcTemplate masterJdbcTemplate;
    private final JdbcTemplate slave1JdbcTemplate;

    public DatabaseHealthIndicator(
            @Qualifier("masterJdbcTemplate") JdbcTemplate masterJdbcTemplate,
            @Qualifier("slave1JdbcTemplate") JdbcTemplate slave1JdbcTemplate) {
        this.masterJdbcTemplate = masterJdbcTemplate;
        this.slave1JdbcTemplate = slave1JdbcTemplate;
    }

    @Override
    public String getComponentName() {
        return "database";
    }

    @Override
    public ComponentHealthVO check() {
        Map<String, Object> details = new HashMap<>();
        String overallStatus = HealthStatusEnum.UP.getValue();

        // 检查 master 数据源
        Map<String, Object> masterResult = checkDataSource("master", masterJdbcTemplate);
        details.put("master", masterResult);
        if (HealthStatusEnum.DOWN.getValue().equals(masterResult.get("status"))) {
            overallStatus = HealthStatusEnum.DOWN.getValue();
        }

        // 检查 slave1 数据源
        Map<String, Object> slave1Result = checkDataSource("slave1", slave1JdbcTemplate);
        details.put("slave1", slave1Result);
        if (HealthStatusEnum.DOWN.getValue().equals(slave1Result.get("status"))) {
            overallStatus = HealthStatusEnum.DOWN.getValue();
        }

        return new ComponentHealthVO(overallStatus, details);
    }

    /**
     * 检查单个数据源的健康状态
     *
     * @param dataSourceName 数据源名称
     * @param jdbcTemplate JdbcTemplate 实例
     * @return 检查结果（包含 status、responseTime、error 等字段）
     */
    private Map<String, Object> checkDataSource(String dataSourceName, JdbcTemplate jdbcTemplate) {
        Map<String, Object> result = new HashMap<>();
        long startTime = System.currentTimeMillis();

        try {
            Integer queryResult = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            long responseTime = System.currentTimeMillis() - startTime;

            if (queryResult != null && queryResult == 1) {
                result.put("status", HealthStatusEnum.UP.getValue());
                result.put("responseTime", responseTime);
                log.debug("{} 数据源健康检查成功，响应时间: {}ms", dataSourceName, responseTime);
            } else {
                result.put("status", HealthStatusEnum.DOWN.getValue());
                result.put("error", "查询结果异常");
                log.warn("{} 数据源健康检查失败：查询结果异常", dataSourceName);
            }
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            result.put("status", HealthStatusEnum.DOWN.getValue());
            result.put("responseTime", responseTime);
            result.put("error", e.getMessage());

            if (responseTime >= HEALTH_CHECK_TIMEOUT_MS) {
                result.put("error", "检查超时");
                log.warn("{} 数据源健康检查超时: {}ms", dataSourceName, responseTime);
            } else {
                log.error("{} 数据源健康检查失败", dataSourceName, e);
            }
        }

        return result;
    }
}
