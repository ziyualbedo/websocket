package com.example.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author 李佳鑫（ZiYu）
 * @createTime 2022/7/22 16:06
 * @param
 * @return
 */
@ServerEndpoint("/test")
@Component
@Slf4j
public class WebSocketServer {
    //存放所有在线的客户端
    public static Map<String, Session> clients = new ConcurrentHashMap<>();

    //客户端连接
    @OnOpen
    public void onOpen(Session session) {
        log.info("有新客户端连接了：{}", session.getId());
        //将新用户存入在线的组
        clients.put(session.getId(), session);
        session.getAsyncRemote().sendText("连接成功，session id:" +  session.getId());
    }

    //客户端断开
    @OnClose
    public void onClose(Session session) {
        log.info("有用户断开连接了：{}", session.getId());
        clients.remove(session.getId());
    }

    //报错
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    //收到客户端发来消息
//    @OnMessage
//    public void onMessage(String message) {
//        log.info("收到客户端发来消息：{}", message);
//        this.sendAll(message);
//    }

    @OnMessage
    public void onMessageOne(String message) {
        this.sendOne(JSONObject.parseObject(message, WebSocketMessage.class));
//        this.sendAll(message);
    }

    //单发消息
    private void sendOne(WebSocketMessage message) {
        Session session = clients.get(message.getUserId());
        if (session != null) {
                session.getAsyncRemote().sendText(message.getMessage());
        }
    }

    //群发消息
    private void sendAll(String message) {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            sessionEntry.getValue().getAsyncRemote().sendText(message);
        }
    }

}
