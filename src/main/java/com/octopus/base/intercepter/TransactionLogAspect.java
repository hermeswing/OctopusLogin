package com.octopus.base.intercepter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLogAspect {

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Before( "execution(* com.octopus.*.controller.*.*(..))" )
    public void logBeforeTransactionStart( JoinPoint joinPoint ) {
        startTime.set( System.currentTimeMillis() );
        System.out.println( "Transaction started for method: " + joinPoint.getSignature().toShortString() );
    }

    @After( "execution(* com.octopus.*.repository.*.*(..))" )
    public void logAfterTransactionCompletion( JoinPoint joinPoint ) {
        long duration = System.currentTimeMillis() - startTime.get();
        System.out.println( "Transaction completed for method: " + joinPoint.getSignature().toShortString() +
                " | Duration: " + duration + "ms" );
        startTime.remove();
    }
}
