package com.example.stoneocean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 设置允许跨域的路径
                        .allowedOriginPatterns(
                                "http://localhost",
                                "http://127.0.0.1",
                                "https://*.nginx.home",
                                "https://nginx.home",
                                "https://*.warmstone.top",
                                "https://warmstone.top"
                                ) // 设置允许跨域请求的来源，"*"表示所有来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                        .allowedHeaders("*") // 允许的请求头，默认允许所有
                        .allowCredentials(true); // 是否允许发送Cookie
            }
        };
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


}
