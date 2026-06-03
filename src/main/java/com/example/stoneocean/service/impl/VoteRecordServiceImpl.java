package com.example.stoneocean.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stoneocean.entity.VoteRecord;
import com.example.stoneocean.entity.dto.VoteRecordSumDTO;
import com.example.stoneocean.mapper.VoteRecordMapper;
import com.example.stoneocean.service.IVoteRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteRecordServiceImpl extends ServiceImpl<VoteRecordMapper, VoteRecord> implements IVoteRecordService {


    public List<VoteRecordSumDTO> getVoteSumRecord(Long rankMemberId) {
        return baseMapper.selectVoteSumByRankMemberId(rankMemberId);
    }

    public VoteRecord selectLastByRankMemberId(Long rankMemberId) {
        return baseMapper.selectLastByRankMemberId(rankMemberId);
    }

    public VoteRecord selectLastByRankMemberIdAndCreatorId(Long rankMemberId, Long creatorId) {

        return baseMapper.selectLastByRankMemberIdAndCreatorId(rankMemberId, creatorId);
    }
}
