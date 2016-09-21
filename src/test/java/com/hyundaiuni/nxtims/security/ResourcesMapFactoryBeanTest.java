package com.hyundaiuni.nxtims.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.hyundaiuni.nxtims.service.app.ResourceService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourcesMapFactoryBeanTest {
    @Autowired
    private ResourceService resourcesService;
    
    @Test
    public void testGetObject() {
        ResourcesMapFactoryBean factoryBean = new ResourcesMapFactoryBean();
        factoryBean.setResourceService(resourcesService);
        
        Exception ex = null;

        try {
            LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = factoryBean.getObject();

            if(CollectionUtils.isEmpty(result)) {
                fail("");
            }
        }
        catch(Exception e) {
            ex = e;
        }

        assertEquals(null, ex);        
    }     
}
