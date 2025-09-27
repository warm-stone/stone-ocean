package com.example.stoneocean.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Tools {
    private final ObjectMapper objectMapper;

    public Tools(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T tryDeserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
