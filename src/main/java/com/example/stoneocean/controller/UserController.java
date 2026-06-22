package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.entity.dto.AuthorizationDTO;
import com.example.stoneocean.service.ITokenService;
import com.example.stoneocean.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserController(IUserService userService,
                          ITokenService tokenService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    private static Long getUserId(Authentication authentication) {
        Long userId;
        if (authentication.getPrincipal() instanceof User) {
            userId = ((User) authentication.getPrincipal()).getId();
        }
        else {
            userId = (Long) ((Jwt) authentication.getPrincipal()).getClaims().get("userId");
        }
        Assert.isTrue (userId != -1L, "用户信息获取失败");
        return userId;
    }

    @PostMapping("/add")
    public ApiResponse<AuthorizationDTO> add(@Valid @RequestBody User user) {
        User byAccount = userService.getByAccount(user.getAccount());
        if (byAccount != null) {
            return ApiResponse.failed("账户已被注册");
        }
        User byNickname = userService.getByNickname(user.getNickname());
        if (byNickname != null) {
            return ApiResponse.failed("昵称已被使用");
        }
        if (user.getEmail() != null && user.getEmail().isEmpty()) {
            user.setEmail(null);
        }
        boolean save = userService.save(user);
        if (!save) {
            return ApiResponse.failed("注册失败");
        }
        // 防止id未自动填充，手动填充 id
        user = userService.getByAccount(user.getAccount());
        String token = this.tokenService.token(user);
        return ApiResponse.success(new AuthorizationDTO(token, user));
    }

    @PostMapping("/selfInfo")
    public ApiResponse<User> getSelfInfo(Authentication authentication) {
        Long userId = getUserId(authentication);
        User user = userService.getById(userId);
        user.setPasswordHash(""); // 保护性置空
        return ApiResponse.success(user);
    }


    @PostMapping("/modify")
    public ApiResponse<AuthorizationDTO> modify(Authentication authentication, @Valid @RequestBody User user) {
        Long userId = getUserId(authentication);
        if (user.getId() == null) user.setId(userId);
        Assert.isTrue(Objects.equals(userId, user.getId()), "鉴权错误");
        User byAccount = userService.getByAccount(user.getAccount());
        if (byAccount != null && !Objects.equals(byAccount.getId(), user.getId())) {
            return ApiResponse.failed("账户已被注册");
        }
        User byNickname = userService.getByNickname(user.getNickname());
        if (byNickname != null && !Objects.equals(byNickname.getId(), user.getId())) {
            return ApiResponse.failed("昵称已被使用");
        }
        if (user.getEmail() != null && user.getEmail().isEmpty()) {
            user.setEmail(null);
        }
        // 是否修改密码：新密码通过 passwordHash 字段传入（@JsonProperty WRITE_ONLY）
        boolean changingPassword = user.getPasswordHash() != null && !user.getPasswordHash().isEmpty();
        if (changingPassword) {
            User dbUser = userService.getById(userId);
            if (user.getOldPassword() == null || user.getOldPassword().isEmpty()) {
                return ApiResponse.failed("请输入原密码");
            }
            if (!passwordEncoder.matches(user.getOldPassword(), dbUser.getPasswordHash())) {
                return ApiResponse.failed("原密码错误");
            }
            // 版号 +1，使此前签发的所有令牌失效
            int currentVersion = dbUser.getTokenVersion() == null ? 0 : dbUser.getTokenVersion();
            user.setTokenVersion(currentVersion + 1);
        }
        boolean flag = userService.updateById(user);
        if (!flag) {
            return ApiResponse.failed("修改失败");
        }
        // 重新查询以获取最新 tokenVersion，保证新令牌携带正确版号
        User updated = userService.getById(userId);
        String token = this.tokenService.token(updated);
        return ApiResponse.success(new AuthorizationDTO(token, updated));
    }


    @PostMapping("/member/{userId}")
    public ApiResponse<User> getById(@PathVariable Long userId) {
        User user = userService.getById(userId);
        user.setPasswordHash(""); // 保护性置空
        return ApiResponse.success(user);
    }
}
