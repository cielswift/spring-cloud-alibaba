package com.ciel.scaproducer1.controller;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.feign.FuckMyLife;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
public class FeignController implements FuckMyLife {

    @Autowired
    protected IScaUserService scaUserService;

    @Override
    @GetMapping("/cc")
    public List<String> fml(@RequestParam("name")String name) {

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader("token");

        LinkedList<String> strings = new LinkedList<>();
        strings.add("name- you-token".concat(token));
        return strings;
    }


    @GetMapping("/test")
    public Object testTransaction(){

        return scaUserService.testTransaction();
    }
}
