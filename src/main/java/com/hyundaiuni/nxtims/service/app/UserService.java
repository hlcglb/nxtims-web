package com.hyundaiuni.nxtims.service.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hyundaiuni.nxtims.domain.app.Auth;
import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;
import com.hyundaiuni.nxtims.support.mail.Address;
import com.hyundaiuni.nxtims.support.mail.VelocityEmailSender;

@Service
public class UserService implements UserDetailsService, MessageSourceAware {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/users";

    @Autowired
    private RestApiTemplate apiTemplate;

    @Autowired
    private VelocityEmailSender velocityEmailSender;

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "username must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        User user = apiTemplate.getRestTemplate().getForObject(resourceUrl + "/{id}", User.class, username);

        if(user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPwd(), user.isUse(),
            user.isNonExpired(), user.isNonPwdExpired(), user.isNonLocked(), getAuthorities(user));
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for(Auth auth : user.getAuthList()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + auth.getAuthId());
            authorities.add(grantedAuthority);
        }

        return authorities;
    }

    public void onAuthenticationSuccess(String userId, String sessionId, String accessIp) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(sessionId, "sessionId must not be null");
        Assert.notNull(sessionId, "accessIp must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("USER_ID", userId);
        parameter.put("SESSION_ID", sessionId);
        parameter.put("ACCESS_IP", accessIp);

        apiTemplate.getRestTemplate().postForEntity(resourceUrl + "/onAuthenticationSuccess", parameter, null);
    }

    public void onAuthenticationFailure(String userId) {
        Assert.notNull(userId, "userId must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("USER_ID", userId);

        apiTemplate.getRestTemplate().postForEntity(resourceUrl + "/onAuthenticationFailure", parameter, null);
    }

    public void onLogout(String userId, String sessionId) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(sessionId, "sessionId must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("USER_ID", userId);
        parameter.put("SESSION_ID", sessionId);

        apiTemplate.getRestTemplate().postForEntity(resourceUrl + "/onLogout", parameter, null);
    }

    public List<User> getUserListByParam(String query, int offset, int limit) {
        Assert.notNull(query, "parameter must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getUserListByParam");
        urlVariables.put("q", query);
        urlVariables.put("offset", offset);
        urlVariables.put("limit", limit);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&q={q}&offset={offset}&limit={limit}";

        List<User> UserList = new ArrayList<>();

        CollectionUtils.addAll(UserList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, User[].class, urlVariables));

        return UserList;
    }

    public User getUser(String userId) {
        Assert.notNull(userId, "userId must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{userId}";

        return apiTemplate.getRestTemplate().getForObject(resourceUrl, User.class, userId);
    }

    public User insertUser(User user) {
        Assert.notNull(user, "user must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        return apiTemplate.getRestTemplate().postForObject(resourceUrl, user, User.class);
    }

    public User updateUser(User user) {
        Assert.notNull(user, "user must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{userId}";

        apiTemplate.getRestTemplate().put(resourceUrl, user, user.getUserId());

        return getUser(user.getUserId());
    }

    public void deleteUser(String userId) {
        Assert.notNull(userId, "userId must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{userId}";

        apiTemplate.getRestTemplate().delete(resourceUrl, userId);
    }

    public void reissuePassword(String userId, String userNm, String email) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(userNm, "userNm must not be null");
        Assert.notNull(email, "email must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("USER_ID", userId);
        parameter.put("USER_NM", userNm);
        parameter.put("EMAIL", email);

        User user = apiTemplate.getRestTemplate().getForObject(resourceUrl + "/reissuePassword", User.class, parameter);

        Map<String, String> model = new HashMap<String, String>();
        model.put("userNm", user.getUserNm());
        model.put("userPwd", user.getUserPwd());

        String title = messageSource.getMessage("TITLE_REISSUE_PASSWORD", new String[] {user.getUserNm()},
            "The password of " + user.getUserNm() + "is reissued.", LocaleContextHolder.getLocale());

        velocityEmailSender.send(new Address[] {new Address(user.getEmail(), user.getUserNm())}, title,
            "reissuePassword.vm", "UTF-8", model, false);
    }
}
