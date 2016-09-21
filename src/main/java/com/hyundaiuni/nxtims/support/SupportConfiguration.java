package com.hyundaiuni.nxtims.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hyundaiuni.nxtims.domain.app.CodeDetail;
import com.hyundaiuni.nxtims.exception.SupportConfigurationException;
import com.hyundaiuni.nxtims.service.app.CodeService;

@Configuration
public class SupportConfiguration {
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
}
