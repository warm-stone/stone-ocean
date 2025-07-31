package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.entity.RankMember;
import com.example.stoneocean.mapper.RankMemberMapper;
import com.example.stoneocean.service.IRankMemberService;
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
public class RankMemberServiceImpl extends ServiceImpl<RankMemberMapper, RankMember> implements IRankMemberService {
    private RankMemberMapper mapper;

    public RankMemberServiceImpl(RankMemberMapper mapper) {
        this.mapper = mapper;
    }

}
