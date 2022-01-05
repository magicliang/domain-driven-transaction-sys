package com.magicliang.transaction.sys.core.config;

import com.magicliang.transaction.sys.common.util.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 动态配置类
 * 待引入类似 apollo 或者 spring config 之类的动态配置实现
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 11:17
 */
@Component
public class DynamicConfig {

    /**
     * 每次查询未支付订单的批次大小
     */
    public volatile static int UNPAID_ORDER_QUERY_BATCH_SIZE = 500;

    /**
     * 任务优先级，结构是 json。key 是业务标识码，value 是任务优先级，取值范围是线程优先级，无配置的业务标识码的线程优先级是 Thread.NORM_PRIORITY。
     */
    public volatile static String TASK_PRIORITY = "";

    /**
     * 获取任务优先级
     *
     * @return 任务优先级配置列表，key 是业务标识码，value 是任务优先级，取值范围是线程优先级，无配置的业务标识码的线程优先级是 Thread.NORM_PRIORITY。
     */
    public static Map<String, Integer> getTaskPriorities() {
        Map<String, Integer> result = JsonUtils.toMapObject(TASK_PRIORITY, Integer.class);
        if (null == result) {
            result = Collections.emptyMap();
        }
        return result;
    }
}
