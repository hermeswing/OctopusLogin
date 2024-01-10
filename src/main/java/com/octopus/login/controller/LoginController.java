package com.octopus.login.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octopus.base.utils.JwtManager;
import com.octopus.login.dto.UserDTO;
import com.octopus.login.service.impl.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = { "1-1. Login Controller" })
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    
    private final UserServiceImpl usersService;
    private final JwtManager      jwtManager;
    
    @ApiOperation(value = "로그인", notes = "로그인을 시도합니다.")
    @PostMapping("/login")
    public String login(UserDTO userDto) {
        UserDTO users = usersService.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        return jwtManager.createToken(users.getEmail(), Arrays.asList(users.getUserRole().getValue()));
    }
    
}