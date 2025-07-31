package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.mapper.RankListMapper;
import com.example.stoneocean.service.IRankListService;
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
public class RankListServiceImpl extends ServiceImpl<RankListMapper, RankList> implements IRankListService {
    private RankListMapper mapper;

    public RankListServiceImpl(RankListMapper mapper) {
        this.mapper = mapper;
    }


    public Page<RankList> getRankListByPage(int currentPage, int pageSize) {
        Page<RankList> page = new Page<>(currentPage, pageSize);
        QueryWrapper<RankList> queryWrapper = new QueryWrapper<>();
        // 可以在这里添加你的查询条件
        return mapper.selectPage(page, queryWrapper);
    }
    public int addRankList(RankList rankList) {
        return mapper.insert(rankList);
    }
}
