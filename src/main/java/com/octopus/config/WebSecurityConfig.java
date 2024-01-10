package com.octopus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 출처: https://velog.io/@soyeon207/JWT-%EC%8B%A4%EC%8A%B5
 * https://github.com/soyeon207/blog_example/blob/master/jwt-security-server/src/main/java/velog/soyeon/jwt/config/JwtAuthenticationFilter.java
 * 
 * @author jongyoung.park
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
    
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                // .antMatchers("/h2-console/**") // h2-console
                .antMatchers("//api/login") // 로그인 api
        ;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 스프링 시큐리티가 세션 쿠키 방식으로 동작하지 않도록 설정
        ;
        // .and()
        // .addFilterBefore(new JwtAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = bCryptPasswordEncoder();
        String                encodedPassword = passwordEncoder.encode("1111");
        
        log.debug("encodedPassword :: {}", encodedPassword);
        
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder.encode("1111"))
                .roles("ADMIN");
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}