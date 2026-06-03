package com.example.stoneocean.entity.dto;

import lombok.Data;

import java.util.Set;

@Data
public class OAuth2ClientInfo {
    private String clientId;
    private Set<String> scopes;
    private String authorizationUri;
}
