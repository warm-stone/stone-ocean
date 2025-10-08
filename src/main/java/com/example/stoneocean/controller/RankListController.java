package com.example.stoneocean.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.service.IRankListService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author warmstone
 * @since 2025-07-31
 */
@RestController
@RequestMapping("/rankList")
public class RankListController {
    private final IRankListService service;

    public RankListController(IRankListService rankListService) {
        this.service = rankListService;
    }

    @GetMapping("/page")
    public ApiResponse<Page<RankList>> getRankList(@RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size
    ) {

        return ApiResponse.success(service.getRankListByPage(page, size));
    }

    @PostMapping("/add")
    public boolean addRankList(@RequestBody RankList rankList, Authentication authentication) {

        Long userId = (Long) ((Jwt)authentication.getCredentials()).getClaims().get("userId");
        rankList.setCreator(userId);
        return service.save(rankList);
    }
}
