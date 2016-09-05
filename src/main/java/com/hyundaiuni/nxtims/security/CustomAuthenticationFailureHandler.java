package com.hyundaiuni.nxtims.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundaiuni.nxtims.service.app.UserService;
import com.hyundaiuni.nxtims.util.MessageDigestUtils;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Log log = LogFactory.getLog(CustomAuthenticationFailureHandler.class);

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        Map<String, String> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        if(exception instanceof BadCredentialsException) {
            // Bad Credentials
            String userId = getUserId(request.getHeader("authorization"));

            if(!StringUtils.isEmpty(userId)) {
                userService.onAuthenticationFailure(userId);
            }

            result.put("type", "BadCredentials");
        }
        else if(exception instanceof LockedException) {
            // User account is locked
            result.put("type", "Locked");
        }
        else if(exception instanceof CredentialsExpiredException) {
            // User credentials have expired
            result.put("type", "CredentialsExpired");
        }
        else if(exception instanceof AccountExpiredException) {
            // User account has expired
            result.put("type", "AccountExpired");
        }
        else if(exception instanceof DisabledException) {
            // User is disabled
            result.put("type", "Disabled");
        }
        else {
            result.put("type", "undefined");
        }

        result.put("error", "Unauthorized");
        result.put("status", "401");
        result.put("message", exception.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(result));
    }

    private String getUserId(String authHeader) {
        String userId = null;

        try {
            String replacedAuthHeader = StringUtils.remove(authHeader, "Basic ");
            String decodedAuthHeader = new String(
                MessageDigestUtils.decodeBase64(replacedAuthHeader.getBytes("UTF-8")));
            userId = StringUtils.substring(decodedAuthHeader, 0, StringUtils.indexOf(decodedAuthHeader, ":"));
        }
        catch(UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException : ", e);
        }

        return userId;
    }
}
