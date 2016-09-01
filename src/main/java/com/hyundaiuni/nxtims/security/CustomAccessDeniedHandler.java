package com.hyundaiuni.nxtims.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String xmlHttpRequest = request.getHeader("X-Requested-With");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");

        if(StringUtils.isEmpty(xmlHttpRequest)) {
            // 일반적인 URL로 접근
            String result = "You don't have permission to access." + accessDeniedException.getMessage();

            response.getWriter().print(result);
            response.getWriter().flush();
        }
        else {
            // Angular JS 화면에서 Call
            Map<String, String> result = new HashMap<>();

            result.put("result", "fail");
            result.put("message", accessDeniedException.getMessage());

            ObjectMapper mapper = new ObjectMapper();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(mapper.writeValueAsString(result));
        }
    }
}
