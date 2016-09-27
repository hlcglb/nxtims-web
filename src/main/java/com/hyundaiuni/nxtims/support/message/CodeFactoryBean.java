package com.hyundaiuni.nxtims.support.message;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;

import com.hyundaiuni.nxtims.domain.app.CodeDetail;
import com.hyundaiuni.nxtims.service.app.CodeService;

public class CodeFactoryBean implements FactoryBean<List<CodeDetail>> {
    private CodeService codeService;

    private List<CodeDetail> codeDetailList;

    public void setCodeService(CodeService codeService) {
        this.codeService = codeService;
    }

    @PostConstruct
    public void init() throws Exception {
        codeDetailList = codeService.getCodeDetailAll();
    }

    @Override
    public List<CodeDetail> getObject() throws Exception {
        if(codeDetailList == null) {
            codeDetailList = codeService.getCodeDetailAll();
        }
        return codeDetailList;
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
