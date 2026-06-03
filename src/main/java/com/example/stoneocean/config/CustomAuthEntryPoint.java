package com.example.stoneocean.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

// 自定义未授权处理器：不设置 WWW-Authenticate 头
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        // 仅返回 401 状态码，不添加 WWW-Authenticate 头
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}