package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.entity.dto.AuthorizationDTO;
import com.example.stoneocean.entity.dto.LoginRequest;
import com.example.stoneocean.service.ITokenService;
import com.example.stoneocean.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final ITokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          IUserService userService,
                          ITokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthorizationDTO> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = tokenService.token(user);
            return ApiResponse.success(new AuthorizationDTO(token, user));
        } catch (AuthenticationException e) {
            return ApiResponse.failed("账号或密码错误", 401);
        }
    }
}
