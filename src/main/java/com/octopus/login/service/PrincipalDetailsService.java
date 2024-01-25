package com.octopus.login.service;

import com.octopus.base.exception.ExUserNotFoundException;
import com.octopus.entity.Users;
import com.octopus.login.dto.PrincipalDetails;
import com.octopus.login.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

// UserDetailsService는 IoC로 찾음
@Slf4j
@Component
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UsersRepository authRepository;

    @Override
    public PrincipalDetails loadUserByUsername( String userId ) throws UsernameNotFoundException {
        log.debug( "★★★★★★★★★★★★★★★★★" );
        Users findUser = authRepository.findByUserId( userId )
                .orElseThrow( () -> new ExUserNotFoundException( "Can't find user with this User ID. -> " + userId ) );

        log.debug( "findByEmail :: {}", findUser );

        return new PrincipalDetails( findUser );
    }

}
