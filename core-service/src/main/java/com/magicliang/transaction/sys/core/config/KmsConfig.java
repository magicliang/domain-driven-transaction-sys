package com.magicliang.transaction.sys.core.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通过 kms 系统产生的配置
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 12:02
 */
@Data
@Component
public class KmsConfig {

    /**
     * 私钥示例
     */
    @Resource(name = "privateKey")
    private String privateKey;
}
