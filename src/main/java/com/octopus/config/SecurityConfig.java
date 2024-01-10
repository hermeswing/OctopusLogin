package com.octopus.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.octopus.base.security.JwtAccessDeniedHandler;
import com.octopus.base.security.JwtAuthenticationEntryPoint;
import com.octopus.utils.TokenProvider;

/**
 * <pre>
 * TokenProvider, JwtFilter를 SecurityConfig에 적용할 떄 사용
 * SecurityConfigurerAdapter를 extends
 * 출처 : https://velog.io/@gale4739/Spring-Security-%EC%A0%81%EC%9A%A9
 * 출처: https://velog.io/@soyeon207/JWT-%EC%8B%A4%EC%8A%B5
 * https://github.com/soyeon207/blog_example/blob/master/jwt-security-server/src/main/java/velog/soyeon/jwt/config/JwtAuthenticationFilter.java
 * </pre>
 * 
 * @author jongyoung.park
 */
@Configuration
@EnableWebSecurity // Spring Security 활성화
@EnableMethodSecurity // @PreAuthorize 어노테이션 메소드 단위로 추가하기 위해 적용 (default : true)
public class SecurityConfig {
    
    private final TokenProvider               tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler      jwtAccessDeniedHandler;
    
    // TokenProvider,JwtAuthenticationEntryPoint,JwtAccessDeniedHandler 의존성 주입
    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider               = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler      = jwtAccessDeniedHandler;
    }
    
    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
                // .requestMatchers("/resources/**");
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
                // 토큰을 사용하기 때문에 csrf 설정 disable
                .csrf().disable()
                
                // 예외 처리 시 직접 만들었던 클래스 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                
                // 세션 사용하지 않기 때문에 세션 설정 STATELESS
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                
                // 토큰이 없는 상태에서 요청이 들어오는 API들은 permitAll
                .and()
                .authorizeHttpRequests()
                // .requestMatchers("/login").permitAll()
                .mvcMatchers("/login").permitAll()
                .anyRequest().authenticated()
                
                // JwtFilter를 addFilterBefore로 등록했던 jwtSecurityConfig 클래스 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
        
        return http.build();
    }
}