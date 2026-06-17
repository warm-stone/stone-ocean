package com.example.stoneocean.service;

import com.example.stoneocean.config.SpringContextHolder;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 端点处理类
 */
@ServerEndpoint("/ws/{userId}")
@Component
public class WebSocketServer {

    // 静态变量，记录当前在线连接数（线程安全）
    private static int onlineCount = 0;

    // 存储所有在线的 WebSocket 连接（线程安全的 Set）
    private static final CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    // 当前连接的会话（与客户端的连接会话，用于发送数据）
    private Session session;

    // 当前连接的用户 ID
    private String userId;

    /**
     * 连接建立成功时调用
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;

        // JWT 认证校验
        String token = session.getRequestParameterMap().getOrDefault("token", List.of()).stream().findFirst().orElse(null);
        if (token == null || token.isEmpty()) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "缺少认证token"));
            } catch (IOException ignored) {}
            return;
        }

        try {
            JwtDecoder decoder = SpringContextHolder.getBean(JwtDecoder.class);
            var jwt = decoder.decode(token);
            Long jwtUserId = (Long) jwt.getClaims().get("userId");
            if (jwtUserId == null || !jwtUserId.toString().equals(userId)) {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "用户身份不匹配"));
                return;
            }
        } catch (Exception e) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "token验证失败"));
            } catch (IOException ignored) {}
            return;
        }

        // 将当前连接加入集合
        webSocketSet.add(this);
        // 在线人数 +1
        addOnlineCount();
        System.out.println("用户 " + userId + " 连接成功，当前在线人数：" + getOnlineCount());

        try {
            // 向客户端发送连接成功消息
            sendMessage("连接成功！目前在线人数：" + getOnlineCount());
        } catch (IOException e) {
            System.out.println("发送消息失败：" + e.getMessage());
        }
    }

    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose() {
        // 从集合中移除当前连接
        webSocketSet.remove(this);
        // 在线人数 -1
        subOnlineCount();
        System.out.println("用户 " + userId + " 断开连接，当前在线人数：" + getOnlineCount());
    }

    /**
     * 收到客户端消息时调用
     *
     * @param message 客户端发送的消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到用户 " + userId + " 的消息：" + message);

        // 示例：将消息广播给所有在线客户端
        for (WebSocketServer webSocket : webSocketSet) {
            try {
                // 转发消息（格式：[发送者ID]：消息内容）
                webSocket.sendMessage("[" + userId + "]：" + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     */
    public void sendMessage(String message) throws IOException {
        // 通过会话发送文本消息
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发消息（静态方法，外部可调用）
     */
    public static void broadcast(String message) {
        for (WebSocketServer webSocket : webSocketSet) {
            try {
                webSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 点对点发送消息（根据 userId 发送给指定用户）
     */
    public static void sendToUser(String userId, String message) {
        for (WebSocketServer webSocket : webSocketSet) {
            if (webSocket.userId.equals(userId)) {
                try {
                    webSocket.sendMessage("[系统通知]：" + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // 在线人数相关的同步方法（线程安全）
    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}