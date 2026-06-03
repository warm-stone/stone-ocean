package com.example.stoneocean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebConfig {


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    /**
     * 注册 ServerEndpointExporter，使 Spring 能够扫描到 @ServerEndpoint 注解的类
     * 注意：仅在嵌入式容器（如 Tomcat）中需要，外部容器无需此 Bean
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
