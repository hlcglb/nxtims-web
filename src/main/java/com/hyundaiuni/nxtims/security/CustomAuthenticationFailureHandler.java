package com.hyundaiuni.nxtims.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.hyundaiuni.nxtims.helper.MessageDigestHelper;
import com.hyundaiuni.nxtims.service.app.UserService;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Log log = LogFactory.getLog(CustomAuthenticationFailureHandler.class);

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        log.info("CustomAuthenticationSuccessHandler exception = " + exception.toString());

        if(exception instanceof BadCredentialsException) {
            String headerString = request.getHeader("authorization");
            String replacedString = StringUtils.remove(headerString, "Basic ");
            String decodedString = new String(MessageDigestHelper.decodeBase64(replacedString.getBytes()));
            String userId = StringUtils.substring(decodedString, 0, StringUtils.indexOf(decodedString, ":"));

            userService.onAuthenticationFailure(userId);
        }
    }
}
