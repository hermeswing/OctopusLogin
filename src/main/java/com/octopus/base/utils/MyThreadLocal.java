package com.octopus.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class MyThreadLocal {
    private static final ThreadLocal<HashMap<String, Object>> threadLocal = new ThreadLocal<>();

    public static Object getContext( String key ) {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = getThreadData();
        if( threadData == null ) {
            return null;
        } else {
            return threadData.get( key );
        }
    }

    public static void setTrackingLog( String path ) {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = getThreadData();
        //log.debug( "threadData :: {}", threadData );
        if( threadData == null ) {
            threadData = new HashMap<>();
        }
        List<String> trackingLog = (List<String>) threadData.get( "trackingLog" );
        if( trackingLog == null ) {
            trackingLog = new ArrayList<>();
        }

        trackingLog.add( path );
        // 데이터 설정
        threadData.put( "trackingLog", trackingLog );
        threadLocal.set( threadData );
    }

    public static void setContext( String key, Object value ) {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = getThreadData();
        log.debug( "threadData :: {}", threadData );
        if( threadData == null ) {
            threadData = new HashMap<>();
        }

        // 데이터 설정
        threadData.put( key, value );
        threadLocal.set( threadData );
    }

    public static HashMap<String, Object> getThreadData() {
        return threadLocal.get();
    }

    public static List<String> getTrackingList() {
        // 현재 스레드의 데이터를 가져오거나 생성
        HashMap<String, Object> threadData = getThreadData();
        //log.debug( "threadData :: {}", threadData );
        if( threadData == null ) {
            return null;
        } else {
            return (List<String>)threadData.get("trackingLog");
        }
    }

    public static void clearContext() {
        threadLocal.remove();
    }
}
