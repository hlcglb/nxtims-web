package com.hyundaiuni.nxtims.framework.security;

import java.security.MessageDigest;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
    private static final String algorithm = "SHA-256";
    private static final String charsetName = "UTF-8";

    @Override
    public String encode(CharSequence rawPassword) {
        MessageDigest messageDigest = null;
        
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(rawPassword.toString().getBytes(charsetName));
        }
        catch(Exception e) {
            e.printStackTrace();
            
            return null;
        }
        
        byte byteData[] = messageDigest.digest();

        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < byteData.length; i++) {
            stringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return stringBuffer.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if(encodedPassword.equals(encode(rawPassword))){
            return true;
        }
        
        return false;
    }

}
