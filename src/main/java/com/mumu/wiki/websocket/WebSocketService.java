package com.mumu.wiki.websocket;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WebSocketService {
    @Resource
    WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String info) {
        webSocketServer.sendInfo(info);
    }
}