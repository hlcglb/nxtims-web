package com.hyundaiuni.nxtims.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.hyundaiuni.nxtims.service.app.UserService;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws ServletException, IOException {

        if(authentication.getDetails() != null) {
            if(authentication.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails)authentication.getDetails();

                String userId = authentication.getName();
                String sessionId = authenticationDetails.getSessionId();
                String accessIp = authenticationDetails.getRemoteAddress();

                userService.onAuthenticationSuccess(userId, sessionId, accessIp);
            }
        }
    }
}
