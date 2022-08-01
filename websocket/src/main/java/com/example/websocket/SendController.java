package com.example.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    WebSocketServer webSocketServer;

    @RequestMapping("ws")
    public void wsSendTest(String message) {
        webSocketServer.onMessageOne(message);
    }
}
