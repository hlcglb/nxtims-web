package com.hyundaiuni.nxtims.service.app;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import com.hyundaiuni.nxtims.service.RestApiTemplate;

@Service
public class SecuredObjectService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/nxtims/v1/resources/";

    @Autowired
    private RestApiTemplate apiTemplate;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getRolesAndUrl() throws Exception {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();

        LinkedHashMap<Object, List<ConfigAttribute>> data = getRolesAndResources("url");

        Set<Object> keys = data.keySet();

        for(Object key : keys) {
            result.put((AntPathRequestMatcher)key, data.get(key));
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public LinkedHashMap<Object, List<ConfigAttribute>> getRolesAndResources(String resourceType) throws Exception {
        String resourceUrl = apiServerUrl + apiUrl;

        LinkedHashMap<Object, List<ConfigAttribute>> resourcesMap = new LinkedHashMap<Object, List<ConfigAttribute>>();

        List<Map<String, Object>> resultList = apiTemplate.getRestTemplate().getForObject(resourceUrl, List.class);

        boolean isResourcesUrl = true;

        if("url".equals(resourceType)) {
            isResourcesUrl = true;
        }
        else {
            isResourcesUrl = false;
        }

        Iterator<Map<String, Object>> itr = resultList.iterator();
        Map<String, Object> tempMap;
        String preResource = null;
        String presentResourceStr;
        Object presentResource;

        while(itr.hasNext()) {
            tempMap = itr.next();

            presentResourceStr = (String)tempMap.get(resourceType);
            // url 인 경우 RequestKey 형식의 key를 Map에 담아야 함
            presentResource = isResourcesUrl ? new AntPathRequestMatcher(presentResourceStr) : presentResourceStr;
            List<ConfigAttribute> configList = new LinkedList<ConfigAttribute>();

            // 이미 requestMap 에 해당 Resource 에 대한 Role 이 하나 이상 맵핑되어 있었던 경우,
            // sort_order 는 resource(Resource) 에 대해 매겨지므로 같은 Resource 에 대한 Role 맵핑은 연속으로 조회됨.
            // 해당 맵핑 Role List (SecurityConfig) 의 데이터를 재활용하여 새롭게 데이터 구축
            if(preResource != null && presentResourceStr.equals(preResource)) {
                List<ConfigAttribute> preAuthList = resourcesMap.get(presentResource);
                Iterator<ConfigAttribute> preAuthItr = preAuthList.iterator();
                while(preAuthItr.hasNext()) {
                    SecurityConfig tempConfig = (SecurityConfig)preAuthItr.next();
                    configList.add(tempConfig);
                }
            }

            configList.add(new SecurityConfig((String)tempMap.get("authority")));

            // 만약 동일한 Resource 에 대해 한개 이상의 Role 맵핑 추가인 경우
            // 이전 resourceKey 에 현재 새로 계산된 Role 맵핑 리스트로 덮어쓰게 됨.
            resourcesMap.put(presentResource, configList);

            // 이전 resource 비교위해 저장
            preResource = presentResourceStr;
        }

        return resourcesMap;
    }
}
