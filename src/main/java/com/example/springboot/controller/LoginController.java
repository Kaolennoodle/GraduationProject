package com.example.springboot.controller;


import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserService userService;

    @PostMapping
    public boolean login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }
}
