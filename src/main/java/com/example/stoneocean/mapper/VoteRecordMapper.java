package com.example.stoneocean.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stoneocean.entity.VoteRecord;
import com.example.stoneocean.entity.dto.VoteRecordSumDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VoteRecordMapper extends BaseMapper<VoteRecord> {

    @Select("SELECT creator, SUM(vote_count) AS voteCount " +
            "FROM t_vote4fun_vote_record " +
            "WHERE rank_member_id = #{rankMemberId} " +
            "GROUP BY creator " +
            "ORDER BY voteCount DESC")
    List<VoteRecordSumDTO> selectVoteSumByRankMemberId(@Param("rankMemberId") Long rankMemberId);

    @Select("SELECT * " +
            "FROM t_vote4fun_vote_record " +
            "WHERE rank_member_id = #{rankMemberId} " +
            "ORDER BY created_time DESC " +
            "LIMIT 1")
    VoteRecord selectLastByRankMemberId(@Param("rankMemberId") Long rankMemberId);

    @Select("SELECT * " +
            "FROM t_vote4fun_vote_record " +
            "WHERE rank_member_id = #{rankMemberId} " +
            "AND creator = #{creatorId} " +
            "ORDER BY created_time DESC " +
            "LIMIT 1")
    VoteRecord selectLastByRankMemberIdAndCreatorId(@Param("rankMemberId") Long rankMemberId
    , @Param("creatorId") Long creatorId);

    @Select("SELECT * " +
            "FROM t_vote4fun_vote_record " +
            "WHERE rank_member_id = #{rankMemberId} " +
            "AND creator = #{creatorId} " +
            "ORDER BY created_time DESC " +
            "LIMIT 1 " +
            "FOR UPDATE")
    VoteRecord selectLastByRankMemberIdAndCreatorIdForUpdate(@Param("rankMemberId") Long rankMemberId
    , @Param("creatorId") Long creatorId);

}
