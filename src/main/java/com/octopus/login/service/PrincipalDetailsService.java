package com.octopus.login.service;

import com.octopus.base.exception.ExUserNotFoundException;
import com.octopus.entity.Users;
import com.octopus.login.dto.PrincipalDetails;
import com.octopus.login.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

// UserDetailsService는 IoC로 찾음
@Slf4j
@Component
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UsersRepository userRepository;

    @Override
    public PrincipalDetails loadUserByUsername( String userId ) {
        log.debug( "★★★★★★★★★★★★★★★★★" );

        boolean existsYn = userRepository.existsByUserId( userId );

        log.debug( "existsYn :: {}", existsYn );

        if( existsYn ) {
            Optional<Users> findUser = userRepository.findByUserId( userId );

            log.debug( "findUser :: {}", findUser.get() );

            return new PrincipalDetails( findUser.get() );
        } else {
            throw new ExUserNotFoundException( "Can't find user with this User ID. -> " + userId );
        }
    }

}
