package com.octopus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.octopus.base.model.BaseEntity;
import com.octopus.enumeration.UserRole;
import com.octopus.login.dto.UserDTO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter // getter를 자동으로 생성합니다.
// @Setter // 객체가 무분별하게 변경될 가능성 있음
// @ToString(exclude = { "crtId", "crtDt", "mdfId", "mdfDt" }) // 연관관계 매핑된 엔티티 필드는 제거. 연관 관계 필드는 toString()에서 사용하지 않는 것이
// // 좋습니다.
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자없는 생성자를 자동으로 생성합니다. 기본 생성자의 접근 제어자가 불명확함. (access =
                                                   // AccessLevel.PROTECTED) 추가
@DynamicInsert // insert 시 null 인필드 제외
@DynamicUpdate // update 시 null 인필드 제외
// @AllArgsConstructor // 객체 내부의 인스턴스멤버들을 모두 가지고 있는 생성자를 생성 (매우 위험)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Post Entity에서 User와의 관계를 Json으로 변환시 오류 방지를 위한 코드
@Proxy(lazy = false)
@Entity // jpa entity임을 선언. 실제 DB의 테이블과 매칭될 Class
@Table(name = "T_USER_M")
public class TUserM extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    @Id // PK 필드임
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Comment ID
    
    @Column(nullable = false)
    private String userId; // 사용자 ID
    
    @Column(nullable = false)
    private String userNm; // 사용자명
    
    @Column(nullable = false)
    private String email; // 이메일주소
    
    @Column(nullable = false)
    private String password; // 비밀번호
    
    @Column(nullable = false)
    private UserRole userRole;
    
    @Builder
    public TUserM(Long id, String userId, String userNm, String email, String password, UserRole userRole,
            String crtId, String mdfId) {
        Assert.hasText(userId, "userId must not be empty");
        Assert.hasText(email, "email must not be empty");
        Assert.hasText(crtId, "crtId must not be empty");
        Assert.hasText(mdfId, "mdfId must not be empty");
        
        this.id       = id;
        this.userId   = userId;
        this.userNm   = userNm;
        this.email    = email;
        this.password = password;
        this.userRole = userRole;
        
        super.crtId = crtId;
        super.mdfId = mdfId;
    }
    
    /**
     * 사용자 정보 Update
     */
    public void updateComment(UserDTO dto) {
        this.userId   = dto.getUserId();
        this.userNm   = dto.getUserNm();
        this.email    = dto.getEmail();
        this.password = dto.getPassword();
        this.userRole = dto.getUserRole();
        super.mdfId   = dto.getMdfId();
    }
    
}
