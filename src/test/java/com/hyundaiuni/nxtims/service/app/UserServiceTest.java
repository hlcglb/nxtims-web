package com.hyundaiuni.nxtims.service.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

import com.hyundaiuni.nxtims.service.app.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Test
    public void loadUserByUsernameTest() {
        UserDetails user = userService.loadUserByUsername("200065");
        assertEquals(user.getUsername(),"200065");
        
        @SuppressWarnings("unchecked")
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>)user.getAuthorities();
        Iterator<GrantedAuthority> itr = authorities.iterator();
        
        assertEquals(itr.hasNext(),true);
    }
    
    @Test
    public void loadUserByUsernameTestNotFound() {
        try {
            userService.loadUserByUsername("20006X");
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(UsernameNotFoundException.class);
        }
    }
    

    @Test
    public void testOnAuthenticationSuccess() {
        try {
            userService.onAuthenticationSuccess("test", "DB18EBE12C90845710D544C7A15D7072", "1.1.1");
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOnAuthenticationFailure() {
        try {
            userService.onAuthenticationFailure("test");
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOnLogout() {
        try {
            userService.onLogout("test", "DB18EBE12C90845710D544C7A15D7072");
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }        
}
