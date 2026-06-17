package com.example.stoneocean.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class Tools {

    private static final Logger log = LoggerFactory.getLogger(Tools.class);
    private final ObjectMapper objectMapper;

    public Tools(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //  json 反序列化
    public <T> T tryDeserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.warn("JSON反序列化失败: {}", e.getMessage());
            return null;
        }
    }

    public LocalDateTime localTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
    }
}
