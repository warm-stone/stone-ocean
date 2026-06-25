package com.example.stoneocean.config;

import com.example.stoneocean.entity.User;
import com.example.stoneocean.service.IUserService;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * 校验 JWT 中的 tokenVersion 是否与数据库一致，不一致则令牌失效（撤销机制）。
 * 缺少 userId claim 的令牌跳过校验（次要签发路径，按原样放行）。
 * 缺少 tokenVersion claim 的旧令牌视为版本 0。
 */
public class TokenVersionValidator implements OAuth2TokenValidator<Jwt> {

    private final IUserService userService;

    public TokenVersionValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        Object userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            return OAuth2TokenValidatorResult.success();
        }
        Long userId = ((Number) userIdClaim).longValue();
        User user = userService.getById(userId);
        if (user == null) {
            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "用户不存在", null));
        }
        int dbVersion = user.getTokenVersion() == null ? 0 : user.getTokenVersion();
        Object tokenVersionClaim = jwt.getClaim("tokenVersion");
        int tokenVersion = tokenVersionClaim == null ? 0 : ((Number) tokenVersionClaim).intValue();
        if (dbVersion != tokenVersion) {
            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "令牌已失效，请重新登录", null));
        }
        return OAuth2TokenValidatorResult.success();
    }
}
