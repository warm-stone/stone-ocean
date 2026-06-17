package com.example.stoneocean.controller;

import com.example.stoneocean.Util.Tools;
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
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/voteRecord")
public class VoteRecordController {
    private final Tools tool;
    private final IVoteRecordService service;
    private final IRankMemberService rankMemberService;

    public VoteRecordController(
            Tools tool,
            IVoteRecordService voteRecordService,
            IRankMemberService rankMemberService) {
        this.tool = tool;
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
        Long userId = (Long) ((Jwt) authentication.getPrincipal()).getClaims().get("userId");
        VoteRecord lastRecord = service.selectLastByRankMemberIdAndCreatorId(voteRecord.getRankMemberId(), userId);
        boolean ret;
        if (lastRecord != null &&
                lastRecord.getCreatedTime().isAfter(tool.localTime().toLocalDate().atStartOfDay())) {
            voteRecord.setVoteCount(voteCount + lastRecord.getVoteCount());
            voteRecord.setId(lastRecord.getId());
            voteRecord.setUpdatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            voteRecord.setModifier(userId);

            if (Math.abs(voteRecord.getVoteCount()) > 1) return ApiResponse.failed("每日投票值应当小于 1");
            ret = service.updateById(voteRecord);
        } else {
            voteRecord.setCreator(userId);
            ret = service.save(voteRecord);
        }
        rankMemberService.addScoreSum(voteRecord.getRankMemberId(), voteCount);
        return ApiResponse.success(ret);
    }
}
