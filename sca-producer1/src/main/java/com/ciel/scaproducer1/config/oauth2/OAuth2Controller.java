package com.ciel.scaproducer1.config.oauth2;

import com.ciel.scaapi.retu.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/ac")

@Slf4j
@AllArgsConstructor
public class OAuth2Controller {

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/ecc/{yy}")
    public Result sec(@PathVariable("yy") String y){


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(principal);
        return Result.ok("sec 访问成功"+y).body(principal);
    }

    @PreAuthorize("hasAnyAuthority('CHANGE')")
    @PutMapping("/ngl/{yy}")
    public Result sex(@PathVariable("yy") String y){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(principal);
        return Result.ok("sec 访问成功"+y).body(principal);
    }

}
