package com.octopus.base.intercepter;

import com.octopus.base.utils.MyThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <pre>
 * MDC(Mapped Diagnostic Context)는 로깅에서 사용되는 패턴으로, 로그 메시지에 특정 데이터를 매핑하여 출력하는데 사용
 * Spring AOP와 MDC를 함께 사용하여 각 호출되는 메서드에 대한 정보를 로그에 추가할 수 있습니다.
 *
 * 사용의 예)
 * 로그 출력 시 MDC의 데이터를 사용하도록 logback.xml 또는 log4j2.xml 설정 파일을 구성
 * <property name="LOG_PATTERN" value="%d{yy-MM-dd HH:mm:SSS} [%thread] %-5level %30logger.%method [ %line line ] [%X{ThreadId}] - %msg%n" />
 *
 * 위 설정에서 %X{ThreadId}는 MDC에서 className 키의 값을 로그 메시지에 추가하도록 합니다.
 * 따라서 각 메서드 호출 시 클래스 이름이 로그에 출력됩니다.
 * </pre>
 */
@Aspect
@Component
@Slf4j
public class TrackingAspect {

    @Before( "execution(* com.octopus.*.controller.*.*(..))" )
    public void loggingBefore( JoinPoint joinPoint ) {
        //String className = joinPoint.getTarget().getClass().getSimpleName();
        MDC.put( "ThreadId", "ThreadId-" + Thread.currentThread().getId() );
        MyThreadLocal.setContext( "startTime", System.currentTimeMillis() );

        MyThreadLocal.setTrackingLog(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName());

        log.debug( "Transaction started for method >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " );
        log.debug( "Target().getClass(): {}", joinPoint.getTarget().getClass() );
        log.debug( "Target().toString(): {}", joinPoint.getTarget().toString() );
        log.debug( "Signature().toShortString() :: {}", joinPoint.getSignature().toShortString() );
        log.debug( "Signature().toLongString() :: {}", joinPoint.getSignature().toLongString() );
        log.debug( "Signature().getName() :: {}", joinPoint.getSignature().getName() );
        log.debug( "Signature().getClass() :: {}", joinPoint.getSignature().getClass() );
        log.debug( "Signature().getDeclaringType() :: {}", joinPoint.getSignature().getDeclaringType() );
        log.debug( "Signature().getDeclaringTypeName() :: {}", joinPoint.getSignature().getDeclaringTypeName() );
        log.debug( "Signature().getModifiers() :: {}", joinPoint.getSignature().getModifiers() );
        log.debug( "Transaction started for method <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " );
    }

    @Around( "execution(* com.octopus.*.controller.*.*(..))" )
    public Object loggingControllerAround( ProceedingJoinPoint joinPoint ) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - (Long) MyThreadLocal.getContext( "startTime" );
            MyThreadLocal.setTrackingLog("실행시간 :: "+ duration + " ms");
            //log.info( "{}.{}({}) 실행 시간 : {} ms", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), Arrays.toString( joinPoint.getArgs() ), duration );

            StringBuffer trackingBuffer = new StringBuffer();
            trackingBuffer.append( "\n\n********************** My Tracking Logging **********************\n\n" );
            List<String> trackingList = MyThreadLocal.getTrackingList();
            for (String element : trackingList) {
                trackingBuffer.append(element + "\n");
            }

            log.debug(trackingBuffer.toString() + "\n");

            //log.info( "Signature().getName() :: {}", joinPoint.getSignature().getName() );
            //log.debug( "Transaction completed for method: {}", joinPoint.getSignature().toLongString() + " >> Duration: " + duration + " ms" );

            MyThreadLocal.clearContext();
            MDC.remove( "ThreadId" );
        }
    }

    @Around( "execution(* com.octopus.*.service.*.*(..))" )
    public Object loggingServiceAround( ProceedingJoinPoint joinPoint ) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            MyThreadLocal.setTrackingLog(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName());
            //log.info( "loggingServiceAround :: Signature().getName() :: {}", joinPoint.getSignature().getName() );
            //log.debug( "loggingServiceAround :: Signature().toLongString() :: {}", joinPoint.getSignature().toLongString() );
        }
    }

    @Around( "execution(* com.octopus.*.repository.*.*(..))" )
    public Object loggingRepositoryAround( ProceedingJoinPoint joinPoint ) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            MyThreadLocal.setTrackingLog(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName());
            //log.info( "loggingRepositoryAround :: Signature().getName() :: {}", joinPoint.getSignature().getName() );
            //log.debug( "loggingRepositoryAround :: Signature().toLongString() :: {}", joinPoint.getSignature().toLongString() );
        }
    }

}
