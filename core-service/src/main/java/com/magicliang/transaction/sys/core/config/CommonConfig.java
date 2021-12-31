package com.magicliang.transaction.sys.core.config;

import com.magicliang.transaction.sys.common.enums.TransEnvEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通用配置
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 11:34
 */
@Data
@ConfigurationProperties(prefix = "common")
@Component
public class CommonConfig {

    /**
     * 环境配置，
     *
     * @see TransEnvEnum
     */
    private String env;

    /**
     * 交易分布式锁的默认时长，单位为秒
     */
    private Integer lockExpiration;

    /**
     * 是否进入挡板测试模式
     * 基础类型被 Spring 注入，改天把名字改回 is 模式
     */
    private Boolean mockMode;

    /**
     * 服务端口
     */
    private Integer servicePort;
}
