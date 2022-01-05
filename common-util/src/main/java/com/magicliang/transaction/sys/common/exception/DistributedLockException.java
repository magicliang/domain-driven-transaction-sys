package com.magicliang.transaction.sys.common.exception;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 分布式锁异常
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 15:30
 */
public class DistributedLockException extends BizException {

    /**
     * 构造器
     *
     * @param errorMsg 错误信息
     */
    public DistributedLockException(String errorMsg) {
        super(errorMsg);
    }

    /**
     * 构造器
     *
     * @param throwable 根错误
     */
    public DistributedLockException(Throwable throwable) {
        super(throwable);
    }
}
