package com.example.stoneocean.service.impl;

import com.example.stoneocean.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TokenService 单元测试
 * 使用 Mockito 进行纯单元测试，不依赖 Spring 上下文
 */
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Authentication authentication;

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(jwtEncoder);
    }

    @Test
    @DisplayName("测试 token(Authentication) 方法 - 验证 JWT claims 正确性")
    void testTokenWithAuthentication_ShouldGenerateJwtWithCorrectClaims() {
        // Given
        String username = "testuser";
        String expectedToken = "mocked.jwt.token";
        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        Object principal = "test-principal";

        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(authentication.getPrincipal()).thenReturn(principal);

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn(expectedToken);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // When
        String actualToken = tokenService.token(authentication);

        // Then
        assertEquals(expectedToken, actualToken);

        // 捕获并验证 JwtEncoderParameters
        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder, times(1)).encode(captor.capture());

        JwtEncoderParameters capturedParams = captor.getValue();
        JwtClaimsSet claims = capturedParams.getClaims();

        // 验证 issuer
        assertEquals("self", claims.getClaim("iss"));

        // 验证 subject
        assertEquals(username, claims.getSubject());

        // 验证 scope claim
        String scope = claims.getClaim("scope");
        assertEquals("ROLE_USER ROLE_ADMIN", scope);

        // 验证 user claim
        assertEquals(principal, claims.getClaim("user"));

        // 验证 issuedAt 和 expiresAt
        Instant issuedAt = claims.getIssuedAt();
        Instant expiresAt = claims.getExpiresAt();
        assertNotNull(issuedAt);
        assertNotNull(expiresAt);

        // 验证过期时间约为 252000 秒后
        long durationSeconds = expiresAt.getEpochSecond() - issuedAt.getEpochSecond();
        assertEquals(252000L, durationSeconds);
    }

    @Test
    @DisplayName("测试 token(Authentication) 方法 - 空权限列表")
    void testTokenWithAuthentication_EmptyAuthorities_ShouldGenerateJwtWithEmptyScope() {
        // Given
        String username = "testuser";
        String expectedToken = "mocked.jwt.token";
        Collection<? extends GrantedAuthority> authorities = List.of();

        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(authentication.getPrincipal()).thenReturn("principal");

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn(expectedToken);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // When
        String actualToken = tokenService.token(authentication);

        // Then
        assertEquals(expectedToken, actualToken);

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(captor.capture());

        JwtClaimsSet claims = captor.getValue().getClaims();

        // 验证 scope 为空字符串
        String scope = claims.getClaim("scope");
        assertEquals("", scope);
    }

    @Test
    @DisplayName("测试 token(User) 方法 - 验证 JWT claims 正确性")
    void testTokenWithUser_ShouldGenerateJwtWithCorrectClaims() {
        // Given
        Long userId = 123L;
        String account = "testaccount";
        String expectedToken = "mocked.jwt.token.for.user";

        User user = User.builder()
                .id(userId)
                .account(account)
                .nickname("Test User")
                .passwordHash("hashedpassword")
                .build();

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn(expectedToken);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // When
        String actualToken = tokenService.token(user);

        // Then
        assertEquals(expectedToken, actualToken);

        // 捕获并验证 JwtEncoderParameters
        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder, times(1)).encode(captor.capture());

        JwtEncoderParameters capturedParams = captor.getValue();
        JwtClaimsSet claims = capturedParams.getClaims();

        // 验证 issuer
        assertEquals("self", claims.getClaim("iss"));

        // 验证 subject
        assertEquals(account, claims.getSubject());

        // 验证 userId claim
        assertEquals(userId, claims.getClaim("userId"));

        // 验证 issuedAt 和 expiresAt
        Instant issuedAt = claims.getIssuedAt();
        Instant expiresAt = claims.getExpiresAt();
        assertNotNull(issuedAt);
        assertNotNull(expiresAt);

        // 验证过期时间约为 252000 秒后
        long durationSeconds = expiresAt.getEpochSecond() - issuedAt.getEpochSecond();
        assertEquals(252000L, durationSeconds);
    }

    @Test
    @DisplayName("测试 token(User) 方法 - 不同用户数据")
    void testTokenWithUser_DifferentUserData_ShouldGenerateJwtWithCorrectClaims() {
        // Given
        Long userId = 999L;
        String account = "anotheruser";
        String expectedToken = "different.jwt.token";

        User user = User.builder()
                .id(userId)
                .account(account)
                .nickname("Another User")
                .email("test@example.com")
                .build();

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn(expectedToken);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // When
        String actualToken = tokenService.token(user);

        // Then
        assertEquals(expectedToken, actualToken);

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(captor.capture());

        JwtClaimsSet claims = captor.getValue().getClaims();

        assertEquals("self", claims.getClaim("iss"));
        assertEquals(account, claims.getSubject());
        assertEquals(userId, claims.getClaim("userId"));
    }

    @Test
    @DisplayName("测试 token(Authentication) 方法 - 验证 encoder.encode 被正确调用")
    void testTokenWithAuthentication_ShouldCallEncoderEncode() {
        // Given
        when(authentication.getName()).thenReturn("user");
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.getPrincipal()).thenReturn("principal");

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // When
        tokenService.token(authentication);

        // Then
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    @DisplayName("测试 token(User) 方法 - 验证 encoder.encode 被正确调用")
    void testTokenWithUser_ShouldCallEncoderEncode() {
        // Given
        User user = User.builder()
                .id(1L)
                .account("user")
                .build();

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // When
        tokenService.token(user);

        // Then
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }
}