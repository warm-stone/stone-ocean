/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.entity.dto.AuthorizationDTO;
import com.example.stoneocean.service.ITokenService;
import com.example.stoneocean.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * A controller for the token resource.
 *
 * @author Josh Cummings
 */
@RestController
@RequestMapping("/token")
public class TokenController {
    private final ITokenService tokenService;
    private final IUserService userService;

    public TokenController(ITokenService tokenService, IUserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/token")
    public ApiResponse<String> token(Authentication authentication) {
        Long userId = -1L;
        if (authentication.getPrincipal() instanceof User) {
            userId = ((User) authentication.getPrincipal()).getId();
        }
        else {
            userId = (Long) ((Jwt)authentication.getPrincipal()).getClaims().get("userId");
        }
        User user = userService.getById(userId);
        return ApiResponse.success(tokenService.token(user));
    }


}
