package com.example.stoneocean.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.stoneocean.entity.RankMember;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
public interface IRankMemberService extends IService<RankMember> {

    List<RankMember> getByParentId(@NotNull Long parentId);

    List<RankMember> getByRankListId(@NotNull Long rankListId);

    int addScoreSum(Long id, int voteCount);

}
