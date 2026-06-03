package com.example.stoneocean.config;

import com.example.stoneocean.Interceptor.CacheControlInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册缓存拦截器，对所有请求生效（可通过addPathPatterns控制范围）
        registry.addInterceptor(new CacheControlInterceptor())
                .addPathPatterns("/file/**");  // 匹配所有路径
    }
}