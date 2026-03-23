package com.magicliang.transaction.sys.biz.service.impl.web.controller;

import com.magicliang.transaction.sys.biz.service.impl.web.model.vo.ApiResult;
import com.magicliang.transaction.sys.common.util.health.HealthStatusVO;
import com.magicliang.transaction.sys.core.service.ICustomHealthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义健康检查控制器
 * 提供业务级健康检查 API，返回结构化的健康状态信息
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Slf4j
@RestController
@RequestMapping("/res/v1/health")
public class HealthController {

    /**
     * 支持的组件名称列表
     */
    private static final List<String> SUPPORTED_COMPONENTS = Arrays.asList("database", "application");

    @Autowired
    private ICustomHealthService customHealthService;

    /**
     * 查询健康状态
     *
     * @param component 组件名称（可选），支持：database、application。不传则返回所有组件
     * @return 健康状态响应
     */
    @GetMapping("/custom")
    public ApiResult<HealthStatusVO> getHealthStatus(@RequestParam(required = false) String component) {
        log.info("收到健康检查请求: component={}", component);
        long startTime = System.currentTimeMillis();

        try {
            // 参数校验
            if (component != null && !SUPPORTED_COMPONENTS.contains(component)) {
                log.warn("未知的组件名称: {}", component);
                return ApiResult.fail("未知的组件名称: " + component, null);
            }

            // 调用服务层
            HealthStatusVO healthStatus = customHealthService.getHealthStatus(component);

            long responseTime = System.currentTimeMillis() - startTime;
            log.info("健康检查响应: status={}, 耗时={}ms", healthStatus.getStatus(), responseTime);

            return ApiResult.success(healthStatus);
        } catch (Exception e) {
            log.error("健康检查失败", e);
            return ApiResult.fail("健康检查失败: " + e.getMessage(), null);
        }
    }
}
