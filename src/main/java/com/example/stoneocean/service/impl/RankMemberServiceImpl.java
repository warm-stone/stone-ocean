package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stoneocean.entity.RankMember;
import com.example.stoneocean.mapper.RankMemberMapper;
import com.example.stoneocean.service.IRankMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<RankMember> getByParentId(Long parentId) {
        QueryWrapper<RankMember> wrapper = new QueryWrapper<>();
        wrapper.isNull("deleted_time");
        wrapper.eq("parent_id", parentId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<RankMember> getByRankListId(Long rankListId) {
        QueryWrapper<RankMember> wrapper = new QueryWrapper<>();
        wrapper.isNull("deleted_time");
        wrapper.eq("rank_list_id", rankListId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public int addScoreSum(Long id, int voteCount) {
        return baseMapper.addScoreSum(id, voteCount);
    }
}
