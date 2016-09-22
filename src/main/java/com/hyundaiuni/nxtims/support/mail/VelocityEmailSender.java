package com.hyundaiuni.nxtims.support.mail;

import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class VelocityEmailSender {
    private static final Log logger = LogFactory.getLog(VelocityEmailSender.class);

    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;
    private String systemName;
    private String homepageUrl;
    private String fromEmail;
    private String fromName;
    private String subjectPrefix;

    /**
     * 메일을 전송할 {@link JavaMailSender}을 설정함.
     * 
     * @param mailSender 사용할 {@link JavaMailSender}
     */
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    /**
     * 메일 내용을 생성할 {@link VelocityEngine}을 설정함.
     * 
     * @param velocityEngine 사용할 {@link VelocityEngine}
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }    

    /**
     * 메일 내용에 들어갈 시스템명을 설정함.
     * 
     * @param systemName 시스템명
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * 메일 내용에 들어갈 홈페이지 주소를 설정함.
     * 
     * @param homepageUrl 홈페이지 주소
     */
    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = StringUtils.removeEnd(homepageUrl, "/");
    }

    /**
     * 전송자 이메일 주소를 설정함.
     * 
     * @param fromEmail 전송자 이메일 주소
     */
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    /**
     * 전송자명을 설정함.
     * 
     * @param fromName 전송자명
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * 메일 제목에 들어갈 머릿말을 설정함.
     * 
     * @param subjectPrefix 메일 제목에 들어갈 머릿말
     */
    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    /**
     * 주어진 템플릿 파일, 변수를 이용하여 이메일을 전송함.
     * 
     * @param to 수신자 이메일 주소
     * @param name 수신자 이름
     * @param subject 제목
     * @param templateLocation 템플릿 파일 경로
     * @param encoding 메일 인코딩
     * @param model 템플릿에 사용할 변수가 저장된 {@link Map}
     * @throws MailSendException 메일 전송시에 에러가 발생할 때
     */
    @SuppressWarnings("rawtypes")
    public void send(String to, String name, String subject, String templateLocation, String encoding, Map model)
        throws MailSendException {
        send(new Address[] {new Address(to, name)}, subject, templateLocation, encoding, model, false);
    }

    /**
     * 주어진 템플릿 파일, 변수를 이용하여 이메일을 전송함.
     * 
     * @param tos 수신자들 이메일 주소, 이름
     * @param subject 제목
     * @param templateLocation 템플릿 파일 경로
     * @param encoding 메일 인코딩
     * @param model 템플릿에 사용할 변수가 저장된 {@link Map}
     * @param each 수신자 별로 각각 전송할 지 여부
     * @throws MailSendException 메일 전송시에 에러가 발생할 때
     */
    @SuppressWarnings("rawtypes")
    public void send(Address[] tos, String subject, String templateLocation, String encoding, Map model, boolean each)
        throws MailSendException {
        send(null, tos, subject, templateLocation, encoding, model, each);
    }

    /**
     * 주어진 템플릿 파일, 변수를 이용하여 이메일을 전송함.
     * 
     * @param from 발송자 이메일 주소, 이름
     * @param tos 수신자들 이메일 주소, 이름
     * @param subject 제목
     * @param templateLocation 템플릿 파일 경로
     * @param encoding 메일 인코딩
     * @param model 템플릿에 사용할 변수가 저장된 {@link Map}
     * @param each 수신자 별로 각각 전송할 지 여부
     * @throws MailSendException 메일 전송시에 에러가 발생할 때
     */
    @SuppressWarnings("rawtypes")
    public void send(Address from, Address[] tos, String subject, String templateLocation, String encoding, Map model,
        boolean each) throws MailSendException {
        send(from, tos, subject, templateLocation, encoding, model, each, (FileAttachment[])null);
    }

    /**
     * 주어진 템플릿 파일, 변수를 이용하여 이메일을 전송함.
     * 
     * @param to 수신자 이메일 주소
     * @param name 수신자 이름
     * @param subject 제목
     * @param templateLocation 템플릿 파일 경로
     * @param encoding 메일 인코딩
     * @param model 템플릿에 사용할 변수가 저장된 {@link Map}
     * @param attachments 첨부파일들
     * @throws MailSendException 메일 전송시에 에러가 발생할 때
     */
    @SuppressWarnings("rawtypes")
    public void send(String to, String name, String subject, String templateLocation, String encoding, Map model,
        FileAttachment... attachments) throws MailSendException {
        send(new Address(to, name), subject, templateLocation, encoding, model, attachments);
    }

    /**
     * 주어진 템플릿 파일, 변수를 이용하여 이메일을 전송함.
     * 
     * @param to 수신자 이메일 주소, 이름
     * @param subject 제목
     * @param templateLocation 템플릿 파일 경로
     * @param encoding 메일 인코딩
     * @param model 템플릿에 사용할 변수가 저장된 {@link Map}
     * @param attachments 첨부파일들
     * @throws MailSendException 메일 전송시에 에러가 발생할 때
     */
    @SuppressWarnings("rawtypes")
    public void send(Address to, String subject, String templateLocation, String encoding, Map model,
        FileAttachment... attachments) throws MailSendException {
        send(new Address[] {to}, subject, templateLocation, encoding, model, false, attachments);
    }

    /**
     * 주어진 템플릿 파일, 변수를 이용하여 이메일을 전송함.
     * 
     * @param tos 수신자들 이메일 주소, 이름
     * @param subject 제목
     * @param templateLocation 템플릿 파일 경로
     * @param encoding 메일 인코딩
     * @param model 템플릿에 사용할 변수가 저장된 {@link Map}
     * @param each 수신자 별로 각각 전송할 지 여부
     * @param attachments 첨부파일들
     * @throws MailSendException 메일 전송시에 에러가 발생할 때
     */
    @SuppressWarnings("rawtypes")
    public void send(Address[] tos, String subject, String templateLocation, String encoding, Map model, boolean each,
        FileAttachment... attachments) throws MailSendException {
        send(null, tos, subject, templateLocation, encoding, model, each, attachments);
    }

    /**
     * 주어진 템플릿 파일, 변수를 이용하여 이메일을 전송함.
     * 
     * @param from 발송자 이메일 주소, 이름
     * @param tos 수신자들 이메일 주소, 이름
     * @param subject 제목
     * @param templateLocation 템플릿 파일 경로
     * @param encoding 메일 인코딩
     * @param model 템플릿에 사용할 변수가 저장된 {@link Map}
     * @param each 수신자 별로 각각 전송할 지 여부
     * @param attachments 첨부파일들
     * @throws MailSendException 메일 전송시에 에러가 발생할 때
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void send(Address from, Address[] tos, String subject, String templateLocation, String encoding, Map model,
        boolean each, FileAttachment... attachments) throws MailSendException {
        if(!model.containsKey(systemName)) {
            model.put("systemName", systemName);
        }
        if(!model.containsKey(homepageUrl)) {
            model.put("homepageUrl", homepageUrl);
        }

        try {
            Address newFrom = from == null ? new Address(fromEmail, fromName) : from;
            String newSubject = StringUtils.defaultIfEmpty(subjectPrefix, "") + subject;

            VelocityContext velocityContext = new VelocityContext(model);

            StringWriter stringWriter = new StringWriter();
            
            velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, stringWriter);

            if(each) {
                if(!ArrayUtils.isEmpty(tos)) {
                    for(Address to : tos) {
                        try {
                            if(to != null) {
                                send(mailSender, newFrom, new Address[] {to}, newSubject, stringWriter.toString(),
                                    encoding, attachments);
                            }
                        }
                        catch(Exception e) {
                            if(logger.isErrorEnabled()) {
                                logger.error("Mail[" + to + "] send fail.", e);
                            }
                        }
                    }
                }
            }
            else {
                send(mailSender, newFrom, tos, newSubject, stringWriter.toString(), encoding, attachments);
            }
        }
        catch(Exception e) {
            throw new MailSendException("Mail send fail.", e);
        }
    }

    private void send(JavaMailSender mailSender, Address from, Address[] tos, String subject, String text,
        String encoding, FileAttachment... attachments) throws Exception {
        int attachmentLength = ArrayUtils.getLength(attachments);
        boolean multipart = attachmentLength > 0;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, encoding);

        helper.setFrom(from.getAddress(), from.getName());
        if(!ArrayUtils.isEmpty(tos)) {
            for(Address to : tos) {
                helper.addTo(to.getAddress(), to.getName());
            }
        }

        helper.setSubject(subject);
        helper.setText(text, true);

        if(attachmentLength > 0) {
            for(FileAttachment attachment : attachments) {
                File file = attachment.getFile();
                if(file != null) {
                    String filename = getAttachmentFilename(file.getName(), encoding);
                    helper.addAttachment(filename, file);
                }
                else {
                    InputStreamSource inputStreamSource = attachment.getInputStreamSource();
                    if(inputStreamSource != null) {
                        String filename = getAttachmentFilename(attachment.getFilename(), encoding);
                        helper.addAttachment(filename, inputStreamSource);
                    }
                }
            }
        }
        mailSender.send(message);
    }

    private String getAttachmentFilename(String filename, String encoding) throws UnsupportedEncodingException {
        return MimeUtility.encodeText(filename, encoding, "B");
    }
}
