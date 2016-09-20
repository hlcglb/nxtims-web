package com.hyundaiuni.nxtims.support;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hyundaiuni.nxtims.domain.app.CodeDetail;
import com.hyundaiuni.nxtims.service.app.CodeService;

@Component
public class CodeFactoryBean implements FactoryBean<List<CodeDetail>> {
    @Autowired
    private CodeService codeService;

    private List<CodeDetail> codeDetailList;

    @PostConstruct
    public void init(){
        codeDetailList = codeService.getCodeDetailAll();
    }

    @Override
    public List<CodeDetail> getObject(){
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
