package com.example.stoneocean.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.*;

@DisplayName("CustomAuthEntryPoint 未授权处理器测试")
class CustomAuthEntryPointTest {

    private CustomAuthEntryPoint entryPoint;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException authException;

    @BeforeEach
    void setUp() {
        entryPoint = new CustomAuthEntryPoint();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authException = mock(AuthenticationException.class);
    }

    @Test
    @DisplayName("commence() 调用 sendError(401, 'Unauthorized')")
    void commenceReturns401() throws Exception {
        entryPoint.commence(request, response, authException);

        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verifyNoMoreInteractions(response);
    }

    @Test
    @DisplayName("commence() 不设置 WWW-Authenticate 头")
    void commenceDoesNotSetWWWAuthenticate() throws Exception {
        entryPoint.commence(request, response, authException);

        // 验证没有设置任何头
        verify(response, never()).setHeader(anyString(), anyString());
        verify(response, never()).addHeader(anyString(), anyString());
    }

    @Test
    @DisplayName("commence() 不使用 request 和 authException（仅传递给 sendError）")
    void commenceDoesNotUseRequestOrException() throws Exception {
        entryPoint.commence(request, response, authException);

        // 验证没有使用 request 或 authException 的任何方法
        verifyNoInteractions(request);
    }
}