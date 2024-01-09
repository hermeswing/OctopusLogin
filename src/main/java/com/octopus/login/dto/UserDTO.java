package com.octopus.login.dto;

import com.octopus.base.model.BaseDTO;
import com.octopus.entity.TUserM;
import com.octopus.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor // 모든인자를 가지는객체 생성
@NoArgsConstructor // 인자 없이 객체 생성
@ToString
@EqualsAndHashCode(callSuper = true) // true의 경우 부모 클래스 필드 값들도 동일한지 체크하며, false(기본값)일 경우 자신 클래스의 필드 값만 고려한다.
public class UserDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;
    
    private Long     id;
    private String   userId;
    private String   userNm;
    private String   email;
    private String   password;
    private UserRole userRole;
    
    @Builder
    private UserDTO(Long id, String password, UserRole userRole, String email) {
        this.id       = id;
        this.password = password;
        this.userRole = userRole;
        this.email    = email;
    }
    
    public TUserM toEntity() {
        return TUserM.builder().userId(userId).userNm(userNm).email(email).password(password).userRole(userRole)
                .crtId(getCrtId()).mdfId(getMdfId()).build();
    }
    
    public UserDTO(TUserM user) {
        this.id     = user.getId();
        this.userId = user.getUserId();
        this.userNm = user.getUserNm();
        super.crtId = user.getCrtId();
        super.crtDt = user.getCrtDt();
        super.mdfId = user.getMdfId();
        super.mdfDt = user.getMdfDt();
    }
    
}