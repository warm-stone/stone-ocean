package com.example.stoneocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
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
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录账号
     */
    @ApiModelProperty("登录账号")
    private String userAccount;

    /**
     * 密码哈希
     */
    @ApiModelProperty("密码哈希")
    private String passwordHash;

    private String email;

    private String phone;

    /**
     * 用户显示名
     */
    @ApiModelProperty("用户显示名")
    private String nickname;

    private String sex;

    private String des;

    private String avatarUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 删除时间，NULL 表示未删除
     */
    @ApiModelProperty("删除时间，NULL 表示未删除")
    private LocalDateTime deletedAt;

    @Override
    public String toString() {
        return "User{" +
                "id = " + id +
                ", userAccount = " + userAccount +
                ", passwordHash = " + passwordHash +
                ", email = " + email +
                ", phone = " + phone +
                ", nickname = " + nickname +
                ", sex = " + sex +
                ", des = " + des +
                ", avatarUrl = " + avatarUrl +
                ", createdAt = " + createdAt +
                ", updatedAt = " + updatedAt +
                ", deletedAt = " + deletedAt +
                "}";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.getUserAccount();
    }
}
