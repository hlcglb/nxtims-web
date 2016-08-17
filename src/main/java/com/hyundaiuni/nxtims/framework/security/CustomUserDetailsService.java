package com.hyundaiuni.nxtims.framework.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hyundaiuni.nxtims.framework.domain.Auth;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "username must not be null");

        com.hyundaiuni.nxtims.framework.domain.User user = new com.hyundaiuni.nxtims.framework.domain.User();
        user.setUserId(username);
        user.setUserPwd("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918"); //패스워드 admin sha-256
        user.setUseYn("Y");
        user.setExpiredYn("N");
        user.setPwdExpiredYn("N");
        user.setLockedYn("N");

        Auth auth = new Auth();
        auth.setAuthId("ROLE_ADMIN");

        List<Auth> authList = new ArrayList<Auth>();
        authList.add(auth);

        user.setAuthList(authList);

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPwd(), user.isUse(),
            user.isNonExpired(), user.isNonPwdExpired(), user.isNonLocked(), getAuthorities(user));
    }

    private Set<GrantedAuthority> getAuthorities(com.hyundaiuni.nxtims.framework.domain.User user) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        for(Auth auth : user.getAuthList()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(auth.getAuthId());
            authorities.add(grantedAuthority);
        }

        return authorities;
    }
}
