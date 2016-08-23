package com.hyundaiuni.nxtims.service.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hyundaiuni.nxtims.domain.app.Auth;
import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.service.RestApiTemplate;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/users";
    
    @Autowired
    private RestApiTemplate apiTemplate;    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "username must not be null");
        
        String resourceUrl = apiServerUrl + apiUrl;
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", username);        

        User user = apiTemplate.getRestTemplate().getForObject(resourceUrl + "/{id}", User.class, params);
        
        if(user == null){
            throw new UsernameNotFoundException(username + " not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPwd(), user.isUse(),
            user.isNonExpired(), user.isNonPwdExpired(), user.isNonLocked(), getAuthorities(user));
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for(Auth auth : user.getAuthList()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(auth.getAuthId());
            authorities.add(grantedAuthority);
        }

        return authorities;
    }
}
