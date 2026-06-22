package com.example.stoneocean.controller;

import com.example.stoneocean.Util.Tools;
import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.VoteRecord;
import com.example.stoneocean.entity.dto.VoteRecordSumDTO;
import com.example.stoneocean.service.IRankMemberService;
import com.example.stoneocean.service.IVoteRecordService;
import org.springframework.dao.DuplicateKeyException;
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
     * 使用事务 + (rank_member_id, creator, vote_date) 唯一约束保证投票并发安全：
     * 首次投票 INSERT 若因并发触发唯一约束冲突，捕获 DuplicateKeyException 后重试一次走 UPDATE 路径。
     * 每日投票值（合并后）应当小于 1，依赖更新后总值限制每日投票数。
     * */
    @PostMapping("/vote")
    @Transactional
    public ApiResponse<Boolean> voteToRankMember(@RequestBody VoteRecord voteRecord, Authentication authentication) {
        Integer voteCount = voteRecord.getVoteCount();
        if (voteCount == null || voteCount == 0) return ApiResponse.failed("投票数不可为空");
        if (Math.abs(voteCount) > 1) return ApiResponse.failed("投票值应当小于 1");

        // 添加今日投票数据
        Long userId = (Long) ((Jwt) authentication.getPrincipal()).getClaims().get("userId");
        Long rankMemberId = voteRecord.getRankMemberId();
        boolean ret = false;
        for (int attempt = 0; attempt < 2; attempt++) {
            VoteRecord lastRecord = service.selectLastByRankMemberIdAndCreatorIdForUpdate(rankMemberId, userId);
            if (lastRecord != null &&
                    lastRecord.getCreatedTime().isAfter(tool.localTime().toLocalDate().atStartOfDay())) {
                voteRecord.setVoteCount(voteCount + lastRecord.getVoteCount());
                voteRecord.setId(lastRecord.getId());
                voteRecord.setUpdatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                voteRecord.setModifier(userId);

                if (Math.abs(voteRecord.getVoteCount()) > 1) return ApiResponse.failed("每日投票值应当小于 1");
                ret = service.updateById(voteRecord);
                break;
            } else {
                voteRecord.setCreator(userId);
                try {
                    ret = service.save(voteRecord);
                    break;
                } catch (DuplicateKeyException e) {
                    // 并发竞态：另一请求已插入今日记录，重试一次走 UPDATE 路径
                    if (attempt == 1) throw e;
                }
            }
        }
        rankMemberService.addScoreSum(rankMemberId, voteCount);
        return ApiResponse.success(ret);
    }
}
