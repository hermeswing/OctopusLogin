package com.octopus.base.intercepter;

import com.octopus.base.WebConst;
import com.octopus.base.utils.MyThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

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
@Slf4j
@Aspect
@Component
public class TrackingAspect {

    @Before( "execution(* com.octopus.*.controller.*.*(..))" )
    public void loggingBefore( JoinPoint joinPoint ) {
        //String className = joinPoint.getTarget().getClass().getSimpleName();
        String threadId = "ThreadId-" + Thread.currentThread().getId();
        MDC.put( WebConst.THREAD_ID, threadId );

        // ThreadLocal 을 초기화 한다.
        MyThreadLocal.clearContext();

        // 호출된 메서드의 클래스와 메서드 이름을 가져오기
        //String className = joinPoint.getSignature().getDeclaringTypeName();
        //String methodName = joinPoint.getSignature().getName();

        MyThreadLocal.setContext( WebConst.THREAD_ID, threadId );
        MyThreadLocal.setContext( WebConst.START_TIME, System.currentTimeMillis() );

        //MyThreadLocal.setTrackingLog( joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() );
        MyThreadLocal.setTrackingLog( "[Controller Call] " + joinPoint.getSignature().toLongString() );

        //log.debug( "Transaction started for method >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " );
        //log.debug( "Target().getClass(): {}", joinPoint.getTarget().getClass() );
        //log.debug( "Target().toString(): {}", joinPoint.getTarget().toString() );
        //log.debug( "Signature().toShortString() :: {}", joinPoint.getSignature().toShortString() );
        //log.debug( "Signature().toLongString() :: {}", joinPoint.getSignature().toLongString() );
        //log.debug( "Signature().getName() :: {}", joinPoint.getSignature().getName() );
        //log.debug( "Signature().getClass() :: {}", joinPoint.getSignature().getClass() );
        //log.debug( "Signature().getDeclaringType() :: {}", joinPoint.getSignature().getDeclaringType() );
        //log.debug( "Signature().getDeclaringTypeName() :: {}", joinPoint.getSignature().getDeclaringTypeName() );
        //log.debug( "Signature().getModifiers() :: {}", joinPoint.getSignature().getModifiers() );
        //log.debug( "Transaction started for method <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " );
    }

    @Around( "execution(* com.octopus.*.controller.*.*(..))" )
    public Object loggingControllerAround( ProceedingJoinPoint joinPoint ) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            MyThreadLocal.setTrackingLog( "[Controller Release] " + joinPoint.getSignature().toLongString() );

            long duration = System.currentTimeMillis() - (Long) MyThreadLocal.getContext( WebConst.START_TIME );
            MyThreadLocal.setTrackingLog( "실행시간 :: " + duration + " ms" );
            //log.info( "{}.{}({}) 실행 시간 : {} ms", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), Arrays.toString( joinPoint.getArgs() ), duration );

            MyThreadLocal.printStackLog();

            //log.info( "Signature().getName() :: {}", joinPoint.getSignature().getName() );
            //log.debug( "Transaction completed for method: {}", joinPoint.getSignature().toLongString() + " >> Duration: " + duration + " ms" );

            //MyThreadLocal.clearContext();
            MDC.remove( WebConst.THREAD_ID );
        }
    }

    @Before( "execution(* com.octopus.*.service.*.*(..))" )
    public void loggingServiceBefore( JoinPoint joinPoint ) {

        // 호출된 메서드가 속한 클래스를 가져오기
        Class<?> declaringType = joinPoint.getSignature().getDeclaringType();
        String calledClassName = declaringType.getName();

        // 호출된 메서드를 호출한 객체(또는 프락시 객체)를 가져오기
        Object callerObject = joinPoint.getThis();

        // 호출한 객체의 클래스를 가져오기
        Class<?> callerClass = callerObject.getClass();
        String callerClassName = callerClass.getName();

        // 호출된 메서드의 클래스와 메서드 이름을 가져오기
        String methodName = joinPoint.getSignature().getName();

        MyThreadLocal.setTrackingLog( "[Before Service Call] " + callerClassName + "." + methodName);

        MyThreadLocal.setTrackingLog( "[Service Call] " + joinPoint.getSignature().toLongString() );
    }

    @After( "execution(* com.octopus.*.service.*.*(..))" )
    public void loggingServiceAfter( JoinPoint joinPoint ) {
        //MyThreadLocal.setTrackingLog( joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() );
        MyThreadLocal.setTrackingLog( "[Service Release] " + joinPoint.getSignature().toLongString() );
    }

    @Before( "execution(* com.octopus.*.repository.*.*(..))" )
    public void loggingRepositoryBefore( JoinPoint joinPoint ) {
        MyThreadLocal.setTrackingLog( "[Repository Call] " + joinPoint.getSignature().toLongString() );
    }

    @After( "execution(* com.octopus.*.repository.*.*(..))" )
    public void loggingRepositoryAfter( JoinPoint joinPoint ) {
        //MyThreadLocal.setTrackingLog( joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() );
        MyThreadLocal.setTrackingLog( "[Repository Release] " + joinPoint.getSignature().toLongString() );
    }

    // 특정 클래스를 대상으로 하는 Pointcut 정의
    @Pointcut( "execution(* com.octopus.base.security.provider.JwtTokenProvider.*(..))" )
    private void baseAspectPointcut() {
    }

    @Before( "baseAspectPointcut()" )
    public void baseAspectPointcutBefore( JoinPoint joinPoint ) {
        MyThreadLocal.setTrackingLog( "[JwtTokenProvider] " + joinPoint.getSignature().toLongString() );
    }

    @Pointcut( "execution(* com.octopus.base.security.filter.JwtFilter.*(..))" )
    private void jwtFilterPointcut() {
    }

    @Before( "jwtFilterPointcut()" )
    public void jwtFilterPointcutBefore( JoinPoint joinPoint ) {
        MyThreadLocal.setTrackingLog( "[JwtFilter] " + joinPoint.getSignature().toLongString() );
    }

}
