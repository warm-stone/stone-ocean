package com.example.stoneocean.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.entity.RankList;
import com.baomidou.mybatisplus.extension.service.IService;

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

    int addRankList(RankList rankList);
}
