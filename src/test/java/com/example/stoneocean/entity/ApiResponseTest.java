package com.example.stoneocean.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResponse 通用返回结构测试")
class ApiResponseTest {

    @Nested
    @DisplayName("success() 方法")
    class SuccessTests {

        @Test
        @DisplayName("success(data) 返回 statusCode=200, message='成功', data=提供的数据")
        void successWithData() {
            ApiResponse<String> response = ApiResponse.success("hello");
            assertEquals(200, response.getStatusCode());
            assertEquals("成功", response.getMessage());
            assertEquals("hello", response.getData());
        }

        @Test
        @DisplayName("success(data, customMessage) 返回 statusCode=200, message=自定义消息")
        void successWithDataAndMessage() {
            ApiResponse<Integer> response = ApiResponse.success(42, "操作完成");
            assertEquals(200, response.getStatusCode());
            assertEquals("操作完成", response.getMessage());
            assertEquals(42, response.getData());
        }

        @Test
        @DisplayName("success(null) 返回 statusCode=200, data=null")
        void successWithNullData() {
            ApiResponse<Object> response = ApiResponse.success(null);
            assertEquals(200, response.getStatusCode());
            assertEquals("成功", response.getMessage());
            assertNull(response.getData());
        }
    }

    @Nested
    @DisplayName("failed() 方法")
    class FailedTests {

        @Test
        @DisplayName("failed(message) 返回 statusCode=500, data=null")
        void failedWithMessage() {
            ApiResponse<Object> response = ApiResponse.failed("出错了");
            assertEquals(500, response.getStatusCode());
            assertEquals("出错了", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("failed('') 返回 statusCode=500, message=空字符串")
        void failedWithEmptyMessage() {
            ApiResponse<Object> response = ApiResponse.failed("");
            assertEquals(500, response.getStatusCode());
            assertEquals("", response.getMessage());
            assertNull(response.getData());
        }
    }

    @Nested
    @DisplayName("byFlag() 方法")
    class ByFlagTests {

        @Test
        @DisplayName("byFlag(true, data) 返回成功响应")
        void byFlagTrue() {
            ApiResponse<String> response = ApiResponse.byFlag(true, "data");
            assertEquals(200, response.getStatusCode());
            assertEquals("成功", response.getMessage());
            assertEquals("data", response.getData());
        }

        @Test
        @DisplayName("byFlag(false, data) 返回失败响应，默认消息='操作失败'")
        void byFlagFalseDefaultMessage() {
            ApiResponse<String> response = ApiResponse.byFlag(false, "data");
            assertEquals(500, response.getStatusCode());
            assertEquals("操作失败", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("byFlag(false, data, customMessage) 返回失败响应，自定义消息")
        void byFlagFalseCustomMessage() {
            ApiResponse<String> response = ApiResponse.byFlag(false, "data", "自定义失败");
            assertEquals(500, response.getStatusCode());
            assertEquals("自定义失败", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("byFlag(false, data, null) 使用默认消息'操作失败'")
        void byFlagFalseNullMessage() {
            ApiResponse<String> response = ApiResponse.byFlag(false, "data", null);
            assertEquals(500, response.getStatusCode());
            assertEquals("操作失败", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("byFlag(false, data, '') 使用默认消息'操作失败'")
        void byFlagFalseEmptyMessage() {
            ApiResponse<String> response = ApiResponse.byFlag(false, "data", "");
            assertEquals(500, response.getStatusCode());
            assertEquals("操作失败", response.getMessage());
            assertNull(response.getData());
        }
    }
}