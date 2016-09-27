package com.hyundaiuni.nxtims.support.message;

import java.util.Map;
import java.util.Properties;

/**
 * 리로드 가능한 다국어 메시지를 관리하는 클래스.
 * 
 * @author 권중규
 */
public class ReloadableStoredMessageSource extends StaticStoredMessageSource {
    @Override
    protected Properties getCachedProperties(Map<String, Properties> cachedProperties, String locale) {
        return null;
    }
}
