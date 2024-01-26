package com.octopus.login.controller;

import com.octopus.base.exception.ExDuplicatedException;
import com.octopus.base.model.CommonResult;
import com.octopus.base.service.ResponseManager;
import com.octopus.login.dto.UserDTO;
import com.octopus.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UsersController {
    
    private final UserService userService;
    private final ResponseManager responseService;

    // 회원가입
    @PostMapping("/signup")
    public CommonResult signup( @Valid @RequestBody UserDTO userDTO) {
        if( userService.existsByUserId(userDTO.getUserId()) ) {
            throw new ExDuplicatedException("중복된 사용자가 존재합니다.");
        } else {
            userService.registerUser(userDTO);
        }

        return responseService.getSuccessResult("생성되었습니다.");
    }
    
}