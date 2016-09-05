package com.hyundaiuni.nxtims.service.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyundaiuni.nxtims.domain.app.Message;
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

        List<Message> messageList = Arrays.asList(apiTemplate.getRestTemplate().getForObject(resourceUrl, Message[].class,
            urlVariables));

        Properties properties = new Properties();

        if(!CollectionUtils.isEmpty(messageList)) {
            for(Message message : messageList) {
                String key = message.getMsgGrpCd() + "." + message.getMsgCd();
                String value = message.getMsgNm();

                properties.setProperty(key, value);
            }
        }

        return properties;
    }
}
