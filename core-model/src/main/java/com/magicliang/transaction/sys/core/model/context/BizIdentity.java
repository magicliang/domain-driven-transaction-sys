package com.magicliang.transaction.sys.core.model.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 业务身份
 * <p>
 * 如果产生业务特殊的入参，放到派生类里
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-06 21:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizIdentity {

    /**
     * 业务代码
     */
    private String bizCode;
}
