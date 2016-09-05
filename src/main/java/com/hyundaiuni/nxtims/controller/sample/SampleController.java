package com.hyundaiuni.nxtims.controller.sample;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyundaiuni.nxtims.service.sample.SampleService;

@RestController
@RequestMapping("/sample")
public class SampleController implements MessageSourceAware {
    @Autowired
    private SampleService sampleService;
    
    private MessageSource messageSource;
    
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> get(@PathVariable("id") String id) {
        return sampleService.get(id);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public String getMessage(@RequestParam("msgCd") String msgCd) {
        String message = messageSource.getMessage(msgCd, null, Locale.getDefault());
        return message;
    }
}
