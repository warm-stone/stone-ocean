package com.example.stoneocean.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.stoneocean.Util.Tools;
import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.entity.fishing.Game;
import com.example.stoneocean.service.IGameService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author warmstone
 * @since 2025-11-06
 */
@RestController
@RequestMapping("/game")
public class GameController {
    private final Tools tool;
    private final IGameService service;

    public GameController(Tools tool, IGameService gameService) {
        this.tool = tool;
        this.service = gameService;
    }

    @GetMapping("/page")
    public ApiResponse<Page<Game>> getGames(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "type", required = false) String type
            ) {
        return ApiResponse.success(service.getGamesByPage(page, size, type));
    }

    @GetMapping("/member/{id}")
    public ApiResponse<Game> getGameById(@PathVariable Long id) {
        if (id == null) return ApiResponse.failed("错误的参数");
        Game game = service.getById(id);
        return ApiResponse.success(game);
    }

    @PostMapping("/add")
    public ApiResponse<Game> addGame(@RequestBody Game game, Authentication authentication) {

        Long userId = (Long) ((Jwt)authentication.getCredentials()).getClaims().get("userId");
        game.setCreator(userId);
        boolean flag = service.save(game);
        return ApiResponse.byFlag(flag, game);
    }

    @PostMapping("/delete/{id}")
    public ApiResponse<Game> deleteGame(@PathVariable Long id, Authentication authentication) {

        if (id == null) return ApiResponse.failed("错误的参数");
        Game game = service.getById(id);
        Long userId = (Long) ((Jwt)authentication.getCredentials()).getClaims().get("userId");
        if (!userId.equals(game.getCreator())) {
            return ApiResponse.failed("非法操作");
        }
        game.setDeletedTime(tool.localTime());
        boolean flag = service.updateById(game);
        return ApiResponse.byFlag(flag, game);
    }
}
