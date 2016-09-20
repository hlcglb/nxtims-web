package com.hyundaiuni.nxtims.controller.app;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hyundaiuni.nxtims.domain.app.CodeDetail;
import com.hyundaiuni.nxtims.service.app.AppService;
import com.hyundaiuni.nxtims.service.app.MessageService;
import com.hyundaiuni.nxtims.support.CodeFactoryBean;

@RestController
@RequestMapping("/api/login")
public class AppController {
    @Autowired
    private AppService appService;

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private CodeFactoryBean codeFactoryBean;

    @RequestMapping("/authentication")
    public Principal authentication(Principal user) {
        return user;
    }

    @RequestMapping("/resource")
    public Map<String, Object> resource(Principal user) {
        String userId = user.getName();

        Map<String, Object> model = appService.getResourceByUserId(userId);
        return model;
    }

    @RequestMapping("/message")
    @ResponseBody
    public Properties message(@RequestParam String lang) {
        Assert.notNull(lang, "locale must not be null");

        return messageService.getMessageListByLanguageCode(lang);
    }
    
    @RequestMapping("/code")
    public List<CodeDetail> code(){
        return codeFactoryBean.getObject();
    }    
}
