package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stoneocean.entity.ThirdPartyAccount;
import com.example.stoneocean.mapper.ThirdPartyAccountMapper;
import com.example.stoneocean.service.IThirdPartyAccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author warmstone
 * @since 2025-08-19
 */
@Service
public class ThirdPartyAccountServiceImpl extends ServiceImpl<ThirdPartyAccountMapper, ThirdPartyAccount> implements IThirdPartyAccountService {

    @Override
    public ThirdPartyAccount getByThirdId(String thirdId, String registrationId) {

        QueryWrapper<ThirdPartyAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("third_id", thirdId);
        queryWrapper.eq("account_type", registrationId);

        return baseMapper.selectOne(queryWrapper);
    }
}
