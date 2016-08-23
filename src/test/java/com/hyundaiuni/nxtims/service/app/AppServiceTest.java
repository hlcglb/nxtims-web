package com.hyundaiuni.nxtims.service.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hyundaiuni.nxtims.domain.app.Resource;
import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppServiceTest {
    @Autowired
    private AppService appService;

    @Test
    public void testGetResourceByUserId() {
        try {
            appService.getResourceByUserId("20006X");
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(ServiceException.class).hasMessageContaining("not found");
        }

        Exception ex = null;
        
        try {
            Map<String, Object> result = appService.getResourceByUserId("200065");
            User user = (User)MapUtils.getObject(result,"USER");
            
            if(user == null){
                fail("");
            }
            
            @SuppressWarnings("unchecked")
            List<Resource> menuList = (List<Resource>)MapUtils.getObject(result,"MENU_LIST");
            
            if(CollectionUtils.isEmpty(menuList)){
                fail("");
            }
        }
        catch(Exception e) {
            ex = e;
        }
        
        assertEquals(null, ex);
    }
}
