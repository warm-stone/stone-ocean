package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.entity.RankMember;
import com.example.stoneocean.service.IRankListService;
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
    private final IRankMemberService service;
    private final IRankListService rankListService;

    public RankMemberController(IRankMemberService rankMemberService, IRankListService rankListService) {
        this.service = rankMemberService;
        this.rankListService = rankListService;
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
        // 校验父榜单归属：仅允许向自己创建的榜单下添加投票项
        Long rankListId = resolveRankListId(rankMember);
        if (rankListId == null) {
            return ApiResponse.failed("无法确定父榜单，请指定 rankListId 或 parentId");
        }
        RankList rankList = rankListService.getById(rankListId);
        if (rankList == null || rankList.getCreator() == null || !rankList.getCreator().equals(userId)) {
            return ApiResponse.failed("无权操作：榜单不属于当前用户");
        }
        rankMember.setCreator(userId);
        boolean save = service.save(rankMember);
        return ApiResponse.success(save);
    }

    /**
     * 解析投票项所属榜单 id：优先取自身 rankListId，否则沿 parentId 向上递归查找。
     */
    private Long resolveRankListId(RankMember rankMember) {
        if (rankMember.getRankListId() != null) {
            return rankMember.getRankListId();
        }
        Long parentId = rankMember.getParentId();
        int depth = 0;
        while (parentId != null && depth++ < 100) {
            RankMember parent = service.getById(parentId);
            if (parent == null) {
                break;
            }
            if (parent.getRankListId() != null) {
                return parent.getRankListId();
            }
            parentId = parent.getParentId();
        }
        return null;
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
