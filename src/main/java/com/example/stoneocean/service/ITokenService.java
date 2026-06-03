package com.example.stoneocean.service;

import com.example.stoneocean.entity.User;
import org.springframework.security.core.Authentication;

public interface ITokenService {
    String token(Authentication authentication);
    String token(User user);
}
