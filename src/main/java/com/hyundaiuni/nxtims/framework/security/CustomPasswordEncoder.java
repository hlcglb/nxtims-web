package com.hyundaiuni.nxtims.framework.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hyundaiuni.nxtims.framework.helper.MessageDigestHelper;

public class CustomPasswordEncoder implements PasswordEncoder {
    private static final Log log = LogFactory.getLog(CustomPasswordEncoder.class);

    private static final String ALGORITHM = "SHA-256";
    private static final String CHARSET_NAME = "UTF-8";

    @Override
    public String encode(CharSequence rawPassword) {
        String encodedPassword = null;

        try {
            encodedPassword = MessageDigestHelper.getMessageDigest(rawPassword.toString(), ALGORITHM, CHARSET_NAME);
        }
        catch(Exception e) {
            log.error("Password Encoding Exception : ", e);
        }

        return encodedPassword;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if(encodedPassword.equals(encode(rawPassword))) {
            return true;
        }

        return false;
    }

}
