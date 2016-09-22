package com.hyundaiuni.nxtims.controller.app;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hyundaiuni.nxtims.domain.app.Auth;
import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.util.WebUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {
    private static final Log log = LogFactory.getLog(UserControllerTests.class);
    
    private static final String URL = "/api/app/users";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        Exception ex = null;

        try {
            this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testGetUserListByParam() {
        Exception ex = null;

        try {
            Map<String, Object> parameter = new HashMap<>();

            String query = WebUtils.mapToRequestParam(parameter, ',', '=', "UTF-8");

            mvc.perform(get(URL + "?inquiry=getUserListByParam&q=" + query + "&offset=0&limit=10")).andDo(
                print()).andExpect(status().isOk()).andExpect(
                    content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
                        jsonPath("$..USER_ID").isArray());
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }    

    @Test
    public void testGetUser() {
        Exception ex = null;

        try {
            mvc.perform(get(URL + "/test")).andDo(print()).andExpect(status().isOk()).andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.USER_ID").value("test"));
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testCUDUser(){
        Exception ex = null;

        try {
            User user = new User();
            
            user.setUserId("XXXXXX");
            user.setUserNm("XXXXXX");
            user.setUserPwd("12345qwert");
            user.setEmail("xxxxxx@hyundai-uni.com");
            user.setUseYn("Y");
            
            Auth auth = new Auth();
            auth.setAuthId("ANONYMOUS");
            
            List<Auth> authList = new ArrayList<>();
            authList.add(auth);
            
            user.setAuthList(authList);

            MvcResult result = mvc.perform(
                post(URL).contentType(MediaType.APPLICATION_JSON_UTF8).content(JsonTestUtils.jsonStringFromObject(user))).andDo(
                    print()).andExpect(status().isOk()).andExpect(
                        content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
                            jsonPath("$.USER_ID").value("XXXXXX")).andReturn();

            log.info(result.getResponse().getContentAsString());

            String query = result.getResponse().getContentAsString();

            User retrieveUser = JsonTestUtils.jsonStringToObject(query, User.class);

            log.info(retrieveUser.getUserId());

            String userId = retrieveUser.getUserId();

            user.setUserPwd("qwert12345");
            
            authList = user.getAuthList();
            
            if(CollectionUtils.isNotEmpty(authList)) {
                for(Auth tempAuth : authList) {
                    tempAuth.setTransactionType("D");
                }
            }
            
            Auth auth1 = new Auth();
            auth1.setAuthId("EMPLOYEE");
            auth1.setTransactionType("C");
            
            authList.add(auth1);
            
            user.setAuthList(authList);            

            mvc.perform(put(URL + "/{userId}", userId).contentType(MediaType.APPLICATION_JSON_UTF8).content(
                JsonTestUtils.jsonStringFromObject(user))).andDo(print()).andExpect(status().isOk());

            mvc.perform(delete(URL + "/{userId}", userId)).andDo(print()).andExpect(status().isOk());
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);        
    }    
}
