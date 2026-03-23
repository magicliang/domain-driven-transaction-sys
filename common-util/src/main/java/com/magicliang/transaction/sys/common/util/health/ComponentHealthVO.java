package com.magicliang.transaction.sys.common.util.health;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 组件健康状态值对象
 *
 * @author magicliang
 * date: 2026-03-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentHealthVO implements Serializable {

    /**
     * 组件健康状态：UP、DOWN、WARN
     */
    private String status;

    /**
     * 组件详细信息，如响应时间、错误信息等
     */
    private Map<String, Object> details;
}
