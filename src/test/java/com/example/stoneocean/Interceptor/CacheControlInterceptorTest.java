package com.example.stoneocean.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CacheControlInterceptor 缓存控制拦截器测试")
class CacheControlInterceptorTest {

    private CacheControlInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new CacheControlInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("preHandle() 设置 Cache-Control 头为 'public, max-age=864000'")
    void preHandleSetsCacheControlHeader() throws Exception {
        interceptor.preHandle(request, response, null);

        verify(response).setHeader("Cache-Control", "public, max-age=864000");
    }

    @Test
    @DisplayName("preHandle() 返回 true（继续执行后续处理）")
    void preHandleReturnsTrue() throws Exception {
        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
    }

    @Test
    @DisplayName("preHandle() 仅设置一个头，不设置其他头")
    void preHandleSetsOnlyOneHeader() throws Exception {
        interceptor.preHandle(request, response, null);

        // 验证只调用了一次 setHeader
        verify(response, times(1)).setHeader(anyString(), anyString());
        verify(response, never()).addHeader(anyString(), anyString());
    }

    @Test
    @DisplayName("preHandle() 不使用 request")
    void preHandleDoesNotUseRequest() throws Exception {
        interceptor.preHandle(request, response, null);

        verifyNoInteractions(request);
    }
}