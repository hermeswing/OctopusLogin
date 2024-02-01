package com.octopus.base.intercepter;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 *     Spring MVC에서 제공하는 인터페이스로, 컨트롤러의 실행 전/후 및 완료 후에 특정 작업을 수행할 수 있습니다.
 *     주로 요청의 전처리와 응답의 후처리에 사용
 *     AppConfig 클래스, loggingInterceptor 클래스 등이 한 세트임.
 * </pre>
 */
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 요청이 컨트롤러에 도달하기 전에 호출되는 메서드
        String className = handler.getClass().getName();
        System.out.println("Pre Handle: " + className);
        return true; // true를 반환하면 컨트롤러 실행, false를 반환하면 실행 중단
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 컨트롤러의 처리가 끝난 후, 뷰가 렌더링되기 전에 호출되는 메서드
        String className = handler.getClass().getName();
        System.out.println("Post Handle: " + className);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 뷰의 렌더링이 완료된 후 호출되는 메서드
        String className = handler.getClass().getName();
        System.out.println("After Completion: " + className);
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