package com.example.stoneocean.service.impl;

import com.example.stoneocean.entity.User;
import com.example.stoneocean.mapper.UserMapper;
import com.example.stoneocean.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
