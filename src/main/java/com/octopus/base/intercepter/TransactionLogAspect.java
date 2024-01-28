package com.octopus.base.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TransactionLogAspect {

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Before( "execution(* com.octopus.*.controller.*.*(..))" )
    public void logBeforeTransactionStart( JoinPoint joinPoint ) {
        startTime.set( System.currentTimeMillis() );
        System.out.println( "Transaction started for method: " + joinPoint.getSignature().toLongString() );
    }

    @Around( "execution(* com.octopus.*.controller.*.*(..))" )
    public Object logBeforeTransactionStart( ProceedingJoinPoint joinPoint ) throws Throwable {
        log.info( "Around before: " + joinPoint.getSignature().getName() );
        Object result = joinPoint.proceed();
        log.info( "Around after: " + joinPoint.getSignature().getName() );

        long duration = System.currentTimeMillis() - startTime.get();
        System.out.println( "Transaction completed for method: " + joinPoint.getSignature().toShortString() +
                " | Duration: " + duration + "ms" );

        startTime.remove();

        return result;
    }

    @After( "execution(* com.octopus.*.repository.*.*(..))" )
    public void logAfterTransactionCompletion( JoinPoint joinPoint ) {
        System.out.println( "Transaction completed for method: " + joinPoint.getSignature().toLongString() );
    }
}
