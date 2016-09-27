package com.hyundaiuni.nxtims.support.message;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.hyundaiuni.nxtims.domain.app.CodeDetail;

@Component
public class CodeMessageSourceHandler implements MessageSourceAware {
    private MessageSource messageSource;

    private List<CodeDetail> codeDetailList = null;

    public CodeMessageSourceHandler(List<CodeDetail> codeDetailList) {
        this.codeDetailList = codeDetailList;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public List<CodeDetail> getCodeDetailAll() {
        if(CollectionUtils.isNotEmpty(codeDetailList)) {
            for(CodeDetail codeDetail : codeDetailList) {
                if(StringUtils.isNotEmpty(codeDetail.getMsgGrpCd()) && StringUtils.isNotEmpty(codeDetail.getMsgCd())) {
                    String codeDtlNm = messageSource.getMessage(codeDetail.getMsgGrpCd() + "." + codeDetail.getMsgCd(),
                        null, codeDetail.getCodeDtlNm(), LocaleContextHolder.getLocale());

                    codeDetail.setCodeDtlNm(codeDtlNm);
                }
            }
        }

        return codeDetailList;
    }
}
