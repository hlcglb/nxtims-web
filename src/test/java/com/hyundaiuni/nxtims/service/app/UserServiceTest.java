package com.hyundaiuni.nxtims.service.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.hyundaiuni.nxtims.domain.app.Auth;
import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.util.WebUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    private static final Log log = LogFactory.getLog(UserServiceTest.class);
    
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
        Exception ex = null;

        try {
            userService.onAuthenticationSuccess("test", "DB18EBE12C90845710D544C7A15D7072", "1.1.1");
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testOnAuthenticationFailure() {
        Exception ex = null;

        try {
            userService.onAuthenticationFailure("test");
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testOnLogout() {
        Exception ex = null;
        
        try {
            userService.onLogout("test", "DB18EBE12C90845710D544C7A15D7072");
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testGetUserListByParam() {
        Exception ex = null;

        try {
            Map<String, Object> parameter = new HashMap<>();
            
            String requestString = WebUtils.mapToRequestParam(parameter, ',', '=', "UTF-8");

            userService.getUserListByParam(requestString, 0, 10);
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testGetUser() {
        User user = userService.getUser("20006X");

        if(user != null) {
            fail("");
        }

        assertThat(userService.getUser("test")).isInstanceOf(User.class);
    }
    
    @Test
    public void testInsertUser(){
        try {
            User user = new User();
            
            user.setUserId("XXXXXX");
            user.setUserNm("XXXXXX");
            user.setUserPwd("12345qwert");
            user.setUseYn("Y");
            
            Auth auth = new Auth();
            auth.setAuthId("ANONYMOUS");
            
            List<Auth> authList = new ArrayList<>();
            authList.add(auth);
            
            user.setAuthList(authList);
            
            userService.insertUser(user);
            
            userService.deleteUser(user.getUserId());
        }
        catch(Exception e) {
            log.error(e.getMessage());
            assertThat(e).isInstanceOf(ServiceException.class).hasMessageContaining("not found");
        }
    }
    
    @Test
    public void testUpdateUser(){
        try {
            User user = new User();
            
            user.setUserId("XXXXXX");
            user.setUserNm("XXXXXX");
            user.setUserPwd("12345qwert");
            user.setUseYn("Y");
            
            Auth auth = new Auth();
            auth.setAuthId("ANONYMOUS");
            
            List<Auth> authList = new ArrayList<>();
            authList.add(auth);
            
            user.setAuthList(authList);
            
            userService.insertUser(user);
            
            user.setUserPwd("qwert12345");
            
            authList = user.getAuthList();
            
            if(CollectionUtils.isNotEmpty(authList)) {
                for(Auth tempAuth : authList) {
                    tempAuth.setTransactionType("D");
                }
            }
            
            Auth auth1 = new Auth();
            auth1.setAuthId("EMPLOYEE");
            auth1.setTransactionType("C");
            
            authList.add(auth1);
            
            user.setAuthList(authList);
            
            userService.updateUser(user);
            
            userService.deleteUser(user.getUserId());
        }
        catch(Exception e) {
            log.error(e.getMessage());
            assertThat(e).isInstanceOf(ServiceException.class).hasMessageContaining("not found");
        }
    }       
}
