package com.example.stoneocean.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.stoneocean.entity.VoteRecord;
import com.example.stoneocean.entity.dto.VoteRecordSumDTO;

import java.util.List;

public interface IVoteRecordService extends IService<VoteRecord> {

    List<VoteRecordSumDTO> getVoteSumRecord(Long rankMemberId);

    VoteRecord selectLastByRankMemberId(Long rankMemberId);
    VoteRecord selectLastByRankMemberIdAndCreatorId(Long rankMemberId, Long creatorId);
    VoteRecord selectLastByRankMemberIdAndCreatorIdForUpdate(Long rankMemberId, Long creatorId);
}
