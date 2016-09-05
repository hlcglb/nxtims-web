package com.hyundaiuni.nxtims.service.app;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyundaiuni.nxtims.domain.app.AuthResource;
import com.hyundaiuni.nxtims.service.RestApiTemplate;

@Service
public class ResourceService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/resources";

    @Autowired
    private RestApiTemplate apiTemplate;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getRolesAndUrl() throws Exception {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();

        String resourceUrl = apiServerUrl + apiUrl;

        List<AuthResource> authResourceList = Arrays.asList(
            apiTemplate.getRestTemplate().getForObject(resourceUrl, AuthResource[].class));

        if(!CollectionUtils.isEmpty(authResourceList)) {
            for(AuthResource authResource : authResourceList) {
                AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(authResource.getResourceUrl(), null,
                    true);

                List<ConfigAttribute> configList = null;

                if(result.containsKey(requestMatcher)) {
                    configList = MapUtils.getObject(result, requestMatcher);
                }
                else {
                    configList = new LinkedList<ConfigAttribute>();
                }

                configList.add(new SecurityConfig("ROLE_" + authResource.getAuthId()));
                result.put(requestMatcher, configList);
            }
        }

        return result;
    }
}
