package com.magicliang.transaction.sys.biz.service.impl.web.controller;

import com.magicliang.transaction.sys.biz.service.impl.web.model.vo.ApiResult;
import com.magicliang.transaction.sys.biz.service.impl.web.model.vo.DatabaseCountVO;
import com.magicliang.transaction.sys.core.service.IDatabaseCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据库表计数查询控制器
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/database")
public class DatabaseCountController {

    @Autowired
    private IDatabaseCountService databaseCountService;

    /**
     * 查询指定表的总记录数
     *
     * @param tableName 表名
     * @return 表名和总记录数
     */
    @GetMapping("/count")
    public ApiResult<DatabaseCountVO> getCount(@RequestParam String tableName) {
        log.info("收到数据库计数查询请求: tableName={}", tableName);

        try {
            long totalCount = databaseCountService.getTotalCount(tableName);
            DatabaseCountVO vo = new DatabaseCountVO(tableName, totalCount);
            return ApiResult.success(vo);
        } catch (IllegalArgumentException e) {
            log.warn("数据库计数查询参数错误: tableName={}, error={}", tableName, e.getMessage());
            return ApiResult.fail(e.getMessage(), null);
        } catch (Exception e) {
            log.error("数据库计数查询失败: tableName={}", tableName, e);
            return ApiResult.fail("查询失败: " + e.getMessage(), null);
        }
    }
}
