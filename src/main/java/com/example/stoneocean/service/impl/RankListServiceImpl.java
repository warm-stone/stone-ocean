package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.mapper.RankListMapper;
import com.example.stoneocean.service.IRankListService;
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


    public Page<RankList> getRankListByPage(int currentPage, int pageSize) {
        Page<RankList> page = new Page<>(currentPage, pageSize);
        QueryWrapper<RankList> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_time");
        return baseMapper.selectPage(page, queryWrapper);
    }
}
