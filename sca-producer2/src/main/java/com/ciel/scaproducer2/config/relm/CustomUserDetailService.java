package com.ciel.scaproducer2.config.relm;

import com.ciel.scacommons.serverimpl.ScaUserServiceINIT;
import com.ciel.scaentity.entity.ScaPermissions;
import com.ciel.scaentity.entity.ScaUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    protected ScaUserServiceINIT userServiceINIT;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        ScaUser scaUser = userServiceINIT.userByName(userName);

        if(null == scaUser){
            throw new UsernameNotFoundException("找不到此用户");
        }

        Set<SimpleGrantedAuthority> collect =
                Stream.concat(userServiceINIT.permissionsByuId(scaUser.getId()).stream().map(ScaPermissions::getName),
                userServiceINIT.rolesByuId(scaUser.getId()).stream().map(t -> "ROLE_".concat(t.getName())))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        return new CustomUser(scaUser.getUsername(),scaUser.getPassword(),collect,scaUser.getId(),scaUser.getUsername());
    }

}
