package com.hyundaiuni.nxtims.controller.framework;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @RequestMapping("/authentication")
    public Principal user(Principal user) {
      return user;
    }
    
    @RequestMapping("/resource")
    public Map<String,Object> main() {
        Map<String,Object> model = new HashMap<String,Object>();
        return model;
      }    
}
