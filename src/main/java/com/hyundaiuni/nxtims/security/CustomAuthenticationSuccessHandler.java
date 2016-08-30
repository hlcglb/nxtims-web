package com.hyundaiuni.nxtims.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.hyundaiuni.nxtims.service.app.UserService;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Log log = LogFactory.getLog(CustomAuthenticationSuccessHandler.class);
    
    private UserService userService;
    
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws ServletException, IOException {

        log.info("CustomAuthenticationSuccessHandler userId = " + authentication.getName());
        
        userService.onAuthenticationSuccess(authentication.getName(), request.getSession().getId());
    }
}
