package com.octopus.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.octopus.utils.JwtFilter;
import com.octopus.utils.TokenProvider;

/**
 * <pre>
 * TokenProvider, JwtFilter를 SecurityConfig에 적용할 떄 사용
 * SecurityConfigurerAdapter를 extends
 * 출처 : https://velog.io/@gale4739/Spring-Security-%EC%A0%81%EC%9A%A9
 * </pre>
 * 
 * @author jypark
 */
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    
    // TokenProvider를 주입
    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    @Override
    public void configure(HttpSecurity http) {
        
        System.out.println("configure");
        
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}