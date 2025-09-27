package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.VoteRecord;
import com.example.stoneocean.entity.dto.VoteRecordSumDTO;
import com.example.stoneocean.service.IRankMemberService;
import com.example.stoneocean.service.IVoteRecordService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/voteRecord")
public class VoteRecordController {
    private final IVoteRecordService service;
    private final IRankMemberService rankMemberService;

    public VoteRecordController(IVoteRecordService voteRecordService,
                                IRankMemberService rankMemberService) {
        this.service = voteRecordService;
        this.rankMemberService = rankMemberService;
    }

    @GetMapping("/statistic/rankMemberId/{rankMemberId}")
    public ApiResponse<List<VoteRecordSumDTO>> getVoteRecordSum(@PathVariable Long rankMemberId) {
        List<VoteRecordSumDTO> voteSumRecord = service.getVoteSumRecord(rankMemberId);
        return ApiResponse.success(voteSumRecord);
    }

    /*
    * 不使用事务，依赖更新后总值限制限制投票数
    * */
    @PostMapping("/vote")
    @Transactional
    public ApiResponse<Boolean> voteToRankMember(@RequestBody VoteRecord voteRecord, Authentication authentication) {
        Integer voteCount = voteRecord.getVoteCount();
        if (voteCount == null || voteCount == 0) return ApiResponse.failed("投票数不可为空");
        if (Math.abs(voteCount) > 1) return ApiResponse.failed("投票值应当小于 1");

        // 添加今日投票数据
        VoteRecord lastRecord = service.selectLastByRankMemberId(voteRecord.getRankMemberId());
        Long userId = (Long) ((Jwt) authentication.getCredentials()).getClaims().get("userId");
        boolean ret;
        if (lastRecord != null &&
                lastRecord.getCreatedTime().isAfter(LocalDateTime.now().toLocalDate().atStartOfDay())) {
            voteRecord.setVoteCount(voteCount + lastRecord.getVoteCount());
            voteRecord.setId(lastRecord.getId());
            voteRecord.setUpdatedTime(LocalDateTime.now());
            voteRecord.setModifier(userId);

            if (Math.abs(voteRecord.getVoteCount()) > 1) return ApiResponse.failed("每日投票值应当小于 1");
            ret = service.updateById(voteRecord);
        }
        else {
            voteRecord.setCreator(userId);
            ret = service.save(voteRecord);
        }
        rankMemberService.addScoreSum(voteRecord.getRankMemberId(), voteCount);
        return ApiResponse.success(ret);
    }
}
