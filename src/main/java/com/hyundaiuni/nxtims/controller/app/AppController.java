package com.hyundaiuni.nxtims.controller.app;

import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hyundaiuni.nxtims.service.app.AppService;
import com.hyundaiuni.nxtims.service.app.MessageService;

@RestController
public class AppController {
    @Autowired
    private AppService appService;

    @Autowired
    private MessageService messageService;

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
    public Properties message(@RequestParam String locale) {
        Assert.notNull(locale, "locale must not be null");

        return messageService.getMessageProperties(locale);
    }
}
