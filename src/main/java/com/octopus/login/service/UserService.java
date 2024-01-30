package com.octopus.login.service;

import com.octopus.entity.Users;
import com.octopus.login.dto.UserDTO;
import com.octopus.login.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * 출처 : <a href="https://velog.io/@u-nij/JWT-JWT-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-4-Redis-%EC%84%A4%EC%A0%95RedisRepositoryConfig-RedisService">...</a>
 * </pre>
 *
 * @author jypark
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional( readOnly = true )
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository userRepository;

    @Transactional( readOnly = true )
    public boolean existsByUserId( String userId ) {
        return userRepository.existsByUserId( userId );
    }

    @Transactional( readOnly = true )
    public boolean existsByUserNm( String userNm ) {
        return userRepository.existsByUserNm( userNm );
    }

    @Transactional( readOnly = true )
    public boolean existsByEmail( String email ) {
        return userRepository.existsByEmail( email );
    }

    @Transactional
    public void registerUser( UserDTO.UserDto userDto ) {
        String encodedPassword = passwordEncoder.encode( userDto.getPassword() );
        log.debug( "encodedPassword :: {}", encodedPassword );

        Users users = Users.createEntiry( userDto, encodedPassword );

        Users saveUsers = userRepository.save( users );

        log.debug( "saveUsers :: {}", saveUsers );
    }

    @Transactional
    public List<UserDTO.UserDto> findAll( UserDTO.ParamDto paramDto ) {
        List<Users> users = userRepository.findAll();
        return UserDTO.UserDto.convertUsersToUserDtos( users );
    }
}
