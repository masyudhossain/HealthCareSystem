package com.healthcare.controller;

import com.healthcare.dto.CreateUserRequest;
import com.healthcare.entity.User;
import com.healthcare.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('manage_users')")
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
}
