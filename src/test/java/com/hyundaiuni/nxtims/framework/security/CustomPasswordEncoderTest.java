package com.hyundaiuni.nxtims.framework.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CustomPasswordEncoderTest {
    @Test
    public void testEncode() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        StringBuilder builder = new StringBuilder();
        builder.append("admin");
        
        String encodedPasword = encoder.encode(builder.toString());
        
        assertThat(encodedPasword).isEqualTo("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");
    }
    
    @Test
    public void testMatches() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        StringBuilder builder = new StringBuilder();
        builder.append("admin");
        
        String encodedPassword = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";
        
        boolean isMatched = encoder.matches(builder.toString(),encodedPassword);
        
        assertThat(isMatched).isTrue();
    }      
}
