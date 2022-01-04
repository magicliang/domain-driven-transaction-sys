package com.magicliang.transaction.sys.common.type;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 可以被领域识别的接口
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 13:59
 */
public interface IIdentifiableType<E> {

    /**
     * 标识自己的类型。
     *
     * @return 类型
     */
    E identify();
}
