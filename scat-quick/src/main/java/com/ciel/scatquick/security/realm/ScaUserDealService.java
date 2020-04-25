package com.ciel.scatquick.security.realm;

import com.ciel.scaapi.crud.IScaPermissionsService;
import com.ciel.scaapi.crud.IScaRoleService;
import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaentity.entity.ScaBaseEntity;
import com.ciel.scaentity.entity.ScaPermissions;
import com.ciel.scaentity.entity.ScaRole;
import com.ciel.scaentity.entity.ScaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScaUserDealService implements UserDetailsService {

    @Autowired
    private IScaUserService scaUserService;

    @Autowired
    private IScaRoleService scaRoleService;

    @Autowired
    private IScaPermissionsService permissionsService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        ScaUser scaUser = scaUserService.getByName(userName);

        if(Faster.isNull(scaUser)){
            throw new UsernameNotFoundException("找不到此用户");
        }
        return loadUserDetail(scaUser);

    }

    public UserDetails loadUserByIp(String ip) throws UsernameNotFoundException {
        ScaUser scaUser = scaUserService.getByIp(ip);
        if(Faster.isNull(scaUser)){
            throw new UsernameNotFoundException("找不到此用户");
        }
        return loadUserDetail(scaUser);
    }


    private ScaCusUser loadUserDetail(ScaUser scaUser) throws UsernameNotFoundException {

        List<ScaRole> scaRoles = scaRoleService.rolesByUserId(scaUser.getId());

        List<ScaPermissions> byRoleIds =
                permissionsService.getByRoleIds(scaRoles.stream().map(ScaBaseEntity::getId).collect(Collectors.toList()));

        List<ScaPermissions> byUserId = permissionsService.getByUserId(scaUser.getId());

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        scaRoles.forEach( t -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(t.getName())));
        });

        byRoleIds.forEach( t -> {
            authorities.add(new SimpleGrantedAuthority(t.getName()));
        });

        byUserId.forEach( t -> {
            authorities.add(new SimpleGrantedAuthority(t.getName()));
        });

        return new ScaCusUser(scaUser.getUsername(),
                scaUser.getPassword(),
                authorities,
                scaUser.getId(),
                scaUser.getUsername(),
                "");

    }
}