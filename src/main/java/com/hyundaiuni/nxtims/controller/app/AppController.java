package com.hyundaiuni.nxtims.controller.app;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.hyundaiuni.nxtims.domain.app.Notice;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.service.app.AppService;
import com.hyundaiuni.nxtims.service.app.MessageService;
import com.hyundaiuni.nxtims.service.app.NoticeService;
import com.hyundaiuni.nxtims.support.CodeMessageSourceHandler;
import com.hyundaiuni.nxtims.util.WebUtils;

@RestController
@RequestMapping("/api/login")
public class AppController {
    @Autowired
    private AppService appService;

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CodeMessageSourceHandler codeMessageSourceHandler;

    @RequestMapping("/authentication")
    public Principal authentication(Principal user) {
        return user;
    }

    @RequestMapping("/resource")
    public Map<String, Object> resource(Principal user) {
        Assert.notNull(user, "user must not be null");
        
        String userId = user.getName();

        Map<String, Object> model = appService.getResourceByUserId(userId);
        return model;
    }

    @RequestMapping("/message")
    @ResponseBody
    public Properties message(@RequestParam("lang") String language) {
        Assert.notNull(language, "language must not be null");

        return messageService.getMessageListByLanguageCode(language);
    }

    @RequestMapping("/code")
    public List<CodeDetail> code() {
        return codeMessageSourceHandler.getCodeDetailAll();
    }
    
    @RequestMapping("/notice")
    public List<Notice> notice() {
        int offset = 0;
        int limit = 5;
        
        SimpleDateFormat sm = new SimpleDateFormat("yyyymmdd");
        String currentYmd = sm.format(new Date());
        
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("openYmd", currentYmd);
        parameter.put("closeYmd", currentYmd);

        String query;
        
        try {
            query = WebUtils.mapToRequestParam(parameter, ',', '=', "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
            throw new ServiceException("ENCODE_NOT_SUPPORTED", e.getMessage(), null, e);
        }        
        
        return noticeService.getNoticeListByParam(query, offset, limit);
    }    
}
