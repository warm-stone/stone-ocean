package com.example.stoneocean.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class Tools {
    private final ObjectMapper objectMapper;

    public Tools(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //  json 反序列化
    public <T> T tryDeserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public LocalDateTime localTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
    }
}
