package com.octopus.login.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.octopus.entity.Users;
import com.octopus.login.dto.UserDTO;
import com.octopus.login.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 출처 : https://velog.io/@u-nij/JWT-JWT-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-4-Redis-%EC%84%A4%EC%A0%95RedisRepositoryConfig-RedisService
 * </pre>
 * 
 * @author jypark
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository userRepository;

    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId) {
        boolean dupYn = userRepository.existsByUserId(userId);
        return dupYn;
    }

    @Transactional(readOnly = true)
    public boolean existsByUserNm(String userNm) {
        boolean dupYn = userRepository.existsByUserNm(userNm);
        return dupYn;
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        boolean dupYn = userRepository.existsByEmail(email);
        return dupYn;
    }

    @Transactional
    public void registerUser(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        log.debug("encodedPassword :: {}", encodedPassword);
        
        Users users = Users.createEntiry(userDTO, encodedPassword);
        
        userRepository.save(users);
    }
}
