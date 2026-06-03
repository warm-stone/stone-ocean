package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.entity.fishing.Game;
import com.example.stoneocean.mapper.GameMapper;
import com.example.stoneocean.service.IGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author warmstone
 * @since 2025-11-06
 */
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements IGameService {

    @Override
    public Page<Game> getGamesByPage(int page, int size, String type) {
        Page<Game> gamePage = new Page<>(page, size);
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("type", type);
        }
        queryWrapper.isNull("deleted_time");
        return baseMapper.selectPage(gamePage, queryWrapper);
    }
}
