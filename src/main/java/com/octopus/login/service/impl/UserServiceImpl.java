package com.octopus.login.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.octopus.entity.TUserM;
import com.octopus.login.dto.UserDTO;
import com.octopus.login.repository.UserRepository;
import com.octopus.login.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UsersService {
    
    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDTO findUser(String email) {
        TUserM users = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new BadCredentialsException("회원 정보를 찾을 수 없습니다."));
        return UserDTO.builder().id(users.getId()).password(users.getPassword()).userRole(users.getUserRole())
                .email(users.getEmail()).build();
    }
    
    /**
     * 사용자 조회
     * 
     * @param userId -사용자 ID
     * @return 사용자 정보
     */
    @Override
    public UserDTO findById(final String userId) {
        
        TUserM  user = userRepository.findByUserId(userId);
        UserDTO dto  = new UserDTO(user);
        log.debug("dto :: {}", dto);
        
        return dto;
    }
    
    @Override
    public UserDTO findByEmailAndPassword(String email, String password) {
        TUserM users = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new BadCredentialsException("이메일이나 비밀번호를 확인해주세요."));
        
        if (passwordEncoder.matches(password, users.getPassword()) == false) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        
        return UserDTO.builder().id(users.getId()).password(users.getPassword()).userRole(users.getUserRole())
                .email(users.getEmail()).build();
    }
    
    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(u -> UserDTO.builder().id(u.getId()).password(u.getPassword())
                .userRole(u.getUserRole()).email(u.getEmail()).build()).collect(Collectors.toList());
    }
    
}