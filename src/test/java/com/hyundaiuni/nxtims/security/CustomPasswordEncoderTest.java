package com.hyundaiuni.nxtims.security;

import com.hyundaiuni.nxtims.framework.security.CustomPasswordEncoder;

public class CustomPasswordEncoderTest {

    public static void main(String[] args) {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        StringBuilder builder = new StringBuilder();
        builder.append("admin");
        
        System.out.println(encoder.encode(builder.toString()));
    }

}
