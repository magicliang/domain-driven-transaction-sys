package com.magicliang.transaction.sys.common.service.integration.delegate.sequence;

import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: id 生成器服务委托接口
 *
 * @author magicliang
 * <p>
 * date: 2021-12-30 17:43
 */
public interface ILeafServiceDelegate {

    /**
     * 获取下一个雪花 id，超时将进行重试
     *
     * @param leafKey leaf key
     * @return 下一个雪花 id
     */
    Long nextSnowflakeId(String leafKey);

    /**
     * 使用leaf snowflake 批量模式获取id，超时将进行重试
     *
     * @param leafKey leafKey
     * @param size    批大小
     * @return id 列表
     */
    List<Long> getSnowflakeBatch(String leafKey, int size);
}
