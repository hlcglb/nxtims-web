package com.hyundaiuni.nxtims.support.mail;

import java.io.File;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

public class FileAttachment {
    private File file;
    private String filename;
    private InputStreamSource inputStreamSource;

    /**
     * 첨부 파일을 설정하는 생성자.
     * 
     * @param file 첨부 파일
     */
    public FileAttachment(File file) {
        this.file = file;
    }

    /**
     * 첨부 파일, 파일명을 설정하는 생성자.
     * 
     * @param file 첨부 파일
     * @param filename 첨부 파일명
     */
    public FileAttachment(File file, String filename) {
        this.file = file;
        this.filename = filename;
    }

    /**
     * 첨부 파일명, 데이터를 설정하는 생성자.
     * 
     * @param filename 첨부 파일명
     * @param contents 첨부 데이터
     */
    public FileAttachment(String filename, byte[] contents) {
        this.filename = filename;
        this.inputStreamSource = new ByteArrayResource(contents);
    }

    /**
     * 첨부 파일을 리턴함.
     */
    public File getFile() {
        return file;
    }

    /**
     * 첨부 파일명을 리턴함.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 첨부 데이터가 들어있는 {@link InputStreamSource}를 리턴함.
     */
    public InputStreamSource getInputStreamSource() {
        return inputStreamSource;
    }
}
