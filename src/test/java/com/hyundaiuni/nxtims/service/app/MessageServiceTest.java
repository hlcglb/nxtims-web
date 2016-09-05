package com.hyundaiuni.nxtims.service.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {
    @Autowired
    private MessageService messageService;
    
    @Test
    public void testGetMessageProperties() {
        Exception ex = null;
        
        try {
            messageService.getMessageProperties("ko_KR");
        }
        catch(Exception e) {
            ex = e;
        }
        
        assertEquals(null, ex);
    }    
}
