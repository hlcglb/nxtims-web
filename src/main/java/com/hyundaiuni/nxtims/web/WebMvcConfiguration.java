package com.hyundaiuni.nxtims.web;

import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.hyundaiuni.nxtims.service.app.MessageService;
import com.hyundaiuni.nxtims.support.message.LocaleManager;
import com.hyundaiuni.nxtims.support.message.ReloadableStoredMessageSource;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    private Locale defaultLocale = LocaleUtils.toLocale("ko_KR");
    private Locale[] availableLocales = new Locale[] {defaultLocale};

    @Autowired
    private MessageService messageService;

    @Bean
    public LocaleManager LocaleManager() {
        LocaleManager LocaleManager = new LocaleManager();

        LocaleManager.setDefaultLocale(defaultLocale);
        LocaleManager.setAvailableLocales(availableLocales);

        return LocaleManager;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();

        resolver.setCookieName("language");
        resolver.setCookieMaxAge(604800);
        resolver.setLocaleManager(LocaleManager());
        resolver.setDefaultLocale(defaultLocale);

        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableStoredMessageSource messageSource = new ReloadableStoredMessageSource();

        messageSource.setLocaleManager(LocaleManager());
        messageSource.setMessageService(messageService);

        return messageSource;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();

        localeChangeInterceptor.setParamName("lang");

        return localeChangeInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
