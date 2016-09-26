package com.hyundaiuni.nxtims.service.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hyundaiuni.nxtims.domain.app.AuthResource;
import com.hyundaiuni.nxtims.domain.app.Resource;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;

@Service
public class ResourceService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/resources";

    @Autowired
    private RestApiTemplate apiTemplate;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getRolesAndUrl() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry=getResourceAuthAll";

        List<AuthResource> authResourceList = new ArrayList<>();

        CollectionUtils.addAll(authResourceList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, AuthResource[].class));

        if(CollectionUtils.isNotEmpty(authResourceList)) {
            for(AuthResource authResource : authResourceList) {
                AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(authResource.getResourceUrl(),
                    authResource.getHttpMethod(), true);

                List<ConfigAttribute> configList;

                if(result.containsKey(requestMatcher)) {
                    configList = MapUtils.getObject(result, requestMatcher);
                }
                else {
                    configList = new LinkedList<>();
                }

                configList.add(new SecurityConfig("ROLE_" + authResource.getAuthId()));
                result.put(requestMatcher, configList);
            }
        }

        return result;
    }

    public List<Resource> getResourceListByParam(String query, int offset, int limit) {
        Assert.notNull(query, "parameter must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getResourceListByParam");
        urlVariables.put("q", query);
        urlVariables.put("offset", offset);
        urlVariables.put("limit", limit);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&q={q}&offset={offset}&limit={limit}";

        List<Resource> resourceList = new ArrayList<>();

        CollectionUtils.addAll(resourceList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, Resource[].class, urlVariables));

        return resourceList;
    }

    public Resource getResource(String resourceId) {
        Assert.notNull(resourceId, "resourceId must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{resourceId}";

        return apiTemplate.getRestTemplate().getForObject(resourceUrl, Resource.class, resourceId);
    }

    public Resource insertResource(Resource resource) {
        Assert.notNull(resource, "resource must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        return apiTemplate.getRestTemplate().postForObject(resourceUrl, resource, Resource.class);
    }

    public Resource updateResource(Resource resource) {
        Assert.notNull(resource, "resource must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{resourceId}";

        apiTemplate.getRestTemplate().put(resourceUrl, resource, resource.getResourceId());

        return getResource(resource.getResourceId());
    }

    public void deleteResource(String resourceId) {
        Assert.notNull(resourceId, "resourceId must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{resourceId}";

        apiTemplate.getRestTemplate().delete(resourceUrl, resourceId);
    }

    public void saveResources(List<Resource> resourceList) {
        if(CollectionUtils.isNotEmpty(resourceList)) {
            String resourceUrl = apiServerUrl + apiUrl + "/save";

            apiTemplate.getRestTemplate().postForEntity(resourceUrl, resourceList, void.class);
        }
    }
}
