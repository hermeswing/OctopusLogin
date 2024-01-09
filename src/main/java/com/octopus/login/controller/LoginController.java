package com.octopus.login.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octopus.base.utils.JwtManager;
import com.octopus.login.dto.UserDTO;
import com.octopus.login.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    
    private final UserServiceImpl usersService;
    private final JwtManager      jwtManager;
    
    @PostMapping("/login")
    public String login(UserDTO userDto) {
        UserDTO users = usersService.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        return jwtManager.createToken(users.getEmail(), Arrays.asList(users.getUserRole().getValue()));
    }
    
}