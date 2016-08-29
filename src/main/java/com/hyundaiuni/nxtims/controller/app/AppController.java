package com.hyundaiuni.nxtims.controller.app;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyundaiuni.nxtims.service.app.AppService;

@RestController
public class AppController {
    @Autowired
    private AppService appService;

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
}
