package com.ciel.scatquick.security.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class LoginToken extends UsernamePasswordAuthenticationToken {

    /**
     * 短信验证码
     */
    private String sms;

    /**
     * 第一个构造器是用于认证之前
     */
    public LoginToken(Object principal, Object credentials,String sms) {
        super(principal, credentials);
        this.sms=sms;
    }

    /**
     * 第二个构造器用于认证成功之后，封装认证用户的信息
     */
    public LoginToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials,authorities);
    }
}
