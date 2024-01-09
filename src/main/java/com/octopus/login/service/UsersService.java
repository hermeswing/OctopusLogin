package com.octopus.login.service;

import java.util.List;

import com.octopus.login.dto.UserDTO;

public interface UsersService {  
    UserDTO findUser(String email);
    
    UserDTO findById(final String userId);
    
    UserDTO findByEmailAndPassword(String email, String password);
    
    List<UserDTO> findAll();
}