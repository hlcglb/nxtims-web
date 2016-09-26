package com.hyundaiuni.nxtims.service.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hyundaiuni.nxtims.domain.app.Auth;
import com.hyundaiuni.nxtims.domain.app.AuthResource;
import com.hyundaiuni.nxtims.domain.app.Resource;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;

@Service
public class AuthService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/auth";

    @Autowired
    private RestApiTemplate apiTemplate;

    public List<Auth> getAuthListByParam(String query, int offset, int limit) {
        Assert.notNull(query, "parameter must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getAuthListByParam");
        urlVariables.put("q", query);
        urlVariables.put("offset", offset);
        urlVariables.put("limit", limit);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&q={q}&offset={offset}&limit={limit}";

        List<Auth> authList = new ArrayList<>();

        CollectionUtils.addAll(authList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, Auth[].class, urlVariables));

        return authList;
    }

    public List<AuthResource> getAuthResourceListByAuthId(String authId) {
        Assert.notNull(authId, "authId must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getAuthResourceListByAuthId");
        urlVariables.put("authId", authId);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&authId={authId}";

        List<AuthResource> authResourceList = new ArrayList<>();

        CollectionUtils.addAll(authResourceList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, AuthResource[].class, urlVariables));

        return authResourceList;
    }
    
    public List<Resource> getNotExistsAuthResourceListByAuthId(String authId) {
        Assert.notNull(authId, "authId must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getNotExistsAuthResourceListByAuthId");
        urlVariables.put("authId", authId);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&authId={authId}";

        List<Resource> resourceList = new ArrayList<>();

        CollectionUtils.addAll(resourceList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, Resource[].class, urlVariables));

        return resourceList;
    }    

    public Auth getAuth(String authId) {
        Assert.notNull(authId, "authId must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{authId}";

        return apiTemplate.getRestTemplate().getForObject(resourceUrl, Auth.class, authId);
    }

    public Auth insertAuth(Auth auth) {
        Assert.notNull(auth, "auth must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        return apiTemplate.getRestTemplate().postForObject(resourceUrl, auth, Auth.class);
    }

    public Auth updateAuth(Auth auth) {
        Assert.notNull(auth, "auth must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{authId}";

        apiTemplate.getRestTemplate().put(resourceUrl, auth, auth.getAuthId());

        return getAuth(auth.getAuthId());
    }

    public void deleteAuth(String authId) {
        Assert.notNull(authId, "authId must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{authId}";

        apiTemplate.getRestTemplate().delete(resourceUrl, authId);
    }
}
