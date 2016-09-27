package com.hyundaiuni.nxtims.support.message;

import java.util.Locale;
import java.util.Properties;

/**
 * 다국어 메시지를 관리하는 클래스.
 * 
 * @author 권중규
 */
public interface StoredMessageSource {
    /**
     * 주어진 locale에 해당하는 메시지가 저장된 {@link Properties}을 리턴함.
     * 
     * @return 메시지가 저장된 {@link Properties}
     */
    public Properties getMessageProperties(String locale);

    /**
     * 주어진 locale에 해당하는 메시지가 저장된 {@link Properties}을 리턴함.
     * 
     * @return 메시지가 저장된 {@link Properties}
     */
    public Properties getMessageProperties(Locale locale);
}
