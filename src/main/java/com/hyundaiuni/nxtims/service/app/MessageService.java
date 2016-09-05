package com.hyundaiuni.nxtims.service.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyundaiuni.nxtims.service.RestApiTemplate;

@Service
public class MessageService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/messages";

    @Autowired
    private RestApiTemplate apiTemplate;

    public Properties getMessageProperties(String locale) {
        String resourceUrl = apiServerUrl + apiUrl +"?inquiry={inquiry}&languageCode={languageCode}";

        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("inquiry", "getMessagesByLanguageCode");
        urlVariables.put("languageCode", locale);

        @SuppressWarnings("unchecked")
        List<Map<String, String>> messageList = apiTemplate.getRestTemplate().getForObject(resourceUrl, List.class,
            urlVariables);

        Properties properties = new Properties();

        if(!CollectionUtils.isEmpty(messageList)) {
            for(Map<String, String> message : messageList) {
                String key = message.get("MSG_GRP_CD") + "." + message.get("MSG_CD");
                String value = message.get("MSG_NM");

                properties.setProperty(key, value);
            }
        }

        return properties;
    }
}
