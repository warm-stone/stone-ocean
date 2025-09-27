package com.example.stoneocean.service;

import com.example.stoneocean.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@Validated
public interface IUserService extends IService<User> {

    User getByAccount(@NotNull String account);

    User getByNickname(@NotNull String nickname);

}
