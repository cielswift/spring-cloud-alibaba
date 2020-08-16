package com.ciel.scatquick.security.realm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class ScaCusUser extends User {

    private Long id;
    private String realName;
    private String ip;

    public ScaCusUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
                      Long userId,String realName,String ip) {
        super(username, password, authorities);
        setId(userId);
        setRealName(realName);
        setIp(ip);
    }
}
