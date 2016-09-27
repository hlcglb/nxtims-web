package com.hyundaiuni.nxtims.support.message;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.Validate;

public class LocaleManager {
    private Locale defaultLocale;
    private Locale[] availableLocales;

    public LocaleManager() {}

    /**
     * 기본 {@link Locale}을 설정함.
     */
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /**
     * 기본 {@link Locale}을 리턴함.
     */
    public Locale getDefaultLocale() {
        Validate.notNull(defaultLocale, "DefaultLocale must not be null.");

        return defaultLocale;
    }

    /**
     * 사용가능한 {@link Locale}들을 설정함.
     */
    public void setAvailableLocales(Locale... availableLocales) {
        this.availableLocales = availableLocales;
    }

    /**
     * 사용가능한 {@link Locale}들을 리턴함.
     */
    public Locale[] getAvailableLocales() {
        return availableLocales == null ? new Locale[] {getDefaultLocale()} : availableLocales;
    }

    /**
     * 사용 가능한 로케이션인지 여부를 리턴함.
     * 
     * @param locale 사용 가능여부를 체크를 할 {@link Locale}
     * @return 사용 가능한 로케이션이면 <code>true</code>, 아니면 <code>false</code>를 리턴함
     */
    public boolean isAvailableLocale(Locale locale) {
        for(Locale availableLocale : getAvailableLocales()) {
            if(availableLocale.equals(locale)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 사용 가능한 로케이션인지 여부 확인후 사용할 수 있는 로케이션이면 주어진 로케이션을 리턴하고, 아니면 기본 {@link Locale}을 리턴함.
     * 
     * @param locale 사용 가능여부를 체크를 할 {@link Locale}
     * @return 사용 가능한 로케이션이면 주어진 로케이션을, 아니면 기본 {@link Locale}을 리턴함
     */
    public Locale getAvailableLocale(Locale locale) {
        if(locale != null) {
            if(isAvailableLocale(locale)) {
                return locale;
            }

            List<Locale> localeList = LocaleUtils.countriesByLanguage(locale.getLanguage());

            for(Locale availableLocale : localeList) {
                for(Locale supportedLocale : getAvailableLocales()) {
                    if(supportedLocale.equals(availableLocale)) {
                        return availableLocale;
                    }
                }
            }
        }

        return getDefaultLocale();
    }
}
