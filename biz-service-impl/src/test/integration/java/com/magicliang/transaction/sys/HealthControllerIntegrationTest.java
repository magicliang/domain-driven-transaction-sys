package com.magicliang.transaction.sys;

import com.magicliang.transaction.sys.biz.service.impl.web.model.vo.ApiResult;
import com.magicliang.transaction.sys.common.util.health.HealthStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HealthController 集成测试
 * 测试自定义健康检查 API 的完整流程
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Slf4j
@SpringBootApplication(excludeName = {
        "ch.vorburger.mariadb4j.springboot.autoconfigure.DataSourceAutoConfiguration",
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
@SpringBootTest(
        classes = {DomainDrivenTransactionSysApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class HealthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetHealthStatus_AllComponents() {
        // Given & When
        ResponseEntity<ApiResult> response = restTemplate.getForEntity(
                "/res/v1/health/custom",
                ApiResult.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getCode() == 0);

        ApiResult<HealthStatusVO> result = response.getBody();
        assertNotNull(result.getData());
        assertNotNull(result.getData().getStatus());
        assertNotNull(result.getData().getTimestamp());
        assertNotNull(result.getData().getComponents());

        log.info("健康检查响应: status={}, components={}",
                result.getData().getStatus(),
                result.getData().getComponents().keySet());
    }

    @Test
    void testGetHealthStatus_FilterByDatabase() {
        // Given & When
        ResponseEntity<ApiResult> response = restTemplate.getForEntity(
                "/res/v1/health/custom?component=database",
                ApiResult.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getCode() == 0);

        ApiResult<HealthStatusVO> result = response.getBody();
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getComponents().size());
        assertTrue(result.getData().getComponents().containsKey("database"));
        assertFalse(result.getData().getComponents().containsKey("application"));

        log.info("数据库组件健康检查: {}", result.getData().getComponents().get("database"));
    }

    @Test
    void testGetHealthStatus_FilterByApplication() {
        // Given & When
        ResponseEntity<ApiResult> response = restTemplate.getForEntity(
                "/res/v1/health/custom?component=application",
                ApiResult.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getCode() == 0);

        ApiResult<HealthStatusVO> result = response.getBody();
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getComponents().size());
        assertTrue(result.getData().getComponents().containsKey("application"));
        assertFalse(result.getData().getComponents().containsKey("database"));

        log.info("应用组件健康检查: {}", result.getData().getComponents().get("application"));
    }

    @Test
    void testGetHealthStatus_UnknownComponent() {
        // Given & When
        ResponseEntity<ApiResult> response = restTemplate.getForEntity(
                "/res/v1/health/custom?component=unknown",
                ApiResult.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(-1, response.getBody().getCode());
        assertNotNull(response.getBody().getMsg());
        assertTrue(response.getBody().getMsg().contains("未知的组件名称"));
        assertNull(response.getBody().getData());

        log.info("未知组件错误响应: {}", response.getBody().getMsg());
    }

    @Test
    void testGetHealthStatus_ResponseFormat() {
        // Given & When
        ResponseEntity<ApiResult> response = restTemplate.getForEntity(
                "/res/v1/health/custom",
                ApiResult.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResult<HealthStatusVO> result = response.getBody();
        assertNotNull(result);
        assertEquals(0, result.getCode());

        HealthStatusVO healthStatus = result.getData();
        assertNotNull(healthStatus);

        // 验证状态字段
        assertTrue(healthStatus.getStatus().equals("UP") ||
                   healthStatus.getStatus().equals("DOWN") ||
                   healthStatus.getStatus().equals("WARN"));

        // 验证时间戳格式（ISO 8601）
        assertTrue(healthStatus.getTimestamp().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));

        // 验证组件不为空
        assertFalse(healthStatus.getComponents().isEmpty());

        log.info("响应格式验证通过: status={}, timestamp={}, componentCount={}",
                healthStatus.getStatus(),
                healthStatus.getTimestamp(),
                healthStatus.getComponents().size());
    }
}
