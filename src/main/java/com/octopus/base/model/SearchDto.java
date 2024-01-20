package com.octopus.base.model;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * <pre>
 * </pre>
 */
@Data
@MappedSuperclass // BaseEntity를 상속한 Entity들은 아래의 필드들을 컬럼으로 인식한다.
@EntityListeners(AuditingEntityListener.class) // Audting(자동으로 값 Mapping) 기능 추가
public class SearchDto {
    
    protected int        page;       // 현재 페이지 번호
    protected int        recordSize; // 페이지당 출력할 데이터 개수
    protected int        pageSize;   // 화면 하단에 출력할 페이지 사이즈
    protected Pagination pagination; // 페이지네이션 정보
    
    public SearchDto() {
        this.page       = 0;
        this.recordSize = 10;
        this.pageSize   = 10;
    }
    
}