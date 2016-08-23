package com.hyundaiuni.nxtims.service.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.hyundaiuni.nxtims.domain.app.Resource;
import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.service.RestApiTemplate;

@Service
public class AppService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/users";

    @Autowired
    private RestApiTemplate apiTemplate;

    @SuppressWarnings("unchecked")
    public Map<String, Object> getResourceByUserId(String userId) {
        Assert.notNull(userId, "userId must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);

        User user = apiTemplate.getRestTemplate().getForObject(resourceUrl + "/{id}", User.class, params);

        if(user == null) {
            throw new ServiceException("USER_NOT_FOUND", userId + " not found.");
        }

        List<Resource> menuList = apiTemplate.getRestTemplate().getForObject(resourceUrl + "/menus/{id}", List.class,
            params);

        if(CollectionUtils.isEmpty(menuList)) {
            throw new ServiceException("MENU_NOT_FOUND", "The menus of " + userId + " not found.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("USER", user);
        result.put("MENU_LIST", menuList);

        return result;
    }
}
