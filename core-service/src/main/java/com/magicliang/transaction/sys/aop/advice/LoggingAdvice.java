package com.magicliang.transaction.sys.aop.advice;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.AfterReturningAdvice;

/**
 * project name: spring-experiments
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2022-05-28 16:20
 */
@Slf4j
public class LoggingAdvice implements AfterReturningAdvice {

    /**
     * Callback after a given method successfully returned.
     *
     * @param returnValue the value returned by the method, if any
     * @param method method being invoked
     * @param args arguments to the method
     * @param target target of the method invocation. May be {@code null}.
     * @throws Throwable if this object wishes to abort the call.
     *         Any exception thrown will be returned to the caller if it's
     *         allowed by the method signature. Otherwise the exception
     *         will be wrapped as a runtime exception.
     */
    @Override
    public void afterReturning(final Object returnValue, final Method method, final Object[] args, final Object target)
            throws Throwable {
        log.info("returnValue: {}", returnValue);
    }
}
