package com.octopus.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.octopus.entity.Users;

// @Repository : JpaRepository를 사용하면 @Repository를 사용하지 않아도 됨.
public interface AuthRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);
}