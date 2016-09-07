package com.hyundaiuni.nxtims.web;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.hyundaiuni.nxtims.support.LocaleManager;
import com.hyundaiuni.nxtims.util.WebUtils;

public class CookieLocaleResolver extends SessionLocaleResolver {
    private static final Log log = LogFactory.getLog(CookieLocaleResolver.class);

    private String cookieName;
    private Integer cookieMaxAge;
    private LocaleManager localeManager;

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public void setLocaleManager(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    /**
     * 쿠기, 브라우저 언어 속성 순으로 로케이션 정보를 찾아와서,<br>
     * 시스템에서 사용 가능한 로케이션으로 변환후 해당 로케이션을 리턴함.
     */
    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        Locale defaultLocale = null;
        Cookie cookie = WebUtils.getCookie(request, cookieName);

        try {
            if(cookie != null) {
                defaultLocale = LocaleUtils.toLocale(cookie.getValue());
            }
        }
        catch(IllegalArgumentException e) {
            log.error(e);
        }

        if(defaultLocale == null) {
            defaultLocale = request.getLocale();
        }

        return localeManager.getAvailableLocale(defaultLocale);
    }

    /**
     * 시스템에서 사용 가능한 로케이션으로 변환후 로케이션을 저장함.
     */
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Locale newLocale = localeManager.getAvailableLocale(locale);
        WebUtils.addCookie(response, cookieName, newLocale.toString(), cookieMaxAge);
        super.setLocale(request, response, newLocale);
    }
}
