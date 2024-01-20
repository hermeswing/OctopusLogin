package com.octopus.login.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.octopus.entity.Users;
import com.octopus.login.dto.PrincipalDetails;
import com.octopus.login.repository.AuthRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// UserDetailsService는 IoC로 찾음
@Slf4j
@Component
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    
    private final AuthRepository authRepository;
    
    @Override
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users findUser = authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with this email. -> " + email));
        
        log.debug("findByEmail :: {}", findUser);
        
        if (findUser != null) {
            PrincipalDetails userDetails = new PrincipalDetails(findUser);
            return userDetails;
        }
        
        return null;
    }
    
}
