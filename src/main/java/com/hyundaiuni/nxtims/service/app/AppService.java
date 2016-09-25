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

import com.hyundaiuni.nxtims.domain.app.Resource;
import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;

@Service
public class AppService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/users";

    @Autowired
    private RestApiTemplate apiTemplate;

    public Map<String, Object> getResourceByUserId(String userId) {
        Assert.notNull(userId, "userId must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);

        User user = apiTemplate.getRestTemplate().getForObject(resourceUrl + "/{id}", User.class, params);

        if(user == null) {
            throw new ServiceException("USER_NOT_FOUND", "Application does not find users", null);
        }

        List<Resource> menuList = new ArrayList<>();

        CollectionUtils.addAll(menuList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl + "/menus/{id}", Resource[].class, params));

        Map<String, Object> result = new HashMap<>();
        result.put("USER", user);
        result.put("MENU_LIST", menuList);

        return result;
    }
}
