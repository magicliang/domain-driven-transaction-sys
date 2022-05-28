package com.magicliang.transaction.sys.aop.generator.impl;

import com.magicliang.transaction.sys.aop.generator.IntegerGenerator;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 整数生成器实现
 *
 * @author magicliang
 * <p>
 * date: 2022-05-28 20:13
 */
@Service
public class IntegerGeneratorImpl implements IntegerGenerator {

    /**
     * 生成整数
     *
     * @return
     */
    @Override
    public int generate() {
        return 0;
    }
}
