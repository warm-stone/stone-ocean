package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.mapper.UserMapper;
import com.example.stoneocean.service.IUserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/*
 * 认证侧用户管理
 * 用户数据管理
 * */
@Service
public class DBUserDetailsManagerService extends ServiceImpl<UserMapper, User>
        implements IUserService, // 数据库操作
        UserDetailsService, UserDetailsPasswordService // 用户认证
{
    private final PasswordEncoder passwordEncoder;
    public DBUserDetailsManagerService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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

    @Override
    public boolean save(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        }
        return super.save(user);
    }

    @Override
    public boolean updateById(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        return super.updateById(user);
    }

    // ###################################################################
    // 用户认证

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", username);
        User user = baseMapper.selectOne(queryWrapper);
        if (user == null || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found");
        }

        return user;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        throw new RuntimeException("未实现");
    }
}
