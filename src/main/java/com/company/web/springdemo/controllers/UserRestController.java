package com.company.web.springdemo.controllers;

import com.company.web.springdemo.helpers.UserMapper;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.models.UserDto;
import com.company.web.springdemo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService service;
    private final UserMapper userMapper;

    public UserRestController(UserService service, UserMapper userMapper) {
        this.service = service;
        this.userMapper = userMapper;
    }

    //TODO
}
