package com.hyundaiuni.nxtims.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.hyundaiuni.nxtims.exception.MessageDigestException;
import com.hyundaiuni.nxtims.helper.MessageDigestHelper;

public class MessageDigestHelperTest {
    @Test
    public void testGetMessageDigest() {
        try {
            MessageDigestHelper.getMessageDigest("111", "SHA", "UTF-8");
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(MessageDigestException.class).hasMessageContaining("NoSuchAlgorithmException");
        }
        
        try {
            MessageDigestHelper.getMessageDigest("111", "SHA-256", "UTF-1");
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(MessageDigestException.class).hasMessageContaining("UnsupportedEncodingException");
        }        
    }
    
    @Test
    public void testEncodeBase64() {
        String encodedStr = new String(MessageDigestHelper.encodeBase64("A=B".getBytes()));

        assertEquals(encodedStr, "QT1C");
    }
    
    @Test
    public void testEncodeBase64WithCharSet() {
        String encodedStr = null;

        try {
            encodedStr = new String(MessageDigestHelper.encodeBase64("A=B".getBytes(),"UTF-8"));
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assertEquals(encodedStr, "QT1C");
        
        try {
            encodedStr = new String(MessageDigestHelper.encodeBase64("A=B".getBytes(),"UTF-1"));
        }
        catch(UnsupportedEncodingException e) {
            assertThat(e).isInstanceOf(UnsupportedEncodingException.class);
        }        
    }
    
    @Test
    public void testDecodeBase64() {
        String encodedStr = new String(MessageDigestHelper.decodeBase64("QT1C".getBytes()));

        assertEquals(encodedStr, "A=B");
    }
    
    @Test
    public void testDecodeBase64WithCharSet() {
        String encodedStr = null;

        try {
            encodedStr = new String(MessageDigestHelper.decodeBase64("QT1C".getBytes(),"UTF-8"));
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assertEquals(encodedStr, "A=B");
        
        try {
            encodedStr = new String(MessageDigestHelper.decodeBase64("A=B".getBytes(),"UTF-1"));
        }
        catch(UnsupportedEncodingException e) {
            assertThat(e).isInstanceOf(UnsupportedEncodingException.class);
        }        
    }    
}
