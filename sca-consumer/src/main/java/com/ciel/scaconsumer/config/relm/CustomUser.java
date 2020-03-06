//package com.ciel.scaconsumer.config.relm;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Collection;
//
//@Getter
//@Setter
//public class CustomUser extends User {
//
//    private Long id;
//    private String realName;
//
//    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
//                      Long userId,String realName) {
//        super(username, password, authorities);
//        setId(userId);
//        setRealName(realName);
//    }
//
//
//}