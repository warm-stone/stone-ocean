package com.example.stoneocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.stoneocean.Util.NotContains;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */

@TableName("t_user")
@ApiModel(value = "User对象", description = "")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录账号
     */
    @NotBlank
    @NotContains(values = {"-", "|", "~"})
    @ApiModelProperty("登录账号")
    private String account;

    /**
     * 密码哈希
     */
    @ApiModelProperty("密码哈希")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    /**
     * 原始密码（仅用于接收前端输入，不持久化）
     */
    @TableField(exist = false)
    @JsonIgnore
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 原密码（仅用于接收前端输入，修改密码时校验，不持久化）
     */
    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassword;

    /**
     * Token 版号，用于服务端撤销令牌（改密码/登出时 +1 使旧令牌失效）
     */
    private Integer tokenVersion;

    /**
     * 角色，默认 USER
     */
    private String role;

    private String email;

    private String phone;

    /**
     * 用户显示名
     */
    @NotBlank
    @ApiModelProperty("用户显示名")
    private String nickname;

    private String sex;

    private String des;

    private String avatarUrl;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    /**
     * 删除时间，NULL 表示未删除
     */
    @ApiModelProperty("删除时间，NULL 表示未删除")
    private LocalDateTime deletedTime;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + (role == null ? "USER" : role).toUpperCase()));
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return this.password != null ? this.password : this.getPasswordHash();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.getAccount();
    }
}
