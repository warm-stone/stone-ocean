package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.mapper.UserMapper;
import com.example.stoneocean.service.IUserService;
import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 服务实现类 数据库侧用户管理
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
//@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User getByAccount(@NotNull String account) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public User getByNickname(@NotNull String nickname) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", nickname);
        return baseMapper.selectOne(queryWrapper);
    }
}
