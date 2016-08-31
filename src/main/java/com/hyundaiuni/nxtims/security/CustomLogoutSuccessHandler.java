package com.hyundaiuni.nxtims.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.hyundaiuni.nxtims.service.app.UserService;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {

        if(authentication.getDetails() != null) {
            if(authentication.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails)authentication.getDetails();

                String userId = authentication.getName();
                String sessionId = authenticationDetails.getSessionId();

                userService.onLogout(userId, sessionId);
            }
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
