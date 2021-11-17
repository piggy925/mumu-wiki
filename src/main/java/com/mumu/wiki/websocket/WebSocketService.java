package com.mumu.wiki.websocket;

import org.jboss.logging.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WebSocketService {
    @Resource
    WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String info, String logId) {
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(info);
    }
}