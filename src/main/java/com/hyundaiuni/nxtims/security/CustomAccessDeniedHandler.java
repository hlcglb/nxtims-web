package com.hyundaiuni.nxtims.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Log log = LogFactory.getLog(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info(accessDeniedException);

        String xmlHttpRequest = request.getHeader("X-Requested-With");

        String result = "";

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");

        if(StringUtils.isEmpty(xmlHttpRequest)) {
            // 일반적인 URL로 접근
            result = "You don't have permission to access." + accessDeniedException.getMessage();

            response.getWriter().print(result);
            response.getWriter().flush();
        }
        else {
            // Angular JS 화면에서 Call
            result = "{\"result\" : \"fail\", \"message\" : \"" + accessDeniedException.getMessage() + "\"}";

            response.getWriter().print(result);
            response.getWriter().flush();
        }
    }
}
