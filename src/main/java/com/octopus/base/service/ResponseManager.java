package com.octopus.base.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octopus.base.enumeration.ResultCode;
import com.octopus.base.model.CommonResult;
import com.octopus.base.model.ListResult;
import com.octopus.base.model.SingleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ResponseManager {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    // 쿼리 스트링 파라미터를 Map에 담아 반환
    public Map<String, Object> queryParamsToMap( final Object queryParams ) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Map<String, Object> data = new HashMap<>();
        // data.put("page", queryParams.getPage());
        // data.put("recordSize", queryParams.getRecordSize());
        // data.put("pageSize", queryParams.getPageSize());
        // data.put("keyword", queryParams.getKeyword());
        // data.put("searchType", queryParams.getSearchType());

        return objectMapper.convertValue( queryParams, Map.class );
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult( T data ) {
        SingleResult<T> result = new SingleResult<>();
        result.setData( data );
        setSuccessResult( result );

        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult( List<T> list ) {
        ListResult<T> result = new ListResult<>();
        result.setList( list );
        setSuccessResult( result );

        return result;
    }

    // HATEOAS를 적용한 다중건 결과를 처리하는 메소드
    // public <T> ListResult<T> getListResult(CollectionModel<T> collection) {
    // ListResult<T> result = new ListResult<>();
    // result.setCollection(collection);
    // setSuccessResult(result);
    // return result;
    // }

    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        result.setSuccess( true );
        result.setCode( ResultCode.SUCCESS.getCode() );
        result.setMsg( messageSourceAccessor.getMessage( "msg.ok" ) ); // 정상처리되었습니다.

        return result;
    }

    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult( String message ) {
        CommonResult result = new CommonResult();
        result.setSuccess( true );
        result.setCode( ResultCode.SUCCESS.getCode() );
        result.setMsg( message );

        return result;
    }

    /**
     * @Method : setSuccessResult
     * @Description : 결과에 api 요청 성공 데이터 세팅
     * @Parameter : [result]
     * @Return : null
     **/
    public void setSuccessResult( CommonResult result ) {
        result.setSuccess( true );
        result.setCode( ResultCode.SUCCESS.getCode() );
        result.setMsg( messageSourceAccessor.getMessage( "msg.ok" ) ); // 정상처리되었습니다.
    }

    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult( int code, String msg ) {
        CommonResult result = new CommonResult();
        result.setSuccess( false );
        result.setCode( code );
        result.setMsg( msg );

        return result;
    }

    /**
     * @Method : setFailResult
     * @Description : 실패 결과 바인딩
     * @Parameter : [commonResponse, result]
     * @Return : null
     **/
    public void setFailResult( CommonResult result ) {
        result.setSuccess( false );
        result.setMsg( messageSourceAccessor.getMessage( "msg.fail" ) ); // 실패하였습니다.
        result.setCode( -1 );
    }

}