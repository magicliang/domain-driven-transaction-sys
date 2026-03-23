package com.magicliang.transaction.sys.core.service.impl;

import com.magicliang.transaction.sys.common.dal.mybatis.mapper.DatabaseCountMapper;
import com.magicliang.transaction.sys.common.dal.mybatis.mapper.TableMetaMapper;
import com.magicliang.transaction.sys.core.service.IDatabaseCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 数据库表计数查询服务实现
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Service
public class DatabaseCountServiceImpl implements IDatabaseCountService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseCountServiceImpl.class);

    /**
     * 表名正则校验：必须以字母或下划线开头，后续可包含字母、数字、下划线
     */
    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    /**
     * 查询超时时间（毫秒）
     */
    private static final long QUERY_TIMEOUT_MS = 5000;

    /**
     * 表名白名单缓存
     */
    private final Set<String> tableNameWhitelist = ConcurrentHashMap.newKeySet();

    /**
     * 白名单是否已加载
     */
    private volatile boolean whitelistLoaded = false;

    @Autowired
    private DatabaseCountMapper databaseCountMapper;

    @Autowired
    private TableMetaMapper tableMetaMapper;

    /**
     * 查询指定表的总记录数
     *
     * @param tableName 表名
     * @return 表总记录数
     */
    @Override
    public long getTotalCount(String tableName) {
        // 1. 正则校验
        validateTableNameFormat(tableName);

        // 2. 白名单校验（懒加载）
        ensureWhitelistLoaded();
        validateTableNameInWhitelist(tableName);

        // 3. 执行 COUNT 查询（带超时控制）
        return executeCountWithTimeout(tableName);
    }

    /**
     * 校验表名格式
     */
    private void validateTableNameFormat(String tableName) {
        if (!TABLE_NAME_PATTERN.matcher(tableName).matches()) {
            log.warn("非法表名格式: {}", tableName);
            throw new IllegalArgumentException("表名格式不合法");
        }
    }

    /**
     * 确保白名单已加载
     */
    private synchronized void ensureWhitelistLoaded() {
        if (!whitelistLoaded) {
            log.info("开始加载表名白名单...");
            List<String> tableNames = tableMetaMapper.selectAllTableNames();
            tableNameWhitelist.addAll(tableNames);
            whitelistLoaded = true;
            log.info("表名白名单加载完成，共 {} 个表", tableNames.size());
        }
    }

    /**
     * 校验表名是否在白名单中
     */
    private void validateTableNameInWhitelist(String tableName) {
        if (!tableNameWhitelist.contains(tableName)) {
            log.warn("表不存在: {}", tableName);
            throw new IllegalArgumentException("表不存在: " + tableName);
        }
    }

    /**
     * 执行 COUNT 查询（带超时控制）
     */
    private long executeCountWithTimeout(String tableName) {
        long startTime = System.currentTimeMillis();

        try {
            long count = databaseCountMapper.countByTableName(tableName);
            long elapsed = System.currentTimeMillis() - startTime;

            if (elapsed > QUERY_TIMEOUT_MS) {
                log.warn("COUNT 查询耗时过长: table={}, elapsed={}ms", tableName, elapsed);
            } else {
                log.debug("COUNT 查询成功: table={}, count={}, elapsed={}ms", tableName, count, elapsed);
            }

            return count;
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - startTime;
            log.error("COUNT 查询失败: table={}, elapsed={}ms", tableName, elapsed, e);
            throw new RuntimeException("查询失败: " + e.getMessage(), e);
        }
    }
}
