package com.octopus.login.dto;

import com.octopus.base.enumeration.UserRole;
import com.octopus.base.model.BaseDTO;
import com.octopus.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor // 모든인자를 가지는객체 생성
@NoArgsConstructor // 인자 없이 객체 생성
@ToString
@EqualsAndHashCode( callSuper = true ) // true의 경우 부모 클래스 필드 값들도 동일한지 체크하며, false(기본값)일 경우 자신 클래스의 필드 값만 고려한다.
public class UserDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank( message = "아이디는 필수 입력값입니다." )
    @Pattern( regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다." )
    private String userId;
    private String userNm;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;
    private UserRole userRole;

    public UserDTO( Users user ) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.userNm = user.getUserNm();
        super.crtId = user.getCrtId();
        super.crtDt = user.getCrtDt();
        super.mdfId = user.getMdfId();
        super.mdfDt = user.getMdfDt();
    }

    public Users toEntity() {
        return Users.builder().userId( userId ).userNm( userNm ).email( email ).password( password ).userRole( userRole )
                .crtId( getCrtId() ).mdfId( getMdfId() ).build();
    }

}