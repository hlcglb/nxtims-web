package com.hyundaiuni.nxtims.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundaiuni.nxtims.domain.app.Resource;

public class ResourceTest {
    @Test
    public void testObjectToJson() {
        Exception ex = null;
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            Resource resource = new Resource();
            resource.setResourceLevel("1");
            resource.setResourceId("000000");
            resource.setResourceNm("컨테이너운송");
            resource.setResourceUrl("");
            resource.setResourceType("01");

            String json = mapper.writeValueAsString(resource);
            
            assertThat(json).contains("RESOURCE_ID");
        }
        catch(Exception e) {
            ex = e;
        }
        
        assertEquals(null, ex);
    }
    
    @Test
    public void testJsonToObject() {
        Exception ex = null;
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            String json = "{\"RESOURCE_LEVEL\":\"1\",\"RESOURCE_ID\":\"000000\",\"RESOURCE_NM\":\"컨테이너운송\",\"RESOURCE_URL\":\"\",\"RESOURCE_TYPE\":\"01\"}";
            
            Resource resource = mapper.readValue(json,Resource.class);
            
            System.out.println(resource.toString());
        }
        catch(Exception e) {
            ex = e;
        }
        
        assertEquals(null, ex);
    } 
}
