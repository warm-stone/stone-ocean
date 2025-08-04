package com.example.stoneocean.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.service.IRankListService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
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
    public Page<RankList> getRankList(@RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      Authentication authentication) {
        String s =  "Hello, " + authentication.getName() + "!";
        System.out.println("s = " + s);

        return service.getRankListByPage(page, size);
    }

    @PostMapping("/add")
    public int addRankList(@RequestBody RankList rankList) {
        return service.addRankList(rankList);
    }
}
