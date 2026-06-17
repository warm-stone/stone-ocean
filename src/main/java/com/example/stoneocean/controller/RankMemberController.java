package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.RankMember;
import com.example.stoneocean.service.IRankMemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@RestController
@RequestMapping("/rankMember")
public class RankMemberController {
    private IRankMemberService service;

    public RankMemberController(IRankMemberService rankMemberService) {
        this.service = rankMemberService;
    }

    @PostMapping("/add")
    public ApiResponse<Boolean> addRankMember(@RequestBody RankMember rankMember, Authentication authentication) {
        // 新增数据票数应该为 0
        Assert.isTrue(rankMember.getScoreSum() == null || rankMember.getScoreSum() == 0,
                "初始票数应该为空");
        Assert.isTrue(rankMember.getScoreCalculate() == null || rankMember.getScoreCalculate() == 0,
                "初始票数应该为空");
        Assert.isTrue(rankMember.getParentId() != null || rankMember.getRankListId() != null,
                "父级不应为空");
        Long userId = (Long) ((Jwt) authentication.getPrincipal()).getClaims().get("userId");
        rankMember.setCreator(userId);
        boolean save = service.save(rankMember);
        return ApiResponse.success(save);
    }

    /*
    * 根据投票项 id 获取子投票项
    * */
    @GetMapping("/subMember/{id}")
    public ApiResponse<List<RankMember>> getSubMember(@PathVariable Long id) {
        List<RankMember> rankMembers = service.getByParentId(id);
        return ApiResponse.success(rankMembers);
    }

    /*
    * 根据列表 id 获取投票项
    *
    * */
    @GetMapping("/member/{rankListId}")
    public ApiResponse<List<RankMember>> getMember(@PathVariable Long rankListId) {
        List<RankMember> byRankListId = service.getByRankListId(rankListId);
        return ApiResponse.success(byRankListId);
    }


}
