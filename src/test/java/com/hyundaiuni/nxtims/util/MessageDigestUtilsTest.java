package com.hyundaiuni.nxtims.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.hyundaiuni.nxtims.exception.MessageDigestException;

public class MessageDigestUtilsTest {
    private static final Log log = LogFactory.getLog(MessageDigestUtilsTest.class);

    @Test
    public void testGetMessageDigest() {
        try {
            MessageDigestUtils.getMessageDigest("111", "SHA", "UTF-8");
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(MessageDigestException.class).hasMessageContaining("NoSuchAlgorithmException");
        }

        try {
            MessageDigestUtils.getMessageDigest("111", "SHA-256", "UTF-1");
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(MessageDigestException.class).hasMessageContaining(
                "UnsupportedEncodingException");
        }
    }

    @Test
    public void testEncodeBase64() {
        String encodedStr = new String(MessageDigestUtils.encodeBase64("A=B".getBytes()));

        assertEquals(encodedStr, "QT1C");
    }

    @Test
    public void testEncodeBase64WithCharSet() {
        String encodedStr = null;

        try {
            encodedStr = new String(MessageDigestUtils.encodeBase64("A=B".getBytes(), "UTF-8"));
        }
        catch(UnsupportedEncodingException e) {
            log.error(e.getMessage());
            encodedStr = null;
        }

        assertEquals(encodedStr, "QT1C");

        try {
            new String(MessageDigestUtils.encodeBase64("A=B".getBytes(), "UTF-1"));
        }
        catch(UnsupportedEncodingException e) {
            assertThat(e).isInstanceOf(UnsupportedEncodingException.class);
        }
    }

    @Test
    public void testDecodeBase64() {
        String encodedStr = new String(MessageDigestUtils.decodeBase64("QT1C".getBytes()));

        assertEquals(encodedStr, "A=B");
    }

    @Test
    public void testDecodeBase64WithCharSet() {
        String encodedStr = null;

        try {
            encodedStr = new String(MessageDigestUtils.decodeBase64("QT1C".getBytes(), "UTF-8"));
        }
        catch(UnsupportedEncodingException e) {
            log.error(e.getMessage());
            encodedStr = null;
        }

        assertEquals(encodedStr, "A=B");

        try {
            new String(MessageDigestUtils.decodeBase64("A=B".getBytes(), "UTF-1"));
        }
        catch(UnsupportedEncodingException e) {
            assertThat(e).isInstanceOf(UnsupportedEncodingException.class);
        }
    }
}
