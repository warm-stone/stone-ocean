package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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
    public void add(@Valid @RequestBody User user) {

    }


    @GetMapping("/oauth2Login/{registrationId}")
    public ApiResponse oauth2Login(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                   @AuthenticationPrincipal OAuth2User oauth2User,
                                   @PathVariable("registrationId") String registrationId) {
        Assert.isTrue(registrationId.equals("github"), "预期外的 registrationId ：【%s】".formatted(registrationId));
        return ApiResponse.success(oauth2User);
    }
}
