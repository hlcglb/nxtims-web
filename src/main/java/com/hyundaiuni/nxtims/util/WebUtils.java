package com.hyundaiuni.nxtims.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.util.CookieGenerator;

public class WebUtils extends org.springframework.web.util.WebUtils {
    /**
     * 주어진 도메인, 이름에 해당하는 첫번째 쿠키를 리턴함.
     * 
     * @param request Http 응답 객체
     * @param cookieDomain 쿠키 도메인
     * @param cookieName 쿠키명
     * @return 주어진 도메인, 이름에 해당하는 첫번째 쿠키를 리턴함.
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieDomain, String cookieName) {
        Assert.notNull(request, "Request must not be null");

        Cookie cookies[] = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookieDomain.equals(cookie.getDomain()) && cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;
    }

    /**
     * 주어진 이름, 값으로 쿠키에 저장함.
     * 
     * @param response Http 응답 객체
     * @param cookieName 쿠기에 저장할 이름
     * @param cookieValue 쿠키에 저장할 값
     */
    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        setCookie(response, cookieName, cookieValue, null);
    }

    /**
     * 주어진 이름, 값으로 쿠키에 저장함.
     * 
     * @param response Http 응답 객체
     * @param cookieName 쿠기에 저장할 이름
     * @param cookieValue 쿠키에 저장할 값
     * @param cookieMaxAge 쿠키를 저장할 시간(초)
     */
    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue,
        Integer cookieMaxAge) {
        setCookie(response, null, cookieName, cookieValue, cookieMaxAge);
    }

    /**
     * 주어진 이름, 값으로 쿠키에 저장함.
     * 
     * @param response Http 응답 객체
     * @param cookieDomain 쿠기에 저장할 도메인
     * @param cookieName 쿠기에 저장할 이름
     * @param cookieValue 쿠키에 저장할 값
     * @param cookieMaxAge 쿠키를 저장할 시간(초)
     */
    public static void setCookie(HttpServletResponse response, String cookieDomain, String cookieName,
        String cookieValue, Integer cookieMaxAge) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieDomain(cookieDomain);
        cookieGenerator.setCookieName(cookieName);
        cookieGenerator.removeCookie(response);
        cookieGenerator.setCookieMaxAge(cookieMaxAge);
        cookieGenerator.addCookie(response, cookieValue);
    }

    /**
     * 주어진 이름, 값으로 쿠키에 추가함.
     * 
     * @param response Http 응답 객체
     * @param cookieName 쿠기에 저장할 이름
     * @param cookieValue 쿠키에 저장할 값
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        addCookie(response, cookieName, cookieValue, null);
    }

    /**
     * 주어진 이름, 값으로 쿠키에 추가함.
     * 
     * @param response Http 응답 객체
     * @param cookieName 쿠기에 저장할 이름
     * @param cookieValue 쿠키에 저장할 값
     * @param cookieMaxAge 쿠키를 저장할 시간(초)
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue,
        Integer cookieMaxAge) {
        addCookie(response, null, cookieName, cookieValue, cookieMaxAge);
    }

    /**
     * 주어진 이름, 값으로 쿠키에 추가함.
     * 
     * @param response Http 응답 객체
     * @param cookieDomain 쿠기에 저장할 도메인
     * @param cookieName 쿠기에 저장할 이름
     * @param cookieValue 쿠키에 저장할 값
     * @param cookieMaxAge 쿠키를 저장할 시간(초)
     */
    public static void addCookie(HttpServletResponse response, String cookieDomain, String cookieName,
        String cookieValue, Integer cookieMaxAge) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieDomain(cookieDomain);
        cookieGenerator.setCookieName(cookieName);
        cookieGenerator.setCookieMaxAge(cookieMaxAge);
        cookieGenerator.addCookie(response, cookieValue);
    }

    /**
     * 주어진 이름의 쿠키를 삭제함.
     * 
     * @param response Http 응답 객체
     * @param cookieName 쿠기에 저장할 이름
     */
    public static void removeCookie(HttpServletResponse response, String cookieName) {
        removeCookie(response, null, cookieName);
    }

    /**
     * 주어진 이름의 쿠키를 삭제함.
     * 
     * @param response Http 응답 객체
     * @param cookieDomain 쿠기에 저장할 도메인
     * @param cookieName 쿠기에 저장할 이름
     */
    public static void removeCookie(HttpServletResponse response, String cookieDomain, String cookieName) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieDomain(cookieDomain);
        cookieGenerator.setCookieName(cookieName);
        cookieGenerator.removeCookie(response);
    }
}
