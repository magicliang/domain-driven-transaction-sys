package com.magicliang.transaction.sys.core.factory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 抽象名值对工厂 bean
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 13:33
 */
@Slf4j
public abstract class AbstractNameValue<T> implements FactoryBean<T> {

    /**
     * bean 名称
     */
    @Setter
    @Getter
    private String name;

    /**
     * bean 代表的缺省值
     */
    @Setter
    private T value;

    /**
     * Return an instance (possibly shared or independent) of the object
     * managed by this factory.
     * <p>As with a {@link BeanFactory}, this allows support for both the
     * Singleton and Prototype design pattern.
     * <p>If this FactoryBean is not fully initialized yet at the time of
     * the call (for example because it is involved in a circular reference),
     * throw a corresponding {@link FactoryBeanNotInitializedException}.
     * <p>As of Spring 2.0, FactoryBeans are allowed to return {@code null}
     * objects. The factory will consider this as normal value to be used; it
     * will not throw a FactoryBeanNotInitializedException in this case anymore.
     * FactoryBean implementations are encouraged to throw
     * FactoryBeanNotInitializedException themselves now, as appropriate.
     *
     * @return an instance of the bean (can be {@code null})
     * @throws Exception in case of creation errors
     * @see FactoryBeanNotInitializedException
     */
    @Override
    public T getObject() throws Exception {
        return getValue();
    }

    /**
     * 这个工厂 bean 注定是单例
     *
     * @return whether the exposed object is a singleton
     * @see #getObject()
     * @see SmartFactoryBean#isPrototype()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 转换原始值
     *
     * @param originalValue 原始值
     * @return 转换值
     */
    protected abstract T transform(T originalValue);

    /**
     * 获取预填充的值
     *
     * @return 预填充的值
     */
    private T getValue() {
        if (null != value) {
            return this.transform(value);
        }
        return null;
    }
}
