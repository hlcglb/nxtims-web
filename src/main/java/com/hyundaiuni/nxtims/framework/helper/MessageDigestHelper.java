package com.hyundaiuni.nxtims.framework.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class MessageDigestHelper {
    public static String getMessageDigest(String input, String algorithm, String charsetName)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(input.getBytes(charsetName));
        byte byteData[] = messageDigest.digest();

        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < byteData.length; i++) {
            stringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuffer.toString();
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
