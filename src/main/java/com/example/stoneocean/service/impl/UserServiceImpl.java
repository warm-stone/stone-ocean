package com.example.stoneocean.service.impl;

import com.example.stoneocean.entity.User;
import com.example.stoneocean.mapper.UserMapper;
import com.example.stoneocean.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final DBUserDetailsManagerService dbUserDetailsManagerService;

    public UserServiceImpl(DBUserDetailsManagerService dbUserDetailsManagerService) {
        this.dbUserDetailsManagerService = dbUserDetailsManagerService;
    }

    @Override
    public void saveUserDetails(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withDefaultPasswordEncoder()
                .username(user.getUserAccount())
                .password(user.getPasswordHash())
                .build();
        dbUserDetailsManagerService.createUser(userDetails);
    }
}
