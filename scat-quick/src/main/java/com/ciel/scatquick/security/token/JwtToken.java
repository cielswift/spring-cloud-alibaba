package com.ciel.scatquick.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtToken extends UsernamePasswordAuthenticationToken {

    public JwtToken(Object principal, Object credentials) {
        super(principal, credentials);
    }


    public JwtToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials,authorities);
    }
}
