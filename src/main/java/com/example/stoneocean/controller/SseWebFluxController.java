package com.example.stoneocean.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class SseWebFluxController {

    private static final Logger log = LoggerFactory.getLogger(SseWebFluxController.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * WebFlux 实现的 SSE 接口（非阻塞，高并发友好）
     */
    @GetMapping(value = "/sse/webflux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sseWebFlux(
            @RequestParam String userId,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId
    ) {
        // 打印重连信息
        if (lastEventId != null) {
            log.info("用户 {} 从消息ID={} 处重连", userId, lastEventId);
        }

        // 每3秒生成一条消息，通过Flux流推送
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(3))
                .map(sequence -> {
                    long currentTime = System.currentTimeMillis();
                    // 普通消息（无事件名）
                    if (sequence % 3 != 0) {  // 每3条消息中2条普通消息
                        return buildSseMessage(
                                null,
                                currentTime + "",
                                "用户 " + userId + "，当前时间：" + LocalDateTime.now().format(FORMATTER)
                        );
                    } else {  // 每3条消息中1条"notice"事件
                        return buildSseMessage(
                                "notice",
                                currentTime + "",
                                "【系统通知】用户 " + userId + "，这是WebFlux推送的通知"
                        );
                    }
                });
    }

    // 复用SSE消息构建方法（同MVC方式）
    private String buildSseMessage(String event, String id, String data) {
        StringBuilder sb = new StringBuilder();
        if (event != null) {
            sb.append("event: ").append(event).append("\n");
        }
        if (id != null) {
            sb.append("id: ").append(id).append("\n");
        }
        String[] lines = data.split("\n");
        for (String line : lines) {
            sb.append("data: ").append(line).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}