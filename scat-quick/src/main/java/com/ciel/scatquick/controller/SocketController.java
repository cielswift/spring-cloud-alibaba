package com.ciel.scatquick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

@RestController
@RequestMapping("/socket")
public class SocketController {

    @Autowired
    private WebSocketController socket;

    @GetMapping("/{message}")
    public void sendMessage(@PathVariable("message") String message) throws IOException {
        socket.sendMessage("这个是controller 发送的消息："+message);
    }

    public static void main(String[] args) {

        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.CHINA).dayOfWeek();
        System.out.println(now.with(fieldISO, 1)); // 2015-02-09 (Monday)

        TemporalField fieldUS = WeekFields.of(Locale.CHINA).dayOfWeek();
        System.out.println(now.with(fieldUS, 1)); // 2015-02-08 (Sunday)


    }

}
