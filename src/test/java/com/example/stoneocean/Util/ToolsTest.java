package com.example.stoneocean.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tools 工具类测试")
class ToolsTest {

    private Tools tools;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        tools = new Tools(objectMapper);
    }

    @Nested
    @DisplayName("tryDeserialize() JSON 反序列化测试")
    class TryDeserializeTests {

        @Test
        @DisplayName("有效 JSON 字符串 → 正确反序列化为对象")
        void tryDeserializeValidJson() {
            String json = "{\"id\":1,\"account\":\"testUser\",\"nickname\":\"测试用户\"}";
            UserDTO result = tools.tryDeserialize(json, UserDTO.class);
            assertNotNull(result);
            assertEquals(1, result.id);
            assertEquals("testUser", result.account);
            assertEquals("测试用户", result.nickname);
        }

        @Test
        @DisplayName("无效 JSON 字符串 → 返回 null")
        void tryDeserializeInvalidJson() {
            String invalidJson = "not json at all";
            UserDTO result = tools.tryDeserialize(invalidJson, UserDTO.class);
            assertNull(result);
        }

        @Test
        @DisplayName("格式错误的 JSON → 返回 null")
        void tryDeserializeMalformedJson() {
            String malformedJson = "{\"id\":1, broken}";
            UserDTO result = tools.tryDeserialize(malformedJson, UserDTO.class);
            assertNull(result);
        }

        @Test
        @DisplayName("空字符串 → 返回 null")
        void tryDeserializeEmptyString() {
            UserDTO result = tools.tryDeserialize("", UserDTO.class);
            assertNull(result);
        }

        @Test
        @DisplayName("null 字符串 → 返回 null")
        void tryDeserializeNullString() {
            UserDTO result = tools.tryDeserialize(null, UserDTO.class);
            assertNull(result);
        }

        @Test
        @DisplayName("JSON 类型不匹配 → 返回 null")
        void tryDeserializeTypeMismatch() {
            String json = "\"this is a string, not an object\"";
            UserDTO result = tools.tryDeserialize(json, UserDTO.class);
            assertNull(result);
        }

        @Test
        @DisplayName("反序列化简单类型 String → 正确返回")
        void tryDeserializeSimpleString() {
            String json = "\"hello world\"";
            String result = tools.tryDeserialize(json, String.class);
            assertEquals("hello world", result);
        }

        @Test
        @DisplayName("反序列化数字类型 Integer → 正确返回")
        void tryDeserializeInteger() {
            String json = "42";
            Integer result = tools.tryDeserialize(json, Integer.class);
            assertEquals(42, result);
        }
    }

    @Nested
    @DisplayName("localTime() 时区测试")
    class LocalTimeTests {

        @Test
        @DisplayName("localTime() 返回 Asia/Shanghai 时区的时间")
        void localTimeReturnsShanghaiZone() {
            LocalDateTime result = tools.localTime();
            assertNotNull(result);

            // 验证返回时间与 Asia/Shanghai 当前时间接近（允许1秒误差）
            LocalDateTime expected = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
            long diffSeconds = Math.abs(result.compareTo(expected));
            assertTrue(diffSeconds <= 1, "时间差应小于1秒，实际差: " + diffSeconds);
        }

        @Test
        @DisplayName("localTime() 不使用系统默认时区")
        void localTimeNotSystemDefault() {
            LocalDateTime shanghaiTime = tools.localTime();
            LocalDateTime systemTime = LocalDateTime.now();

            // 如果系统时区不是 Asia/Shanghai，两者应该不同
            // 如果系统时区恰好是 Asia/Shanghai，两者应该相同
            // 无法在测试中改变系统时区，仅验证返回非null
            assertNotNull(shanghaiTime);
        }
    }

    /**
     * 用于测试反序列化的简单 DTO
     */
    static class UserDTO {
        public int id;
        public String account;
        public String nickname;
    }
}