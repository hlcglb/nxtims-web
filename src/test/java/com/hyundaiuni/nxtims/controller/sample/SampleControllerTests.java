package com.hyundaiuni.nxtims.controller.sample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleControllerTests {
    private static final String URL = "/sample";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get(URL + "/1")).andDo(print()).andExpect(status().isOk()).andExpect(
            content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.message").exists());
        
        mvc.perform(get(URL + "/19850003")).andDo(print()).andExpect(status().isOk()).andExpect(
            content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.EMP_NO").value("19850003"));
    }
}
