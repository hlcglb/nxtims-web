package com.hyundaiuni.nxtims.service.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.hyundaiuni.nxtims.domain.app.Resource;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.util.WebUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServiceTest {
    private static final Log log = LogFactory.getLog(ResourceServiceTest.class);
    
    @Autowired
    private ResourceService resourceService;

    @Test
    public void testGetRolesAndUrl() {
        Exception ex = null;

        try {
            LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = resourceService.getRolesAndUrl();

            if(CollectionUtils.isEmpty(result)) {
                fail("");
            }

            AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/", null, true);
            
            if(!result.containsKey(requestMatcher)) {
                fail("");
            }            
        }
        catch(Exception e) {
            ex = e;
        }

        assertEquals(null, ex);
    }
    

    @Test
    public void testGetResourceListByParam() {
        Exception ex = null;

        try {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("resourceNm", "SAMPLE");
            
            String requestString = WebUtils.mapToRequestParam(parameter, ',', '=', "UTF-8");

            resourceService.getResourceListByParam(requestString, 0, 10);
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }

    @Test
    public void testGetResourceById() {
        Exception ex = null;

        try {
            resourceService.getResourceById("000000");
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }

    @Test
    public void testInsertResource() {
        Exception ex = null;

        try {
            Resource resource = new Resource();

            resource.setResourceNm("TEST");

            resourceService.insertResource(resource);
        }
        catch(Exception e) {
            log.error(e.getMessage());
            assertThat(e).isInstanceOf(ServiceException.class).hasMessageContaining("not be null");
        }

        try {
            Resource resource = new Resource();

            resource.setResourceNm("TEST");
            resource.setResourceType("03");
            resource.setResourceUrl("---");
            resource.setUseYn("Y");

            resource = resourceService.insertResource(resource);

            resourceService.deleteResource(resource.getResourceId());
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }

    @Test
    public void testUpdateResource() {
        Exception ex = null;

        try {
            Resource resource = new Resource();

            resource.setResourceNm("TEST");
            resource.setResourceType("03");
            resource.setResourceUrl("---");
            resource.setUseYn("Y");

            resource = resourceService.insertResource(resource);

            resource.setResourceNm("TEST1");

            resourceService.updateResource(resource.getResourceId(), resource);

            resourceService.deleteResource(resource.getResourceId());
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testSaveResource() {
        Exception ex = null;

        try {
            List<Resource> resourceList = new ArrayList<>();
            
            Resource updateResource = new Resource();

            updateResource.setResourceId("000007");
            updateResource.setResourceNm("SAMPLE");
            updateResource.setResourceType("02");
            updateResource.setResourceUrl("/sample/****");
            updateResource.setUseYn("Y");
            updateResource.setTransactionType("U");
            
            resourceList.add(updateResource);
            
            Resource updateResource2 = new Resource();

            updateResource2.setResourceId("000007");
            updateResource2.setResourceNm("SAMPLE");
            updateResource2.setResourceType("02");
            updateResource2.setResourceUrl("/sample/**");
            updateResource2.setUseYn("Y");
            updateResource2.setTransactionType("U");
            
            resourceList.add(updateResource2);            
            
            resourceService.saveResources(resourceList);
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    } 
}
