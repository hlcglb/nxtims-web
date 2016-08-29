package com.hyundaiuni.nxtims.service.app;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyundaiuni.nxtims.service.RestApiTemplate;

@Service
public class ResourcesService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/resources/";

    @Autowired
    private RestApiTemplate apiTemplate;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getRolesAndUrl() throws Exception {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();

        String resourceUrl = apiServerUrl + apiUrl;

        @SuppressWarnings("unchecked")
        List<Map<String, String>> authResourceList = apiTemplate.getRestTemplate().getForObject(resourceUrl,
            List.class);

        if(!CollectionUtils.isEmpty(authResourceList)) {
            for(Map<String, String> authResource : authResourceList) {
                AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(
                    MapUtils.getString(authResource, "RESOURCE_URL"), null, true);

                List<ConfigAttribute> configList = null;

                if(result.containsKey(requestMatcher)) {
                    configList = MapUtils.getObject(result, requestMatcher);
                }
                else {
                    configList = new LinkedList<ConfigAttribute>();
                }

                configList.add(new SecurityConfig("ROLE_" + MapUtils.getString(authResource, "AUTH_ID")));
                result.put(requestMatcher, configList);
            }
        }

        return result;
    }

}
