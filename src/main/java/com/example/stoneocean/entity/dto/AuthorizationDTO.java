package com.example.stoneocean.entity.dto;


import com.example.stoneocean.entity.User;
import lombok.Data;

@Data
public class AuthorizationDTO {
    private String token;
    private User user;
    public AuthorizationDTO(String token, User user) {
        this.token = token;
        this.user = user;

    }
}
