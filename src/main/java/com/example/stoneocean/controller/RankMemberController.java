package com.example.stoneocean.controller;

import com.example.stoneocean.entity.RankList;
import com.example.stoneocean.service.IRankMemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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



}
