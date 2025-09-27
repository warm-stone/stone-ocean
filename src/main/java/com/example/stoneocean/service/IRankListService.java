package com.example.stoneocean.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.stoneocean.entity.RankList;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
public interface IRankListService extends IService<RankList> {

    /*
    * 分页获取排行榜
    * */
    Page<RankList> getRankListByPage(int currentPage, int pageSize);

}
