package com.octopus.base.utils;

import com.octopus.base.WebConst;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class MyThreadLocal {
    private static final ThreadLocal<HashMap<String, Object>> threadLocal = ThreadLocal.withInitial( HashMap::new );

    private MyThreadLocal() {
        // private 생성자로 외부에서 직접 인스턴스 생성을 방지
    }

    public static MyThreadLocal getInstance() {
        return Holder.INSTANCE;
    }

    public static void setContext( String key, Object value ) {
        // 데이터 설정
        getInstance().setAttribute( key, value );
    }

    public static Object getContext( String key ) {
        return getInstance().getAttribute( key );
    }

    public static void setTrackingLog( String path ) {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = threadLocal.get();

        List<String> trackingLog = (List<String>) threadData.get( WebConst.TRACKING_LOGGER );
        if( trackingLog == null ) {
            trackingLog = new ArrayList<>();
        }

        trackingLog.add( path );
        // 데이터 설정
        threadData.put( WebConst.TRACKING_LOGGER, trackingLog );
        //threadLocal.set( threadData );
    }

    public static void setDevTrackingLog( String logger ) {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = threadLocal.get();

        List<String> trackingLog = (List<String>) threadData.get( WebConst.TRACKING_LOGGER );
        if( trackingLog == null ) {
            trackingLog = new ArrayList<>();
        }

        trackingLog.add( "[개발자 로그] >> " + logger );
        // 데이터 설정
        threadData.put( WebConst.TRACKING_LOGGER, trackingLog );
        //threadLocal.set( threadData );
    }

    public static List<String> getTrackingList() {
        return (List<String>) threadLocal.get().get( WebConst.TRACKING_LOGGER );
    }

    public static void clearContext() {
        log.info( "ThreadLocal 이 삭제되었습니다." );
        threadLocal.remove();
    }

    public static void printStackLog() {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append( "\n\n********************** " + getContext( WebConst.THREAD_ID ) + " Tracking Logging **********************\n\n" );
        List<String> trackingList = MyThreadLocal.getTrackingList();
        for( String element : trackingList ) {
            logBuilder.append( element + "\n" );
        }

        log.debug( logBuilder.toString() + "\n" );
    }

    private void setAttribute( String key, Object value ) {
        threadLocal.get().put( key, value );
    }

    private Object getAttribute( String key ) {
        return threadLocal.get().get( key );
    }

    private void removeAttribute( String key ) {
        threadLocal.get().remove( key );
    }

    // 실제 싱글톤 인스턴스를 보유하는 정적 내부 클래스
    private static class Holder {
        private static final MyThreadLocal INSTANCE = new MyThreadLocal();
    }
}
