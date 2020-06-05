package com.ciel.scatquick.controller;

import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Controller
@ServerEndpoint("/websocket")
public class WebSocketController {

    //通过 Session.getBasicRemote().sendText(String message) 想客户端推送消息。

    /*websocket 客户端会话 通过Session 向客户端发送数据*/
    private Session session;
    /*线程安全set 存放每个客户端处理消息的对象*/
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<>();
    /*websocket 连接建立成功后进行调用*/


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("websocket 有新的链接"+webSocketSet.size());

        boolean equals = Objects.equals("a", "b");
    }

    /*WebSocket 连接关闭调用的方法*/
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    /*收到客户端消息后调用的方法*/
    @OnMessage
    public void onMessage(String message) throws IOException {
        for (WebSocketController socket : webSocketSet) {
            socket.session.getBasicRemote().sendText("自己嘎给自己嘎发的消息："+message);
        }
    }

    /* WebSocket 发生错误时进行调用*/
    @OnError
    public void onError(Session session,Throwable error){
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        for (WebSocketController socket : webSocketSet) {
            socket.session.getBasicRemote().sendText(message);
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
