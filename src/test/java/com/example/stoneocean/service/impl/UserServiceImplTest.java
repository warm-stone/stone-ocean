package com.example.stoneocean.service.impl;

import com.example.stoneocean.entity.User;
import com.example.stoneocean.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private IUserService userService;
    @Autowired
    private ObjectMapper objectMapper; // 注入Spring配置的ObjectMapper


    @Test
    void testGetByAccount_nullParam() throws JsonProcessingException {
        System.out.println(("----- 根据账户获取用户信息 ------"));
        assertThrowsExactly(ConstraintViolationException.class, () -> {
            userService.getByAccount(null);
        });
    }
}