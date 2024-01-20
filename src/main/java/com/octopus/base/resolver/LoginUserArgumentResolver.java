package com.octopus.base.resolver;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.octopus.base.annotation.LoginUser;
import com.octopus.login.dto.PrincipalDetails;
import com.octopus.login.service.PrincipalDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * @LoginUser Annotation 을 사용할 수 있다.
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    
    private final HttpSession             session;
    private final PrincipalDetailsService userService;
    
    /**
     * Parameter가 @LoginUser Annotation이고, UserSessionDto Type이면..
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass           = PrincipalDetails.class.equals(parameter.getParameterType());
        
        if (isLoginUserAnnotation && isUserClass) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 사용자 정보를 Return 한다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        
        // Spring Security
        // final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if(authentication == null){
        // throw new RuntimeException;
        // }
        // UserSessionDto userDto = (UserSessionDto) authentication.getPrincipal();
        
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass           = PrincipalDetails.class.equals(parameter.getParameterType());
        
        if (isLoginUserAnnotation && isUserClass) {
            // TODO Session 객체가 없음. Redis 에서 사용자의 정보를 가져와야 할 지도..
            // TODO 사용자 정보를 Return 한다.
            // UserSessionDto userDto = (UserSessionDto) session.getAttribute("user");
            PrincipalDetails userDetail = userService.loadUserByUsername("admin");
            
            log.debug("userDetail :: {}", userDetail);
            
            return userDetail;
        } else {
            return null;
        }
        
    }
}