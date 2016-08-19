package com.hyundaiuni.nxtims.framework.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsrfHeaderFilterTest {
    private CsrfHeaderFilter headerFilter;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    HttpSession session;

    @Mock
    CsrfToken token;

    @Before
    public void setUp() {
        headerFilter = new CsrfHeaderFilter();
    }

    @Test
    public void test() {
        Exception ex = null;

        try {
            headerFilter.doFilterInternal(request, response, filterChain);

            CsrfToken csrf = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
            
            if(csrf != null){
                fail("");
            }
            
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            mockHttpServletRequest.setAttribute(CsrfToken.class.getName(), token);
            
            MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
            
            headerFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, filterChain);
            csrf = (CsrfToken)mockHttpServletRequest.getAttribute(CsrfToken.class.getName());
            
            if(csrf == null){
                fail("");
            }            
        }
        catch(ServletException e) {
            ex = e;
        }
        catch(IOException e) {
            ex = e;
        }

        assertEquals(null, ex);
    }
}
