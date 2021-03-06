package com.hyundaiuni.nxtims.controller.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppRedirectController {
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
    
    @RequestMapping(value = "/view/**")
    public String program() {
        return "forward:/";
    }

}
