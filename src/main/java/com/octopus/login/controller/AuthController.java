package com.octopus.login.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.octopus.login.dto.UserDTO;
import com.octopus.utils.JwtFilter;
import com.octopus.utils.TokenProvider;

@RestController
public class AuthController {
    
    private final TokenProvider                tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    public AuthController(
            TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider                = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }
    
    @PostMapping("/login")
    public String getLoginToken(@RequestBody UserDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        
        System.out.println(authenticationToken);
        
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.createToken(authentication);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        
        return jwt;
    }
}