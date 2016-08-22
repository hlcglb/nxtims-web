package com.hyundaiuni.nxtims.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.hyundaiuni.nxtims.service.app.CustomUserDetailsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomUserDetailsServiceTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Test
    public void loadUserByUsernameTest() {
        UserDetails user = customUserDetailsService.loadUserByUsername("200065");
        assertEquals(user.getUsername(),"200065");
        
        @SuppressWarnings("unchecked")
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>)user.getAuthorities();
        Iterator<GrantedAuthority> itr = authorities.iterator();
        
        assertEquals(itr.hasNext(),true);
    }
    
    @Test
    public void loadUserByUsernameTestNotFound() {
        try {
            customUserDetailsService.loadUserByUsername("20006X");
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(UsernameNotFoundException.class);
        }
    }        
}
