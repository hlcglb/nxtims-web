package com.hyundaiuni.nxtims.support;

import java.util.List;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.hyundaiuni.nxtims.domain.app.CodeDetail;
import com.hyundaiuni.nxtims.exception.SupportConfigurationException;
import com.hyundaiuni.nxtims.service.app.CodeService;
import com.hyundaiuni.nxtims.support.mail.VelocityEmailSender;

@Configuration
public class SupportConfiguration {
    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.default-encoding}")
    private String mailDefaultEncoding;

    @Value("${mail.system-name}")
    private String mailSystemName;

    @Value("${mail.homepage-url}")
    private String mailHomepageUrl;

    @Value("${mail.from-email}")
    private String mailFromEmail;

    @Value("${mail.from-name}")
    private String mailFromName;

    @Value("${mail.subject-prefix}")
    private String mailSubjectPrefix;

    @Autowired
    private CodeService codeService;

    @Bean
    public List<CodeDetail> getCodeDetailList() throws SupportConfigurationException {
        CodeFactoryBean codeFactoryBean = new CodeFactoryBean();

        codeFactoryBean.setCodeService(codeService);

        List<CodeDetail> codeDetailList = null;

        try {
            codeDetailList = codeFactoryBean.getObject();
        }
        catch(Exception e) {
            throw new SupportConfigurationException(e.getMessage(), e);
        }

        return codeDetailList;
    }

    @Bean
    public CodeMessageSourceHandler codeMessageSourceHandler() throws SupportConfigurationException {
        CodeMessageSourceHandler codeMessageSourceHandler = new CodeMessageSourceHandler(getCodeDetailList());

        return codeMessageSourceHandler;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailHost);
        mailSender.setDefaultEncoding(mailDefaultEncoding);

        return mailSender;
    }
    
    @Bean
    public VelocityEngine velocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();

        velocityEngine.setProperty("file.resource.loader.path", "/src/main/resources/templates/");

        return velocityEngine;
    }    

    @Bean
    public VelocityEmailSender velocityEmailSender() {
        VelocityEmailSender velocityEmailSender = new VelocityEmailSender();

        velocityEmailSender.setMailSender(mailSender());
        velocityEmailSender.setVelocityEngine(velocityEngine());
        velocityEmailSender.setSystemName(mailSystemName);
        velocityEmailSender.setHomepageUrl(mailHomepageUrl);
        velocityEmailSender.setFromEmail(mailFromEmail);
        velocityEmailSender.setFromName(mailFromName);
        velocityEmailSender.setSubjectPrefix(mailSubjectPrefix);

        return velocityEmailSender;
    }
}
