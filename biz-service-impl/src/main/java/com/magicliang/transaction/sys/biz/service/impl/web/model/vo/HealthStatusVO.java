package com.magicliang.transaction.sys.biz.service.impl.web.model.vo;

import com.magicliang.transaction.sys.common.util.health.ComponentHealthVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 健康状态响应值对象（已废弃，使用 common-util 中的类）
 *
 * @author magicliang
 * date: 2026-03-23
 * @deprecated 使用 {@link com.magicliang.transaction.sys.common.util.health.HealthStatusVO}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class HealthStatusVO implements Serializable {

    /**
     * 整体健康状态：UP、DOWN、WARN
     */
    private String status;

    /**
     * 检查时间戳（ISO 8601 格式）
     */
    private String timestamp;

    /**
     * 各组件健康状态详情，key 为组件名称，value 为组件健康信息
     */
    private Map<String, ComponentHealthVO> components;
}
