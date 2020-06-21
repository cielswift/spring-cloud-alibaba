package com.ciel.scatquick.websocket;

import com.alibaba.fastjson.JSON;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket")

@EqualsAndHashCode
@Getter
@Setter
@Slf4j
public class WebSocketServer {

    /**
     * 通过 Session.getBasicRemote().sendText(String message) 想客户端推送消息。
     * websocket 客户端会话 通过Session 向客户端发送数据
     */
    protected Session session;

    /**
     * 线程安全map 存放每个客户端处理消息的对象 static 的只有一个
     */
    public static ConcurrentHashMap<String, WebSocketServer> WEB_SOCKETS = new ConcurrentHashMap<>();

    /**
     * websocket 连接建立成功后进行调用
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        WEB_SOCKETS.put(session.getId(),this);
        System.out.println("websocket 有新的链接:" + WEB_SOCKETS.size());
    }

    /*
    WebSocket 连接关闭调用的方法
    */
    @OnClose
    public void onClose() {
        WEB_SOCKETS.remove(this.session.getId());
        System.out.println("关闭了链接:" + WEB_SOCKETS.size());
    }

    /*收到客户端消息后调用的方法*/
    @OnMessage
    public void onMessage(String message) throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("ID", session.getId());
        map.put("MSG", message);

        String jsonString = JSON.toJSONString(map);

        //异步发送 单独发送
        session.getAsyncRemote().sendText("点对点发送:" + jsonString);

        WEB_SOCKETS.forEach((x,y) -> {
            try {
                y.session.getBasicRemote()
                        .sendText(String.format("同步广播所有人 : you id %s,msg %s",y.session.getId(),message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static void sendMessageById(String id,String msg) throws IOException {
        WebSocketServer webSocketServer = WEB_SOCKETS.get(id);
        webSocketServer.session.getAsyncRemote()
                .sendText(String.format("SEND YOU: you id: %s,msg: %s",webSocketServer.session.getId(),msg));
    }

    public static void sendMessageAll(String msg) {
        WEB_SOCKETS.forEach((x,y) -> {
            y.session.getAsyncRemote()
                    .sendText(String.format("SEND ALL PEOPLE: you id: %s,msg: %s",y.session.getId(),msg));
        });
    }

    /*
    WebSocket 发生错误时进行调用
    */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
