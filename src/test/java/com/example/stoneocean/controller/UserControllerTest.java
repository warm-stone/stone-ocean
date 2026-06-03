package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.entity.dto.AuthorizationDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private ITokenService tokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

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
    @DisplayName("add() 注册用户测试")
    class AddTests {

        @Test
        @DisplayName("账户已被注册 - 返回失败响应")
        void add_AccountAlreadyExists_ReturnsFailedResponse() {
            // Arrange
            User newUser = User.builder()
                    .account("existinguser")
                    .passwordHash("password")
                    .nickname("新昵称")
                    .build();
            
            when(userService.getByAccount("existinguser")).thenReturn(testUser);

            // Act
            ApiResponse<AuthorizationDTO> response = userController.add(newUser);

            // Assert
            assertEquals(500, response.getStatusCode());
            assertEquals("账户已被注册", response.getMessage());
            assertNull(response.getData());
            verify(userService, never()).save(any());
        }

        @Test
        @DisplayName("昵称已被使用 - 返回失败响应")
        void add_NicknameAlreadyExists_ReturnsFailedResponse() {
            // Arrange
            User newUser = User.builder()
                    .account("newuser")
                    .passwordHash("password")
                    .nickname("测试用户")
                    .build();
            
            when(userService.getByAccount("newuser")).thenReturn(null);
            when(userService.getByNickname("测试用户")).thenReturn(testUser);

            // Act
            ApiResponse<AuthorizationDTO> response = userController.add(newUser);

            // Assert
            assertEquals(500, response.getStatusCode());
            assertEquals("昵称已被使用", response.getMessage());
            assertNull(response.getData());
            verify(userService, never()).save(any());
        }

        @Test
        @DisplayName("邮箱为空字符串 - 应设置为 null")
        void add_EmptyEmail_SetsToNull() {
            User newUser = User.builder()
                    .account("newuser")
                    .passwordHash("password")
                    .nickname("新昵称")
                    .email("")
                    .build();
            
            User savedUser = User.builder()
                    .id(2L)
                    .account("newuser")
                    .passwordHash("password")
                    .nickname("新昵称")
                    .build();

            when(userService.getByAccount("newuser")).thenReturn(null, savedUser);
            when(userService.getByNickname("新昵称")).thenReturn(null);
            when(userService.save(newUser)).thenReturn(true);
            when(tokenService.token(savedUser)).thenReturn("test-token");

            ApiResponse<AuthorizationDTO> response = userController.add(newUser);

            assertNull(newUser.getEmail());
            assertEquals(200, response.getStatusCode());
            verify(userService).save(newUser);
        }

        @Test
        @DisplayName("保存失败 - 返回失败响应")
        void add_SaveFails_ReturnsFailedResponse() {
            // Arrange
            User newUser = User.builder()
                    .account("newuser")
                    .passwordHash("password")
                    .nickname("新昵称")
                    .build();
            
            when(userService.getByAccount("newuser")).thenReturn(null);
            when(userService.getByNickname("新昵称")).thenReturn(null);
            when(userService.save(newUser)).thenReturn(false);

            // Act
            ApiResponse<AuthorizationDTO> response = userController.add(newUser);

            // Assert
            assertEquals(500, response.getStatusCode());
            assertEquals("注册失败", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("注册成功 - 返回授权信息")
        void add_Success_ReturnsAuthorizationDTO() {
            // Arrange
            User newUser = User.builder()
                    .account("newuser")
                    .passwordHash("password")
                    .nickname("新昵称")
                    .build();
            
            User savedUser = User.builder()
                    .id(2L)
                    .account("newuser")
                    .passwordHash("password")
                    .nickname("新昵称")
                    .build();

            when(userService.getByAccount("newuser")).thenReturn(null, savedUser);
            when(userService.getByNickname("新昵称")).thenReturn(null);
            when(userService.save(newUser)).thenReturn(true);
            when(tokenService.token(savedUser)).thenReturn("jwt-token-123");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.add(newUser);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertEquals("成功", response.getMessage());
            assertNotNull(response.getData());
            assertEquals("jwt-token-123", response.getData().getToken());
            assertEquals(savedUser, response.getData().getUser());
            verify(userService).save(newUser);
            verify(tokenService).token(savedUser);
        }
    }

    @Nested
    @DisplayName("login() 登录测试")
    class LoginTests {

        @Test
        @DisplayName("使用 Jwt principal 登录成功")
        void login_WithJwtPrincipal_ReturnsAuthorizationDTO() {
            // Arrange
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getById(1L)).thenReturn(testUser);
            when(tokenService.token(testUser)).thenReturn("login-token");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.login(authentication);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
            assertEquals("login-token", response.getData().getToken());
            assertEquals(testUser, response.getData().getUser());
        }

        @Test
        @DisplayName("使用 User principal 登录成功")
        void login_WithUserPrincipal_ReturnsAuthorizationDTO() {
            // Arrange
            User principalUser = User.builder()
                    .id(1L)
                    .account("testuser")
                    .passwordHash("hash")
                    .nickname("测试")
                    .build();
            
            when(authentication.getPrincipal()).thenReturn(principalUser);
            when(userService.getById(1L)).thenReturn(testUser);
            when(tokenService.token(testUser)).thenReturn("login-token");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.login(authentication);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
            assertEquals("login-token", response.getData().getToken());
        }
    }

    @Nested
    @DisplayName("getSelfInfo() 获取当前用户信息测试")
    class GetSelfInfoTests {

        @Test
        @DisplayName("获取当前用户信息 - 密码哈希应被置空")
        void getSelfInfo_Success_PasswordHashCleared() {
            // Arrange
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getById(1L)).thenReturn(testUser);

            // Act
            ApiResponse<User> response = userController.getSelfInfo(authentication);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
            assertEquals("", response.getData().getPasswordHash());
            assertEquals("testuser", response.getData().getAccount());
        }
    }

    @Nested
    @DisplayName("modify() 修改用户信息测试")
    class ModifyTests {

        @Test
        @DisplayName("用户ID匹配 - 修改成功")
        void modify_UserIdMatches_Success() {
            // Arrange
            User modifyUser = User.builder()
                    .id(1L)
                    .account("updateduser")
                    .passwordHash("newhash")
                    .nickname("更新昵称")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getByAccount("updateduser")).thenReturn(null);
            when(userService.getByNickname("更新昵称")).thenReturn(null);
            when(userService.updateById(modifyUser)).thenReturn(true);
            when(tokenService.token(modifyUser)).thenReturn("new-token");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
            assertEquals("new-token", response.getData().getToken());
        }

        @Test
        @DisplayName("用户ID不匹配 - 抛出异常")
        void modify_UserIdMismatch_ThrowsException() {
            // Arrange
            User modifyUser = User.builder()
                    .id(2L)
                    .account("updateduser")
                    .passwordHash("newhash")
                    .nickname("更新昵称")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                userController.modify(authentication, modifyUser);
            });
        }

        @Test
        @DisplayName("账户已被其他用户使用 - 返回失败响应")
        void modify_AccountUsedByOtherUser_ReturnsFailedResponse() {
            // Arrange
            User modifyUser = User.builder()
                    .id(1L)
                    .account("existinguser")
                    .passwordHash("newhash")
                    .nickname("更新昵称")
                    .build();

            User otherUser = User.builder()
                    .id(2L)
                    .account("existinguser")
                    .passwordHash("hash")
                    .nickname("其他用户")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getByAccount("existinguser")).thenReturn(otherUser);

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertEquals(500, response.getStatusCode());
            assertEquals("账户已被注册", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("昵称已被其他用户使用 - 返回失败响应")
        void modify_NicknameUsedByOtherUser_ReturnsFailedResponse() {
            // Arrange
            User modifyUser = User.builder()
                    .id(1L)
                    .account("updateduser")
                    .passwordHash("newhash")
                    .nickname("已存在昵称")
                    .build();

            User otherUser = User.builder()
                    .id(2L)
                    .account("otheruser")
                    .passwordHash("hash")
                    .nickname("已存在昵称")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getByAccount("updateduser")).thenReturn(null);
            when(userService.getByNickname("已存在昵称")).thenReturn(otherUser);

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertEquals(500, response.getStatusCode());
            assertEquals("昵称已被使用", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("更新失败 - 返回失败响应")
        void modify_UpdateFails_ReturnsFailedResponse() {
            // Arrange
            User modifyUser = User.builder()
                    .id(1L)
                    .account("updateduser")
                    .passwordHash("newhash")
                    .nickname("更新昵称")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getByAccount("updateduser")).thenReturn(null);
            when(userService.getByNickname("更新昵称")).thenReturn(null);
            when(userService.updateById(modifyUser)).thenReturn(false);

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertEquals(500, response.getStatusCode());
            assertEquals("修改失败", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("用户ID为null时自动填充 - 修改成功")
        void modify_UserIdNull_AutoFilled() {
            // Arrange
            User modifyUser = User.builder()
                    .account("updateduser")
                    .passwordHash("newhash")
                    .nickname("更新昵称")
                    .build(); // id is null

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getByAccount("updateduser")).thenReturn(null);
            when(userService.getByNickname("更新昵称")).thenReturn(null);
            when(userService.updateById(any(User.class))).thenReturn(true);
            when(tokenService.token(any(User.class))).thenReturn("new-token");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertEquals(1L, modifyUser.getId()); // ID should be auto-filled
        }

        @Test
        @DisplayName("邮箱为空字符串 - 应设置为 null")
        void modify_EmptyEmail_SetsToNull() {
            // Arrange
            User modifyUser = User.builder()
                    .id(1L)
                    .account("updateduser")
                    .passwordHash("newhash")
                    .nickname("更新昵称")
                    .email("")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getByAccount("updateduser")).thenReturn(null);
            when(userService.getByNickname("更新昵称")).thenReturn(null);
            when(userService.updateById(modifyUser)).thenReturn(true);
            when(tokenService.token(modifyUser)).thenReturn("new-token");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertNull(modifyUser.getEmail()); // Email should be set to null
            assertEquals(200, response.getStatusCode());
        }

        @Test
        @DisplayName("账户被当前用户自己使用 - 允许修改")
        void modify_AccountUsedBySameUser_Success() {
            // Arrange
            User modifyUser = User.builder()
                    .id(1L)
                    .account("testuser")
                    .passwordHash("newhash")
                    .nickname("更新昵称")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            // Same user owns this account
            when(userService.getByAccount("testuser")).thenReturn(testUser);
            when(userService.getByNickname("更新昵称")).thenReturn(null);
            when(userService.updateById(modifyUser)).thenReturn(true);
            when(tokenService.token(modifyUser)).thenReturn("new-token");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
        }

        @Test
        @DisplayName("昵称被当前用户自己使用 - 允许修改")
        void modify_NicknameUsedBySameUser_Success() {
            // Arrange
            User modifyUser = User.builder()
                    .id(1L)
                    .account("updateduser")
                    .passwordHash("newhash")
                    .nickname("测试用户")
                    .build();

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getByAccount("updateduser")).thenReturn(null);
            // Same user owns this nickname
            when(userService.getByNickname("测试用户")).thenReturn(testUser);
            when(userService.updateById(modifyUser)).thenReturn(true);
            when(tokenService.token(modifyUser)).thenReturn("new-token");

            // Act
            ApiResponse<AuthorizationDTO> response = userController.modify(authentication, modifyUser);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
        }
    }

    @Nested
    @DisplayName("getById() 获取用户信息测试")
    class GetByIdTests {

        @Test
        @DisplayName("获取用户信息 - 密码哈希应被置空")
        void getById_Success_PasswordHashCleared() {
            // Arrange
            when(userService.getById(1L)).thenReturn(testUser);

            // Act
            ApiResponse<User> response = userController.getById(1L);

            // Assert
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getData());
            assertEquals("", response.getData().getPasswordHash());
            assertEquals("testuser", response.getData().getAccount());
            assertEquals("测试用户", response.getData().getNickname());
        }
    }

    @Nested
    @DisplayName("getUserId() 私有方法测试")
    class GetUserIdTests {

        @Test
        @DisplayName("从 User principal 提取 userId")
        void getUserId_FromUserPrincipal_Success() {
            // Arrange
            User principalUser = User.builder()
                    .id(1L)
                    .account("testuser")
                    .passwordHash("hash")
                    .nickname("测试")
                    .build();
            
            when(authentication.getPrincipal()).thenReturn(principalUser);
            when(userService.getById(1L)).thenReturn(testUser);
            when(tokenService.token(testUser)).thenReturn("token");

            // Act - 通过 login 方法间接测试
            ApiResponse<AuthorizationDTO> response = userController.login(authentication);

            // Assert
            assertEquals(200, response.getStatusCode());
            verify(userService).getById(1L);
        }

        @Test
        @DisplayName("从 Jwt principal 提取 userId")
        void getUserId_FromJwtPrincipal_Success() {
            // Arrange
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(userService.getById(1L)).thenReturn(testUser);
            when(tokenService.token(testUser)).thenReturn("token");

            // Act - 通过 login 方法间接测试
            ApiResponse<AuthorizationDTO> response = userController.login(authentication);

            // Assert
            assertEquals(200, response.getStatusCode());
            verify(userService).getById(1L);
        }

        @Test
        @DisplayName("userId 为 -1 时抛出异常")
        void getUserId_InvalidUserId_ThrowsException() {
            // Arrange
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", -1L);
            
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaims()).thenReturn(claims);
            when(authentication.getPrincipal()).thenReturn(jwt);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                userController.login(authentication);
            });
        }
    }
}