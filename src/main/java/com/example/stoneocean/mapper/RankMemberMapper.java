package com.example.stoneocean.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stoneocean.entity.RankMember;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@Valid
public interface RankMemberMapper extends BaseMapper<RankMember> {


    @Update("UPDATE t_vote4fun_rank_member " +
            "SET score_sum = score_sum + #{voteCount} " +
            "WHERE id = #{id}")
    int addScoreSum(@NotNull @Param("id") Long id, int voteCount);
}
