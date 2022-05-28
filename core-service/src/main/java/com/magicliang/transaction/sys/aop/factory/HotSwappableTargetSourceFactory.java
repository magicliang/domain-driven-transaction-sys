package com.magicliang.transaction.sys.aop.factory;

import org.springframework.aop.target.HotSwappableTargetSource;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 可热切换的目标源工厂
 *
 * @author magicliang
 * <p>
 * date: 2022-05-28 18:45
 */
public class HotSwappableTargetSourceFactory {

    /**
     * 把原始对象包装成 HotSwappableTargetSource
     *
     * @param sourceTarget 原始对象
     * @return HotSwappableTargetSource
     */
    public static HotSwappableTargetSource createHotSwappableTargetSource(Object sourceTarget) {
        return new HotSwappableTargetSource(sourceTarget);
    }
}
