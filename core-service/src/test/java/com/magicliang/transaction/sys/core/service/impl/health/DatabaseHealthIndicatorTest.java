package com.magicliang.transaction.sys.core.service.impl.health;

import com.magicliang.transaction.sys.common.enums.HealthStatusEnum;
import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * DatabaseHealthIndicator 单元测试
 *
 * @author magicliang
 * date: 2026-03-23
 */
@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {

    @Mock
    private JdbcTemplate masterJdbcTemplate;

    @Mock
    private JdbcTemplate slave1JdbcTemplate;

    private DatabaseHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        healthIndicator = new DatabaseHealthIndicator(masterJdbcTemplate, slave1JdbcTemplate);
    }

    @Test
    void testGetComponentName() {
        // Given & When
        String componentName = healthIndicator.getComponentName();

        // Then
        assertEquals("database", componentName);
    }

    @Test
    void testCheck_AllDataSourcesUp() {
        // Given
        when(masterJdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);
        when(slave1JdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);

        // When
        ComponentHealthVO result = healthIndicator.check();

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.UP.getValue(), result.getStatus());
        assertNotNull(result.getDetails());

        Map<String, Object> details = result.getDetails();
        assertTrue(details.containsKey("master"));
        assertTrue(details.containsKey("slave1"));

        Map<String, Object> masterResult = (Map<String, Object>) details.get("master");
        Map<String, Object> slave1Result = (Map<String, Object>) details.get("slave1");

        assertEquals(HealthStatusEnum.UP.getValue(), masterResult.get("status"));
        assertEquals(HealthStatusEnum.UP.getValue(), slave1Result.get("status"));
        assertTrue((Long) masterResult.get("responseTime") >= 0);
        assertTrue((Long) slave1Result.get("responseTime") >= 0);

        verify(masterJdbcTemplate, times(1)).queryForObject("SELECT 1", Integer.class);
        verify(slave1JdbcTemplate, times(1)).queryForObject("SELECT 1", Integer.class);
    }

    @Test
    void testCheck_MasterDown() {
        // Given
        when(masterJdbcTemplate.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new RuntimeException("Connection refused"));
        when(slave1JdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);

        // When
        ComponentHealthVO result = healthIndicator.check();

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.DOWN.getValue(), result.getStatus());

        Map<String, Object> details = result.getDetails();
        Map<String, Object> masterResult = (Map<String, Object>) details.get("master");
        Map<String, Object> slave1Result = (Map<String, Object>) details.get("slave1");

        assertEquals(HealthStatusEnum.DOWN.getValue(), masterResult.get("status"));
        assertNotNull(masterResult.get("error"));
        assertEquals(HealthStatusEnum.UP.getValue(), slave1Result.get("status"));
    }

    @Test
    void testCheck_Slave1Down() {
        // Given
        when(masterJdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);
        when(slave1JdbcTemplate.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new RuntimeException("Connection timeout"));

        // When
        ComponentHealthVO result = healthIndicator.check();

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.DOWN.getValue(), result.getStatus());

        Map<String, Object> details = result.getDetails();
        Map<String, Object> masterResult = (Map<String, Object>) details.get("master");
        Map<String, Object> slave1Result = (Map<String, Object>) details.get("slave1");

        assertEquals(HealthStatusEnum.UP.getValue(), masterResult.get("status"));
        assertEquals(HealthStatusEnum.DOWN.getValue(), slave1Result.get("status"));
        assertNotNull(slave1Result.get("error"));
    }

    @Test
    void testCheck_BothDown() {
        // Given
        when(masterJdbcTemplate.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new RuntimeException("Master connection failed"));
        when(slave1JdbcTemplate.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new RuntimeException("Slave connection failed"));

        // When
        ComponentHealthVO result = healthIndicator.check();

        // Then
        assertNotNull(result);
        assertEquals(HealthStatusEnum.DOWN.getValue(), result.getStatus());

        Map<String, Object> details = result.getDetails();
        Map<String, Object> masterResult = (Map<String, Object>) details.get("master");
        Map<String, Object> slave1Result = (Map<String, Object>) details.get("slave1");

        assertEquals(HealthStatusEnum.DOWN.getValue(), masterResult.get("status"));
        assertEquals(HealthStatusEnum.DOWN.getValue(), slave1Result.get("status"));
    }
}
