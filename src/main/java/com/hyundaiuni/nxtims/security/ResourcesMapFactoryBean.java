package com.hyundaiuni.nxtims.security;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.hyundaiuni.nxtims.service.app.ResourcesService;

public class ResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {
    private ResourcesService resourcesService;
    
    public void setResourcesService(ResourcesService resourcesService){
        this.resourcesService = resourcesService;
    }

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    @PostConstruct
    public void init() throws Exception {
        requestMap = resourcesService.getRolesAndUrl();
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if(requestMap == null) {
            requestMap = resourcesService.getRolesAndUrl();
        }
        return requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
