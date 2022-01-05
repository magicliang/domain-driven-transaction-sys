package com.magicliang.transaction.sys.common.service.integration.constant;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 集成模块常量
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 12:21
 */
public class IntegrationConstant {

    /**
     * UTF8编码格式，其实不需要这个枚举
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 本应用的 leaf key
     */
    public static final String LEAF_KEY = "financial.trans.payorderno";

    /**
     * 私有构造器
     */
    private IntegrationConstant() {
        throw new UnsupportedOperationException();
    }

    /**
     * Appkey 常量集合
     */
    public static class AppKeyConstant {

        /**
         * 私有构造器，偶尔为之
         */
        private AppKeyConstant() {
            throw new UnsupportedOperationException();
        }

    }
}
