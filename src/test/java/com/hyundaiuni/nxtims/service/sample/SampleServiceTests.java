package com.hyundaiuni.nxtims.service.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleServiceTests {
    @Autowired
    private SampleService sampleService;

    @Test
    public void testGet() {
        Map<String, Object> resultMap = sampleService.get("19850003");
        assertThat(resultMap).isNotEmpty();
    }

    @Test
    public void testInsert() {
        Exception ex = null;
        
        Map<String, Object> params = new HashMap<>();
        params.put("REMARK", "SPRING INSERT 테스트");

        try {
            sampleService.insert(params);
        }
        catch(Exception e) {
            ex = e;
        }
        
        assertEquals(null, ex);
    }
}
