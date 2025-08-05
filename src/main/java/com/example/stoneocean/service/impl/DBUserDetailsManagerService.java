package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.stoneocean.entity.User;
import com.example.stoneocean.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class DBUserDetailsManagerService implements UserDetailsManager, UserDetailsPasswordService {

    private final UserMapper userMapper;

    public DBUserDetailsManagerService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", username);
        User user = userMapper.selectOne(queryWrapper);
        return user != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("user '" + username + "' not found");
        }

        return user;
    }

}
