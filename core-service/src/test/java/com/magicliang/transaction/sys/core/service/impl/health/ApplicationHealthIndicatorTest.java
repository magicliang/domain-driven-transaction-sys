package com.magicliang.transaction.sys.core.service.impl.health;

import com.magicliang.transaction.sys.common.enums.HealthStatusEnum;
import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ApplicationHealthIndicator 单元测试
 *
 * @author magicliang
 * date: 2026-03-23
 */
class ApplicationHealthIndicatorTest {

    private ApplicationHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        healthIndicator = new ApplicationHealthIndicator();
    }

    @Test
    void testGetComponentName() {
        // Given & When
        String componentName = healthIndicator.getComponentName();

        // Then
        assertEquals("application", componentName);
    }

    @Test
    void testCheck_NormalMemoryUsage() {
        // Given & When
        ComponentHealthVO result = healthIndicator.check();

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.UP.getValue(), result.getStatus());
        assertNotNull(result.getDetails());

        Map<String, Object> details = result.getDetails();
        assertTrue(details.containsKey("uptime"));
        assertTrue(details.containsKey("memoryUsage"));

        Long uptime = (Long) details.get("uptime");
        Double memoryUsage = (Double) details.get("memoryUsage");

        assertTrue(uptime >= 0, "运行时长应该大于等于0");
        assertTrue(memoryUsage >= 0 && memoryUsage <= 1.0, "内存使用率应该在 0-1 之间");
    }

    @Test
    void testCheck_ReturnsValidTimestamp() {
        // Given & When
        ComponentHealthVO result = healthIndicator.check();

        // Then
        assertNotNull(result.getDetails());
        // uptime 应该是正数（应用已经运行了一段时间）
        Long uptime = (Long) result.getDetails().get("uptime");
        assertTrue(uptime > 0, "应用已启动，uptime 应该大于0");
    }
}
