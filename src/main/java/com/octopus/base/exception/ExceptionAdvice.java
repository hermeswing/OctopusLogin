package com.octopus.base.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.octopus.base.model.CommonResult;
import com.octopus.base.service.ResponseManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    
    private final ResponseManager       responseService;
    private final MessageSourceAccessor messageSourceAccessor;
    
    /**
     * @Valid 에 의해 발생되는 Exception 메시지 처리
     * @param e
     * @return
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult internalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        
        log.debug("e.getBindingResult() :: {}", e.getMessage());
        //log.debug("e.getBindingResult() :: {}", NonUniqueResultException);
        
        return responseService.getFailResult(-1, getMessage("중복된 데이터가 존재합니다.."));
    }
    
    /**
     * @Valid 에 의해 발생되는 Exception 메시지 처리
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        
        log.debug("e.getBindingResult() :: {}", e.getBindingResult());
        
        Map<String, String> errMsg = makeErrorResponse(e.getBindingResult());
        
        return responseService.getFailResult(-1,
                getMessage("argumentException") + " :: [" + errMsg.get("errorField")
                        + "] :: " + errMsg.get("description"));
    }
    
    /**
     * <pre>
     * null이 아닌 인자의 값이 잘못되었을 때
     * </pre>
     * 
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult argumentException(HttpServletRequest request,
            IllegalArgumentException e) {
        
        log.debug("[ExceptionAdvice.argumentException] :: {}", e.toString());
        
        return responseService.getFailResult(-1, getMessage("argumentException"));
    }
    
    /**
     * <pre>
     * 객체 상태가 메서드 호출을 처리하기에 적절치 않을 때
     * </pre>
     * 
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult illegalStateException(HttpServletRequest request,
            IllegalStateException e) {
        
        log.debug("[ExceptionAdvice.illegalStateException] :: {}", e.toString());
        
        return responseService.getFailResult(-1, getMessage("argumentException"));
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request,
            UserNotFoundException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(-1, getMessage("userNotFound"));
    }
    
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult dataAccessException(HttpServletRequest request, DataAccessException dae) {
        Map<String, Object> result = getErrMsg(dae);
        log.info("[ExceptionAdvice >> dataAccessException] result :: {}", result);
        
        return responseService.getFailResult(Integer.parseInt(String.valueOf(result.get("errCode"))),
                result.get("errMsg").toString());
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        log.info("[ExceptionAdvice >> defaultException] getMessage :: {}", e.getMessage());
        
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(-1, getMessage("unKnown"));
    }
    
    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }
    
    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSourceAccessor.getMessage(code, args, LocaleContextHolder.getLocale());
    }
    
    /**
     * Error Message 처리
     *
     * @param ex
     * @return
     */
    private Map<String, Object> getErrMsg(DataAccessException ex) {
        int    errCode = 0;
        String errMsg  = "";
        
        // log.info("[DataAccessException >> getErrMsg] getMessage :: {}", ex.get);
        log.info("[DataAccessException >> getErrMsg] getMessage :: {}", ex.getMessage());
        
        if (ex instanceof BadSqlGrammarException) {
            SQLException se = ((BadSqlGrammarException) ex).getSQLException();
            
            errCode = se.getErrorCode();
            errMsg  = se.getMessage();
        } else if (ex instanceof InvalidResultSetAccessException) {
            SQLException se = ((InvalidResultSetAccessException) ex).getSQLException();
            
            errCode = se.getErrorCode();
            errMsg  = se.getMessage();
        } else if (ex instanceof DuplicateKeyException) {
            // 고유성 제한 위반과 같은 데이터 삽입 또는 업데이트시 무결성 위반
            errCode = -1;
            errMsg  = getMessage("duplicateKeyException"); // "중복된 데이터가 존재합니다.";
        } else if (ex instanceof DataIntegrityViolationException) {
            // 고유성 제한 위반과 같은 데이터 삽입 또는 업데이트시 무결성 위반
            // "등록된 데이터가 컬럼의 속성과 다릅니다. (길이, 속성, 필수입력항목 등..)";
            errCode = -1;
            //errMsg  = getMessage("dataIntegrityViolationException"); // "등록된 데이터가 컬럼의 속성과 다릅니다. (길이, 속성, 필수입력항목 등..)";
            errMsg  = getMessage("duplicateKeyException"); // "중복된 데이터가 존재합니다.";
        } else if (ex instanceof DataAccessResourceFailureException) {
            // 데이터 액세스 리소스가 완전히 실패했습니다 (예 : 데이터베이스에 연결할 수 없음)
            errCode = -1;
            errMsg  = getMessage("dataAccessResourceFailureException"); // "데이터베이스 연결오류";
        } else if (ex instanceof CannotAcquireLockException) {
            
        } else if (ex instanceof DeadlockLoserDataAccessException) {
            // 교착 상태로 인해 현재 작업이 실패했습니다.
            errCode = -1;
            errMsg  = getMessage("deadlockLoserDataAccessException"); // "교착 상태로 인한 현재 작업 실패";
        } else if (ex instanceof CannotSerializeTransactionException) {
            errCode = -1;
            errMsg  = "직렬화 모드에서 트랜잭션을 완료 할 수 없음";
        } else {
            errCode = -1;
            errMsg  = ex.getMessage();
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("errCode", errCode);
        map.put("errMsg", errMsg);
        
        return map;
    }
    
    private Map<String, String> makeErrorResponse(BindingResult bindingResult) {
        String description = "";
        String defaultMsg  = "";
        String errorField  = "";
        
        Map<String, String> errMap = new HashMap<>();
        
        // 에러가 있다면
        if (bindingResult.hasErrors()) {
            // DTO에 설정한 meaasge값을 가져온다
            defaultMsg = bindingResult.getFieldError().getDefaultMessage();
            
            log.debug("defaultMsg :: {}", defaultMsg);
            
            errorField = bindingResult.getFieldError().getField();
            
            log.debug("errorField :: {}", errorField);
            
            errMap.put("errorField", errorField);
            
            // DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();
            
            log.debug("bindResultCode :: {}", bindResultCode);
            
            switch (bindResultCode) {
                case "NotNull":
                    description = getMessage("valid.notnull");
                    break;
                case "Min":
                    description = getMessage("valid.min");
                    break;
                case "Max":
                    description = getMessage("valid.max");
                    break;
                case "Size":
                    description = defaultMsg;
                    break;
            }
        }
        
        errMap.put("description", description);
        
        return errMap;
    }
}