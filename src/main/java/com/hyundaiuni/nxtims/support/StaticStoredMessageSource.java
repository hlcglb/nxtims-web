package com.hyundaiuni.nxtims.support;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.context.support.AbstractMessageSource;

import com.hyundaiuni.nxtims.service.app.MessageService;

/**
 * 저장된 다국어 메시지를 관리하는 클래스.
 * 
 * @author 권중규
 */
public class StaticStoredMessageSource extends AbstractMessageSource implements StoredMessageSource {
    private LocaleManager localeManager;
    private MessageService messageService;

    private final Map<String, Properties> cachedProperties = new HashMap<String, Properties>();

    public void setLocaleManager(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public Properties getMessageProperties(String locale) {
        String availableLocale = localeManager.getAvailableLocale(LocaleUtils.toLocale(locale)).toString();
        Properties properties = getCachedProperties(cachedProperties, availableLocale);
        if(properties == null) {
            properties = messageService.getMessageListByLanguageCode(availableLocale);
            cachedProperties.put(availableLocale, properties);
        }

        return properties;
    }

    /**
     * 미리 저장되어져 있는 메시지 파일을 리턴함.
     */
    protected Properties getCachedProperties(Map<String, Properties> cachedProperties, String locale) {
        return cachedProperties.get(locale);
    }

    public Properties getMessageProperties(Locale locale) {
        return getMessageProperties(locale.toString());
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        Properties properties = cachedProperties.get(locale.toString());
        if(properties == null) {
            properties = getMessageProperties(locale);
        }

        String message = properties.getProperty(code);

        if(message == null) {
            return null;
        }
        else {
            return createMessageFormat(message, locale);
        }
    }
}
