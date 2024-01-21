package com.octopus.base.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.octopus.base.WebConst;
import com.octopus.base.security.provider.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * JWT를 위한 커스텀 필터 
 * 출처 : https://velog.io/@u-nij/JWT-JWT-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-3-JwtAuthenticationFilter-JwtAccessDeniedHandler-JwtAuthenticationEntryPoint
 * 출처 : https://velog.io/@gale4739/Spring-Security-%EC%A0%81%EC%9A%A9
 * </pre>
 * 
 * @author jypark
 */
@Slf4j
// public class JwtFilter extends GenericFilterBean {
public class JwtFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider tokenProvider;
    
    public JwtFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    // 실제 필터링 로직 작성
    // doFilter : 토큰의 인증 정보를 SecurityContext에 저장
    @Override
    // public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    // ServletException {
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        
        // resolveToken을 통해 토큰을 받아옴
        String accessToken = resolveToken((HttpServletRequest) request);
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String             requestURI         = httpServletRequest.getRequestURI();
        
        log.debug("jwt : {}", accessToken);
        log.debug("requestURI : {}", requestURI);
        
        // 토큰 유효성 검증 후 정상이면 SecurityContext에 저장
        if (StringUtils.hasText(accessToken) && tokenProvider.validateAccessToken(accessToken)) {
            
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }
        
        // 생성한 필터 실행
        // chain.doFilter(httpServletRequest, response);
        filterChain.doFilter(request, response);
        
        log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
    
    // Request Header에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(WebConst.AUTHORIZATION_HEADER);
        
        // if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            
            log.debug("[resolveToken] token : {}", bearerToken);
            
            return bearerToken.substring(7);
        }
        
        return null;
    }
}