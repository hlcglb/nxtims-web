package com.hyundaiuni.nxtims.controller.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyundaiuni.nxtims.domain.app.Message;
import com.hyundaiuni.nxtims.service.app.MessageService;

@RestController
@RequestMapping("/app/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(params = "inquiry=getMessageListByLanguageCode", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageListByLanguageCode(@RequestParam("languageCode") String languageCode) {
        Assert.notNull(languageCode, "languageCode must not be null");

        return new ResponseEntity<>(messageService.getMessageListByLanguageCode(languageCode), HttpStatus.OK);
    }

    @RequestMapping(params = "inquiry=getMessageListByParam", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageListByParam(@RequestParam("q") String query, @RequestParam("offset") int offset,
        @RequestParam("limit") int limit) {
        Assert.notNull(query, "query must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        return new ResponseEntity<>(messageService.getMessageListByParam(query, offset, limit), HttpStatus.OK);
    }

    @RequestMapping(params = "inquiry=getMessageLocaleListByParam", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageLocaleListByParam(@RequestParam("msgGrpCd") String msgGrpCd,
        @RequestParam("msgCd") String msgCd) {
        Assert.notNull(msgGrpCd, "msgGrpCd must not be null");
        Assert.notNull(msgCd, "msgCd must not be null");

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("msgGrpCd", msgGrpCd);
        parameter.put("msgCd", msgCd);

        return new ResponseEntity<>(messageService.getMessageLocaleListByParam(parameter), HttpStatus.OK);
    }

    @RequestMapping(value = "/{msgPk}", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageByMsgPk(@PathVariable("msgPk") String msgPk) {
        Assert.notNull(msgPk, "msgPk must not be null");

        return new ResponseEntity<>(messageService.getMessageByMsgPk(msgPk), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insertMessage(@RequestBody Message message) {
        return new ResponseEntity<>(messageService.insertMessage(message), HttpStatus.OK);
    }

    @RequestMapping(value = "/{msgPk}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMessage(@PathVariable("msgPk") String msgPk, @RequestBody Message message) {
        messageService.updateMessage(msgPk, message);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{msgPk}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMessage(@PathVariable("msgPk") String msgPk) {
        messageService.deleteMessage(msgPk);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
