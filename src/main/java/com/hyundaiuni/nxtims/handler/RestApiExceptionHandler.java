package com.hyundaiuni.nxtims.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hyundaiuni.nxtims.exception.ServiceException;

@ControllerAdvice
public class RestApiExceptionHandler implements MessageSourceAware {
    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();

        error.put("CODE", "UNDEFINED");
        error.put("MESSAGE", e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleServiceException(ServiceException e) {
        Map<String, String> error = new HashMap<>();

        error.put("CODE", e.getCode());
        error.put("MESSAGE",
            messageSource.getMessage(e.getCode(), null, e.getMessage(), LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
