package com.ciel.scatquick.websocket;

import com.ciel.scaapi.util.Faster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/socket")
public class SocketController {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @GetMapping("/{ddd}")
    public void sendMessage(@PathVariable("ddd") Date ddd) throws IOException {

        WebSocketServer.sendMessageAll("FUCK YOU MOTHER:"+ Faster.format(ddd));
    }

}
