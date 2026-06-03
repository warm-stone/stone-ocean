package com.example.stoneocean.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.entity.fishing.Game;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author warmstone
 * @since 2025-11-06
 */
public interface IGameService extends IService<Game> {

    Page<Game> getGamesByPage(int page, int size, String type);
}
