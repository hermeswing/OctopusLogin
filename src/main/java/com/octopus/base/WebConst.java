package com.octopus.base;

public interface WebConst {

    String THREAD_ID = "ThreadId";
    String START_TIME = "startTime";
    String TRACKING_LOGGER = "trackingLog";

    String AUTHORIZATION_HEADER = "Authorization";

    String AUTHORITIES_KEY = "role";
    
    String USER_ID = "userId";

    String EMAIL_KEY = "email";

    String URL = "https://localhost:7000";
    
    /** ---- URLs ---- * */
    String HOME_URL = "/";
    
    String ERROR_URL = "/error";
    
    String FORBIDDEN_URL = "/forbidden";
    
    String LOGIN_URL = "/login";
    
    String LOGIN_PROCESSING_URL = "/authenticate";
    
    String LOGIN_SUCCESS_URL = "/loginSuccess";
    
    String LOGIN_FAILURE_URL = "/loginFailure";
    
    String LOGIN_TIMEOUT_URL = "/loginTimeout";
    
    String RESET_PASSWORD_URL = "/resetPassword";
    
    String CHANGE_PASSWORD_URL = "/changePassword";
    
    String LOGOUT_URL = "/logout";
    
    String LOGOUT_SUCCESS_URL = "/logoutSuccess";
    
    String WEBJARS_URL = "/webjars/**";
    
    String STATIC_RESOURCES_URL = "/static/**";
    
    String API_BASE_URL = "/api/**";
}
