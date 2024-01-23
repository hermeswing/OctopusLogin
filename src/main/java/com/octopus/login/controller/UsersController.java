package com.octopus.login.controller;

import com.octopus.base.model.CommonResult;
import com.octopus.base.service.ResponseManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.octopus.login.dto.UserDTO;
import com.octopus.login.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsersController {
    
    private final UserService userService;
    private final ResponseManager responseService;

    // 회원가입
    @PostMapping("/signup")
    public CommonResult signup( @RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);

        return responseService.getSuccessResult("생성되었습니다.");
    }
    
}