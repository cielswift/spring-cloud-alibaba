package com.ciel.scaproducer1.controller;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.feign.FuckMyLifeFeign;
import com.ciel.scaapi.util.SysUtils;
import com.ciel.scaentity.entity.ScaGirls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * feign 远程调用
 */
@RestController
public class FeignController implements FuckMyLifeFeign {

    @Autowired
    protected IScaUserService scaUserService;

    @Override
    @GetMapping("/get")
    public List<String> format(@RequestParam("name")String name) {

        HttpServletRequest request = SysUtils.getRequest();
        String token = request.getHeader("Authentication");
        String mother = request.getHeader("mother");
        String nameh = request.getHeader("name");

        LinkedList<String> strings = new LinkedList<>();
        strings.add(token);
        strings.add(mother);
        strings.add(nameh);
        strings.add(name);
        return strings;
    }


    @Override
    @GetMapping(value = "/get/map")
    public String getQueryMap(@SpringQueryMap ScaGirls scaGirls) {
        System.out.println(scaGirls);
        return scaGirls.getName();
    }

    @Override
    @PostMapping("/post")
    public String posts(@RequestBody ScaGirls scaGirls, @RequestParam("id") Long id) {
        System.out.println(scaGirls);
        return "POST:"+id + scaGirls.getName();
    }

    @Override
    @PutMapping(value = "/put")
    public String puts(@RequestBody ScaGirls scaGirls, @RequestParam("id") Long id) {
        System.out.println(scaGirls);
        return "POST:"+id + scaGirls.getName();
    }

    @Override
    @DeleteMapping(value = "/del/{id}/{name}")
    public String delete(@PathVariable("id")Long id, @PathVariable("name")String name) {
        return name +id;
    }

    @GetMapping("/test")
    public Object testTransaction(){
        return scaUserService.testTransaction();
    }
}
