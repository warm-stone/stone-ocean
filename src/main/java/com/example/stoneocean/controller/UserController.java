package com.example.stoneocean.controller;

import com.example.stoneocean.entity.User;
import com.example.stoneocean.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    private boolean checkUser(User user) {
        if (user == null) return false;
        if (user.getUsername() == null
                || user.getPassword() == null
                || user.getNickname() == null) return false;
        return true;
    }

    @PostMapping("/add")
    public void add(@RequestBody User user) {

        userService.saveUserDetails(user);
    }
}
