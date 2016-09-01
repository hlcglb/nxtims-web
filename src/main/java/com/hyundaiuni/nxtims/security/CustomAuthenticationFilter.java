package com.hyundaiuni.nxtims.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class CustomAuthenticationFilter extends BasicAuthenticationFilter {
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public void setAuthenticationSuccessHandler(CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    public void setAuthenticationFailureHandler(CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    protected void onSuccessfulAuthentication(javax.servlet.http.HttpServletRequest request,
        javax.servlet.http.HttpServletResponse response, Authentication authResult) throws IOException{

        try {
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
        }
        catch(ServletException e) {
            e.printStackTrace();
        }
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException {
        
        try {
            authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
        }
        catch(ServletException e) {
            e.printStackTrace();
        }
    }
}
