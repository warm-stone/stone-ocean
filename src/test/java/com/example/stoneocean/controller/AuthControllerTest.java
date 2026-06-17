package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.entity.dto.AuthorizationDTO;
import com.example.stoneocean.entity.dto.LoginRequest;
import com.example.stoneocean.service.ITokenService;
import com.example.stoneocean.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUserService userService;

    @Mock
    private ITokenService tokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .account("testuser")
                .passwordHash("hashedPassword")
                .nickname("测试用户")
                .email("test@example.com")
                .build();
    }

    @Nested
    @DisplayName("login() 登录测试")
    class LoginTests {

        @Test
        @DisplayName("登录成功 - 返回 AuthorizationDTO")
        void login_Success_ReturnsAuthorizationDTO() {
            // Arrange
            LoginRequest request = new LoginRequest();
            request.setAccount("testuser");
            request.setPassword("password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(tokenService.token(testUser)).thenReturn("jwt-token-123");

            // Act
            ApiResponse<AuthorizationDTO> response = authController.login(request);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
            assertEquals("jwt-token-123", response.getData().getToken());
            assertEquals(testUser, response.getData().getUser());
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(tokenService).token(testUser);
        }

        @Test
        @DisplayName("密码错误 - 返回 401")
        void login_BadCredentials_Returns401() {
            // Arrange
            LoginRequest request = new LoginRequest();
            request.setAccount("testuser");
            request.setPassword("wrongpassword");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            // Act
            ApiResponse<AuthorizationDTO> response = authController.login(request);

            // Assert
            assertEquals(401, response.getStatusCode());
            assertEquals("账号或密码错误", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("账户不存在 - 返回 401")
        void login_UserNotFound_Returns401() {
            // Arrange
            LoginRequest request = new LoginRequest();
            request.setAccount("nonexistent");
            request.setPassword("password");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            // Act
            ApiResponse<AuthorizationDTO> response = authController.login(request);

            // Assert
            assertEquals(401, response.getStatusCode());
            assertNull(response.getData());
        }
    }
}
