package com.mule.demo.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

 /**
 * WebSocket 服务端
 * 监听地址: ws://localhost:8080/ws/{projectId}/{userId}
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{projectId}/{userId}")
public class WebSocketServer {
    // 静态变量，用来记录所有在线连接。
    // Key: projectId (项目ID)
    // Value: 这个项目下的所有 WebSocket 连接 (Session)
    private static final ConcurrentHashMap<Long, CopyOnWriteArraySet<Session>> PROJECT_SESSIONS = new
      ConcurrentHashMap<>();
      /*
      连接成功调用
      */
      @OnOpen
      public void onOpen(Session session, @PathParam("projectId") Long projectId, @PathParam("userId") Long userId) {
          //如果没人看，初始化空集合
          PROJECT_SESSIONS.putIfAbsent(projectId, new CopyOnWriteArraySet<>());
          //将当前连接加入集合
          PROJECT_SESSIONS.get(projectId).add(session);
          log.info("new connection, projectId: {}, userId: {}, current connections:{}",userId,projectId, PROJECT_SESSIONS.get(projectId).size());
      }
      /*
      连接关闭调用
      */
      @OnClose
      public void onClose(Session session, @PathParam("projectId") Long projectId) {
          // 从集合里移除
         if (PROJECT_SESSIONS.containsKey(projectId)) {
             PROJECT_SESSIONS.get(projectId).remove(session);
             if (PROJECT_SESSIONS.get(projectId).isEmpty()) {
                 PROJECT_SESSIONS.remove(projectId); // 如果没人看了，把项目key删掉省内存
             }
         }
         log.info("connection closed, projectId: {}", projectId);

}
        @OnError
         public void onError(Session session, Throwable error) {
          log.error("WebSocket error", error);
}
/*
广播消息
*/
public static void sendInfo(Long projectId, String message) {
    CopyOnWriteArraySet<Session> sessions = PROJECT_SESSIONS.get(projectId);
    if (sessions==null) {
        return;
    }
    for (Session session : sessions) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("WebSocket send error", e);
        }
        
    }
}
}
