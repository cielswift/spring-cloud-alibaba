package com.ciel.scatquick.security.provider;

import com.ciel.scatquick.security.realm.ScaCusUser;
import com.ciel.scatquick.security.realm.ScaUserDealService;
import com.ciel.scatquick.security.token.IPToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IPProvider implements AuthenticationProvider {

    @Autowired
    private ScaUserDealService userDetailService;

    // 只支持 IpToken 该身份
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(IPToken.class);
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        IPToken ipAuthenticationToken = (IPToken) authentication;
        String ip = ipAuthenticationToken.getIp();

        ScaCusUser userDetails = (ScaCusUser) userDetailService.loadUserByIp(ip);

        // 封装权限信息，并且此时身份已经被认证
        return new IPToken(userDetails, userDetails.getAuthorities());
    }

}