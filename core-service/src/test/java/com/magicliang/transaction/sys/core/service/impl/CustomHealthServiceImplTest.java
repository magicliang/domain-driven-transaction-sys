package com.magicliang.transaction.sys.core.service.impl;

import com.magicliang.transaction.sys.common.enums.HealthStatusEnum;
import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;
import com.magicliang.transaction.sys.common.util.health.HealthStatusVO;
import com.magicliang.transaction.sys.core.service.HealthIndicator;
import com.magicliang.transaction.sys.core.service.ICustomHealthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CustomHealthServiceImpl 单元测试
 *
 * @author magicliang
 * date: 2026-03-23
 */
@ExtendWith(MockitoExtension.class)
class CustomHealthServiceImplTest {

    @Mock
    private HealthIndicator databaseIndicator;

    @Mock
    private HealthIndicator applicationIndicator;

    private ICustomHealthService healthService;

    /**
     * 辅助方法：创建简单的 Map（Java 8 兼容）
     */
    private Map<String, String> createMap(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @BeforeEach
    void setUp() {
        List<HealthIndicator> indicators = Arrays.asList(databaseIndicator, applicationIndicator);
        healthService = new CustomHealthServiceImpl(indicators);
    }

    @Test
    void testGetHealthStatus_AllComponentsUp() {
        // Given
        when(databaseIndicator.getComponentName()).thenReturn("database");
        when(applicationIndicator.getComponentName()).thenReturn("application");

        Map<String, Object> dbDetails = new HashMap<>();
        dbDetails.put("master", createMap("status", "UP"));
        when(databaseIndicator.check()).thenReturn(new ComponentHealthVO("UP", dbDetails));

        Map<String, Object> appDetails = new HashMap<>();
        appDetails.put("uptime", 3600L);
        when(applicationIndicator.check()).thenReturn(new ComponentHealthVO("UP", appDetails));

        // When
        HealthStatusVO result = healthService.getHealthStatus(null);

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.UP.getValue(), result.getStatus());
        assertNotNull(result.getTimestamp());
        assertEquals(2, result.getComponents().size());
        assertTrue(result.getComponents().containsKey("database"));
        assertTrue(result.getComponents().containsKey("application"));

        verify(databaseIndicator, times(1)).check();
        verify(applicationIndicator, times(1)).check();
    }

    @Test
    void testGetHealthStatus_FilterByComponent() {
        // Given
        when(databaseIndicator.getComponentName()).thenReturn("database");
        when(applicationIndicator.getComponentName()).thenReturn("application");

        Map<String, Object> dbDetails = new HashMap<>();
        dbDetails.put("master", createMap("status", "UP"));
        when(databaseIndicator.check()).thenReturn(new ComponentHealthVO("UP", dbDetails));

        // When
        HealthStatusVO result = healthService.getHealthStatus("database");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getComponents().size());
        assertTrue(result.getComponents().containsKey("database"));
        assertFalse(result.getComponents().containsKey("application"));

        verify(databaseIndicator, times(1)).check();
        verify(applicationIndicator, never()).check();
    }

    @Test
    void testGetHealthStatus_OneComponentDown() {
        // Given
        when(databaseIndicator.getComponentName()).thenReturn("database");
        when(applicationIndicator.getComponentName()).thenReturn("application");

        Map<String, Object> dbDetails = new HashMap<>();
        dbDetails.put("error", "Connection refused");
        when(databaseIndicator.check()).thenReturn(new ComponentHealthVO("DOWN", dbDetails));

        Map<String, Object> appDetails = new HashMap<>();
        appDetails.put("uptime", 3600L);
        when(applicationIndicator.check()).thenReturn(new ComponentHealthVO("UP", appDetails));

        // When
        HealthStatusVO result = healthService.getHealthStatus(null);

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.DOWN.getValue(), result.getStatus());
        assertEquals(2, result.getComponents().size());
    }

    @Test
    void testGetHealthStatus_OneComponentWarn() {
        // Given
        when(databaseIndicator.getComponentName()).thenReturn("database");
        when(applicationIndicator.getComponentName()).thenReturn("application");

        Map<String, Object> dbDetails = new HashMap<>();
        dbDetails.put("master", createMap("status", "UP"));
        when(databaseIndicator.check()).thenReturn(new ComponentHealthVO("UP", dbDetails));

        Map<String, Object> appDetails = new HashMap<>();
        appDetails.put("memoryUsage", 0.95);
        when(applicationIndicator.check()).thenReturn(new ComponentHealthVO("WARN", appDetails));

        // When
        HealthStatusVO result = healthService.getHealthStatus(null);

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.WARN.getValue(), result.getStatus());
    }

    @Test
    void testGetHealthStatus_ExceptionIsolated() {
        // Given
        when(databaseIndicator.getComponentName()).thenReturn("database");
        when(applicationIndicator.getComponentName()).thenReturn("application");

        when(databaseIndicator.check()).thenThrow(new RuntimeException("Database connection failed"));

        Map<String, Object> appDetails = new HashMap<>();
        appDetails.put("uptime", 3600L);
        when(applicationIndicator.check()).thenReturn(new ComponentHealthVO("UP", appDetails));

        // When
        HealthStatusVO result = healthService.getHealthStatus(null);

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.DOWN.getValue(), result.getStatus());
        assertEquals(2, result.getComponents().size());

        // application 应该仍然被检查
        ComponentHealthVO appHealth = result.getComponents().get("application");
        assertNotNull(appHealth);
        assertEquals(HealthStatusEnum.UP.getValue(), appHealth.getStatus());

        // database 应该标记为 DOWN
        ComponentHealthVO dbHealth = result.getComponents().get("database");
        assertNotNull(dbHealth);
        assertEquals(HealthStatusEnum.DOWN.getValue(), dbHealth.getStatus());
    }

    @Test
    void testGetHealthStatus_UnknownComponent() {
        // Given
        when(databaseIndicator.getComponentName()).thenReturn("database");
        when(applicationIndicator.getComponentName()).thenReturn("application");

        // When
        HealthStatusVO result = healthService.getHealthStatus("unknown");

        // Then
        assertNotNull(result);
        assertEquals(0, result.getComponents().size());
        
        verify(databaseIndicator, never()).check();
        verify(applicationIndicator, never()).check();
    }
}
