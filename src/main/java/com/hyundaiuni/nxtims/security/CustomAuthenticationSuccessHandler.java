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

        Object authenticationDetails = authentication.getDetails();

        if(authenticationDetails != null) {
            if(authenticationDetails instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails)authenticationDetails;

                String userId = authentication.getName();
                String sessionId = webAuthenticationDetails.getSessionId();
                String accessIp = webAuthenticationDetails.getRemoteAddress();

                userService.onAuthenticationSuccess(userId, sessionId, accessIp);
            }
        }
    }
}
