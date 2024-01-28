package com.octopus.base.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Aspect
@Component
@Slf4j
public class TransactionLogAspect {

    private static final ThreadLocal<HashMap<String, Object>> logStore = new ThreadLocal<>();

    @Before( "execution(* com.octopus.*.controller.*.*(..))" )
    public void logBeforeTransactionStart( JoinPoint joinPoint ) {
        setData( "startTime", System.currentTimeMillis() );
        System.out.println( "Transaction started for method: " + joinPoint.getSignature().toLongString() );
    }

    @Around( "execution(* com.octopus.*.controller.*.*(..))" )
    public Object logBeforeTransactionStart( ProceedingJoinPoint joinPoint ) throws Throwable {
        log.info( "Around before: " + joinPoint.getSignature().getName() );
        Object result = joinPoint.proceed();
        log.info( "Around after: " + joinPoint.getSignature().getName() );

        log.debug( "startTime :: {}", getData( "startTime" ) );
        long duration = System.currentTimeMillis() - (Long) getData( "startTime" );
        System.out.println( "Transaction completed for method: " + joinPoint.getSignature().toShortString() +
                " | Duration: " + duration + "ms" );

        logStore.remove();

        return result;
    }

    @After( "execution(* com.octopus.*.repository.*.*(..))" )
    public void logAfterTransactionCompletion( JoinPoint joinPoint ) {
        log.debug( "Transaction completed for method: {}" + joinPoint.getSignature().toLongString() );
    }

    private void setData( String key, Object value ) {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = logStore.get();
        log.debug( "threadData :: {}", threadData );
        if( threadData == null ) {
            threadData = new HashMap<>();
        }

        // 데이터 설정
        threadData.put( key, value );
        logStore.set( threadData );
    }

    private Object getData( String key ) {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = logStore.get();
        if( threadData == null ) {
            return null;
        } else {
            return threadData.get( key );
        }
    }
}
