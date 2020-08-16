package com.ciel.scatquick.security.token;

import com.ciel.scatquick.security.realm.ScaCusUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * IP登录认证
 */

@Getter
@Setter
public class IPToken extends AbstractAuthenticationToken {

    private ScaCusUser scaCusUser;

    private String ip;

    public IPToken(String ip) {
        super(null);
        this.ip = ip;
        super.setAuthenticated(false);// 注意这个构造方法是认证时使用的
    }

    public IPToken(ScaCusUser scaCusUser, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.scaCusUser = scaCusUser;
        super.setAuthenticated(true);// 注意这个构造方法是认证成功后使用的

    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.scaCusUser;
    }

}
