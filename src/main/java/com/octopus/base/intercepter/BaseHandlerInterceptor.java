package com.octopus.base.intercepter;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public abstract class BaseHandlerInterceptor implements HandlerInterceptor {

    /**
     * <pre>
     *     컨트롤러의 메서드에 매핑된 특정 URI가 호출됐을 때 실행되는 메서드로, 컨트롤러를 경유(접근)하기 직전에 실행되는 메서드입니다.
     *     우리는 사용자가 어떠한 기능을 수행했는지 파악하기 위해, 해당 메서드(기능)와 매핑된 URI 정보가 로그로 출력되도록 처리합니다.
     * </pre>
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
            throws Exception {
        // Controller 진입 전
        log.debug( "===============================================" );
        log.debug( "==================== BEGIN ====================" );
        log.debug( "Request URI ===> " + request.getRequestURI() );
        return HandlerInterceptor.super.preHandle( request, response, handler );
    }

    /**
     * <pre>
     *     컨트롤러를 경유(접근) 한 후, 즉 화면(View)으로 결과를 전달하기 전에 실행되는 메서드입니다.
     *     preHandle( )과는 반대로 요청(Request)의 끝을 알리는 로그가 콘솔에 출력되도록 처리합니다.
     * </pre>
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler,
                            ModelAndView modelAndView ) throws Exception {
        // Controller 처리 후
        log.debug( "==================== END ======================" );
        log.debug( "===============================================" );
        HandlerInterceptor.super.postHandle( request, response, handler, modelAndView );
    }

    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
            throws Exception {
        // 처리완료 후
    }

    /**
     * Rest Controller인지 여부를 나타내는 값을 반환
     *
     * @param handler
     * @return
     */
    // protected boolean isRestController(Object handler) {
    // val bean = getBean(handler, AbstractRestController.class);
    //
    // if (bean instanceof AbstractRestController) {
    // return true;
    // }
    //
    // return false;
    // }

    /**
     * 인수 개체가 지정 클래스인지 여부를 나타냅니다.
     *
     * @param obj
     * @param clazz
     * @return
     */
    protected boolean isInstanceOf( Object obj, Class<?> clazz ) {

        if( clazz.isAssignableFrom( obj.getClass() ) ) {
            return true;
        }

        return false;
    }

    /**
     * Handler의 Bean을 반환합니다.
     *
     * @param handler
     * @return
     */
    @SuppressWarnings( "unchecked" )
    protected <T> T getBean( Object handler, Class<T> clazz ) {

        if( handler != null && handler instanceof HandlerMethod ) {
            val hm = ( (HandlerMethod) handler ).getBean();

            if( clazz.isAssignableFrom( hm.getClass() ) ) {
                return (T) hm;
            }

            return null;
        }

        return null;
    }
}