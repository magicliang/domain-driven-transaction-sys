package com.magicliang.transaction.sys.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2022-06-04 13:54
 */
@Slf4j
@Aspect
@Component
public class ProfilingAspect {

    public ProfilingAspect() {
        log.info("ProfilingAspect");
    }

    @Around("methodsToBeProfiled()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch sw = new StopWatch(getClass().getSimpleName());
        try {
            sw.start(pjp.getSignature().getName());
            return pjp.proceed();
        } finally {
            sw.stop();
            log.info(sw.prettyPrint());
        }
    }

    // 这里面的 ((execution(public * com.controller.C1.*(..)) || (execution(public * com.controller.C2.*(..))) 是可以用 || 逻辑关联的
    @Pointcut("within(com.magicliang.transaction.sys..*)")
    public void methodsToBeProfiled() {
    }
}
