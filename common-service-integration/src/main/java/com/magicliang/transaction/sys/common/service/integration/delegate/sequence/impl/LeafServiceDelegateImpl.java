package com.magicliang.transaction.sys.common.service.integration.delegate.sequence.impl;

import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.common.service.integration.delegate.sequence.ILeafServiceDelegate;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: id 生成器服务委托实现
 *
 * @author magicliang
 * <p>
 * date: 2021-12-30 17:45
 */
@Slf4j
@Component
public class LeafServiceDelegateImpl implements ILeafServiceDelegate {

    /**
     * 最大重试次数
     */
    private static final int INVOKE_RETRY_TIME = 5;

    /**
     * 超时服务
     */
    private static final String TIMEOUT = "timeout";

    /**
     * 使用leaf snowflake 模式获取id,超时将进行重试
     *
     * @param leafKey leaf key
     * @return id
     */
    @Override
    public Long nextSnowflakeId(String leafKey) {
        AssertUtils.assertNotBlank(leafKey, INVALID_PARAMETER_ERROR, "invalid leafKey: " + leafKey);
        List<Long> ids = getSnowflakeBatch(leafKey, 1);
        return ids.get(0);
    }

    /**
     * 使用leaf snowflake 批量模式获取id，超时将进行重试
     *
     * @param leafKey leafKey
     * @param size    批大小
     * @return id 列表
     */
    @Override
    public List<Long> getSnowflakeBatch(String leafKey, int size) {
        AssertUtils.assertNotBlank(leafKey, INVALID_PARAMETER_ERROR, "invalid leafKey: " + leafKey);
        AssertUtils.isTrue(size > 0, INVALID_PARAMETER_ERROR, "invalid size: " + size);

        int retryTime = 0;
        while (++retryTime <= INVOKE_RETRY_TIME) {
            try {
                // 生成 results
//                AssertUtils.assertNotEmpty(results, LEAF_ID_GENERATION_BIZ_ERROR, "leaf id生成异常, 结果为空");
//                List<Long> ids = new ArrayList<>(results.size());
//                for (Result result : results) {
//                    AssertUtils.assertEquals(Status.SUCCESS, result.getStatus(), LEAF_ID_GENERATION_BIZ_ERROR, "leaf id生成异常, 异常码:" + result.getId());
//                    ids.add(result.getId());
//                }
//                return ids;
            } catch (RuntimeException e) {
                throwTexception(retryTime, e);
            } catch (Exception e) {
                throw new BaseTransException(e, LEAF_ID_GENERATION_BIZ_ERROR, "leaf id 生成异常");
            }
        }
        throw new BaseTransException(DEFAULT_BIZ_ERROR, "leaf id生成异常");
    }

    /**
     * 抛出异常
     *
     * @param retryTime 已重试次数
     * @param e         捕获的异常
     */
    private void throwTexception(final int retryTime, final Exception e) {
        if (e.getMessage() != null) {
            // 1. 超时，继续执行
            if (e.getMessage().contains(TIMEOUT)) {
                return;
            }
            // 2. 因超时重试达到最大重试次数
            if ((retryTime >= INVOKE_RETRY_TIME)) {
                throw new BaseTransException(e, LEAF_ID_GENERATION_MAX_RETRY_ERROR,
                        LEAF_ID_GENERATION_MAX_RETRY_ERROR.getErrorMsg() + " 错误信息:" + e.getMessage());
            }
            // 3. 其他 te 异常
            throw new BaseTransException(e, LEAF_ID_GENERATION_TE_ERROR,
                    LEAF_ID_GENERATION_TE_ERROR.getErrorMsg() + " 错误信息:" + e.getMessage());
        }
        throw new BaseTransException(e, LEAF_ID_GENERATION_TE_ERROR);
    }
}
