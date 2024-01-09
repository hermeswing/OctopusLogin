COMMENT on column T_USER_M.ID is 'ID';
COMMENT on column T_USER_M.USER_ID is '사용자 ID';
COMMENT on column T_USER_M.USER_NM is '사용자명';
COMMENT on column T_USER_M.PASSWORD is '비밀번호';
COMMENT on column T_USER_M.EMAIL is '이메일주소';
COMMENT on column T_USER_M.USER_ROLE is '사용자권한';
COMMENT on column T_USER_M.CRT_ID is '생성자ID';
COMMENT on column T_USER_M.CRT_DT is '생성일시';
COMMENT on column T_USER_M.MDF_ID is '수정자ID';
COMMENT on column T_USER_M.MDF_DT is '수정일시';

INSERT INTO T_USER_M(USER_ID, USER_NM, PASSWORD, EMAIL, USER_ROLE, CRT_ID, CRT_DT, MDF_ID, MDF_DT) VALUES ('admin', '어드민', '1', 'hermeswing@naver.com', 'ROLE_ADMIN', 'admin', NOW(), 'admin', NOW());
INSERT INTO T_USER_M(USER_ID, USER_NM, PASSWORD, EMAIL, USER_ROLE, CRT_ID, CRT_DT, MDF_ID, MDF_DT) VALUES ('hong', '홍길동', '1', 'hermeswing@naver.com', 'ROLE_USER', 'admin', NOW(), 'admin', NOW());
