package com.hyundaiuni.nxtims.service.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hyundaiuni.nxtims.domain.app.Message;
import com.hyundaiuni.nxtims.domain.app.MessageLocale;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;

@Service
public class MessageService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/messages";

    @Autowired
    private RestApiTemplate apiTemplate;

    public Properties getMessageListByLanguageCode(String languageCode) {
        Assert.notNull(languageCode, "languageCode must not be null");
        
        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&languageCode={languageCode}";

        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("inquiry", "getMessageListByLanguageCode");
        urlVariables.put("languageCode", languageCode);

        List<MessageLocale> messageLocaleList = new ArrayList<>();

        CollectionUtils.addAll(messageLocaleList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, MessageLocale[].class, urlVariables));

        Properties properties = new Properties();

        if(CollectionUtils.isNotEmpty(messageLocaleList)) {
            for(MessageLocale messageLocale : messageLocaleList) {
                String key = messageLocale.getMsgGrpCd() + "." + messageLocale.getMsgCd();
                String value = messageLocale.getMsgNm();

                properties.setProperty(key, value);
            }
        }

        return properties;
    }

    public List<Message> getMessageListByParam(String query, int offset, int limit) {
        Assert.notNull(query, "parameter must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getMessageListByParam");
        urlVariables.put("q", query);
        urlVariables.put("offset", offset);
        urlVariables.put("limit", limit);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&q={q}&offset={offset}&limit={limit}";

        List<Message> messageList = new ArrayList<>();

        CollectionUtils.addAll(messageList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, Message[].class, urlVariables));

        return messageList;
    }

    public List<MessageLocale> getMessageLocaleListByParam(Map<String, Object> parameter) {
        Assert.notNull(parameter, "parameter must not be null");

        String msgGrpCd = MapUtils.getString(parameter, "msgGrpCd");
        String msgCd = MapUtils.getString(parameter, "msgCd");

        Assert.notNull(msgGrpCd, "msgGrpCd must not be null");
        Assert.notNull(msgCd, "msgCd must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getMessageLocaleListByParam");
        urlVariables.put("msgGrpCd", msgGrpCd);
        urlVariables.put("msgCd", msgCd);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&msgGrpCd={msgGrpCd}&msgCd={msgCd}";

        List<MessageLocale> messageLocaleList = new ArrayList<>();

        CollectionUtils.addAll(messageLocaleList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, MessageLocale[].class, urlVariables));

        return messageLocaleList;
    }

    public Message getMessage(String msgPk) {
        Assert.notNull(msgPk, "msgPk must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{msgPk}";

        return apiTemplate.getRestTemplate().getForObject(resourceUrl, Message.class, msgPk);
    }

    public Message insertMessage(Message message) {
        Assert.notNull(message, "message must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        return apiTemplate.getRestTemplate().postForObject(resourceUrl, message, Message.class);
    }

    public Message updateMessage(Message message) {
        Assert.notNull(message, "message must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{msgPk}";

        apiTemplate.getRestTemplate().put(resourceUrl, message, message.getMsgPk());

        return getMessage(message.getMsgPk());
    }

    public void deleteMessage(String msgPk) {
        Assert.notNull(msgPk, "msgPk must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{msgPk}";

        apiTemplate.getRestTemplate().delete(resourceUrl, msgPk);
    }
}
