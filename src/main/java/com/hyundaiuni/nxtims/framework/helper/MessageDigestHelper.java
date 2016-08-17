package com.hyundaiuni.nxtims.framework.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.hyundaiuni.nxtims.framework.exception.MessageDigestException;

public class MessageDigestHelper {
    private MessageDigestHelper() {}

    public static String getMessageDigest(String input, String algorithm, String charsetName)
        throws MessageDigestException {
        MessageDigest messageDigest = null;
        StringBuilder stringBuilder = null;

        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(input.getBytes(charsetName));
            byte byteData[] = messageDigest.digest();

            stringBuilder = new StringBuilder();

            for(int i = 0; i < byteData.length; i++) {
                stringBuilder.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        }
        catch(NoSuchAlgorithmException e) {
            throw new MessageDigestException("NoSuchAlgorithmException : " + e.getMessage());
        }
        catch(UnsupportedEncodingException e) {
            throw new MessageDigestException("UnsupportedEncodingException : " + e.getMessage());
        }

        return stringBuilder.toString();
    }

    public static String encodeBase64(byte[] binaryData) {
        return new String(Base64.encodeBase64(binaryData));
    }

    public static String encodeBase64(byte[] binaryData, String charsetName) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(binaryData), charsetName);
    }

    public static String decodeBase64(byte[] binaryData) {
        return new String(Base64.decodeBase64(binaryData));
    }

    public static String decodeBase64(byte[] binaryData, String charsetName) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(binaryData), charsetName);
    }
}
