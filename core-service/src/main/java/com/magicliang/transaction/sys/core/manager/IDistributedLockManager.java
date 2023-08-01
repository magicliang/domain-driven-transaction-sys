package com.magicliang.transaction.sys.core.manager;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 分布式锁底层依赖的管理器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 15:31
 */
public interface IDistributedLockManager {

    void init();

    void destroy();

    Lock getReentrantLock(String lockName);

    ReadWriteLock getReentrantReadWriteLock(String lockName);

    Lock getReentrantLock(String lockName, int expireTime);

    Lock getReentrantLock(String lockName, int expireTime, int retry);

    ReadWriteLock getReentrantReadWriteLock(String lockName, int expireTime);

    ReadWriteLock getReentrantReadWriteLock(String lockName, int expireTime, int retry);

    Lock getNewMultiLock(Lock[] locks);

    Lock getNewMultiLock(String[] locks);

    Lock getNewMultiLock(String[] locks, int expireTime);

    String switchEngine();

    String switchEngine(String engineName);
}
