package com.magicliang.transaction.sys.core.factory;

import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 字符串值工厂 bean
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 13:38
 */
public class StringValue extends AbstractNameValue<String> {

    /**
     * 转换原始值
     *
     * @param originalValue 原始值
     * @return 转换值
     */
    @Override
    protected String transform(String originalValue) {
        return StringUtils.isBlank(originalValue) ? "" : originalValue;
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }
}
