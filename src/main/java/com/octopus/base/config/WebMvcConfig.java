package com.octopus.base.config;

import com.octopus.base.intercepter.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 *     AppConfig 클래스, loggingInterceptor 클래스 등이 한 세트임.
 * </pre>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors( InterceptorRegistry registry ) {
        // LoggingInterceptor를 등록하여 요청의 처리과정에서 거치는 클래스 목록을 수집
        registry.addInterceptor( loggingInterceptor() );
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
}
