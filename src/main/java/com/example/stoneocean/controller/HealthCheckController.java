package com.example.stoneocean.controller;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("info")
public class HealthCheckController {

    @GetMapping("")
    public Info getInfo(OAuth2AuthenticationToken authentication) {
        return new Info()
                .setApplication("tutorial-social-logins")
                .setPrincipal(authentication.getPrincipal().getAttributes());
    }

    @Data
    @Accessors(chain = true)
    private static class Info {
        private String application;
        private Map<String, Object> principal;
    }
}
