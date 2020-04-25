package com.ciel.scatquick.security;

import com.ciel.scatquick.security.realm.ScaCusUser;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    /**
     * 获取当前登录用户
     * @return
     */
    public static ScaCusUser currentScaUser(){
        try {
            return (ScaCusUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            return null;
        }
    }
}
