package com.magicliang.transaction.sys.aop.factory;

import org.springframework.aop.target.HotSwappableTargetSource;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 可热切换的目标源工厂
 *
 * @author magicliang
 *         <p>
 *         date: 2022-05-28 18:45
 */
public class HotSwappableTargetSourceFactory {

    /**
     * 把原始对象包装成 HotSwappableTargetSource
     * 这个 bean 本身也可以被 Spring 管理起来，也就是说 decorator 模式本身也可以在内部再做一次切换的，这点很重要
     * 这样生成的 source 会使用 target 接口掩盖原生接口，除非 factory 另外 addInterfaces
     *
     * @param sourceTarget 原始对象
     * @return HotSwappableTargetSource
     */
    public static HotSwappableTargetSource createHotSwappableTargetSource(Object sourceTarget) {
        return new HotSwappableTargetSource(sourceTarget);
    }
}
