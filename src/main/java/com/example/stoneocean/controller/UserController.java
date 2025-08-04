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
 *  前端控制器
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private IUserService userService;
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public void add(@RequestBody User user) {
        userService.saveUserDetails(user);
    }
}
