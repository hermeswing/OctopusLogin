package com.octopus.login.repository;

import com.octopus.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// @Repository : JpaRepository를 사용하면 @Repository를 사용하지 않아도 됨.
public interface UsersRepository extends JpaRepository<Users, String> {
    /* 유효성 검사 - 중복 체크
     * 중복 : true
     * 중복이 아닌 경우 : false
     */
    boolean existsByUserId(String userId);
    boolean existsByUserNm(String userNm);
    boolean existsByEmail(String email);

    Optional<Users> findByUserId(String userId);
}