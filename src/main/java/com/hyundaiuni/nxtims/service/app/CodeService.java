package com.hyundaiuni.nxtims.service.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hyundaiuni.nxtims.domain.app.CodeDetail;
import com.hyundaiuni.nxtims.domain.app.CodeMaster;
import com.hyundaiuni.nxtims.service.RestApiTemplate;

@Service
public class CodeService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/code";

    @Autowired
    private RestApiTemplate apiTemplate;

    public List<CodeDetail> getCodeDetailAll() {
        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getCodeDetailAll");

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}";

        List<CodeDetail> codeDetailList = new ArrayList<>();

        CollectionUtils.addAll(codeDetailList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, CodeDetail[].class, urlVariables));

        return codeDetailList;
    }

    public List<CodeMaster> getCodeMasterListByParam(String query, int offset, int limit) {
        Assert.notNull(query, "parameter must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getCodeMasterListByParam");
        urlVariables.put("q", query);
        urlVariables.put("offset", offset);
        urlVariables.put("limit", limit);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&q={q}&offset={offset}&limit={limit}";

        List<CodeMaster> codeMasterList = new ArrayList<>();

        CollectionUtils.addAll(codeMasterList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, CodeMaster[].class, urlVariables));

        return codeMasterList;
    }

    public List<CodeDetail> getCodeDetailListByCodeMstCd(String codeMstCd) {
        Assert.notNull(codeMstCd, "codeMstCd must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getCodeDetailListByCodeMstCd");
        urlVariables.put("codeMstCd", codeMstCd);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&codeMstCd={codeMstCd}";

        List<CodeDetail> codeDetailList = new ArrayList<>();

        CollectionUtils.addAll(codeDetailList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, CodeDetail[].class, urlVariables));

        return codeDetailList;
    }

    public CodeMaster getCode(String codeMstCd) {
        Assert.notNull(codeMstCd, "codeMstCd must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{codeMstCd}";

        return apiTemplate.getRestTemplate().getForObject(resourceUrl, CodeMaster.class, codeMstCd);
    }

    public CodeMaster insertCode(CodeMaster codeMaster) {
        Assert.notNull(codeMaster, "codeMaster must not be null");

        String resourceUrl = apiServerUrl + apiUrl;

        return apiTemplate.getRestTemplate().postForObject(resourceUrl, codeMaster, CodeMaster.class);
    }

    public CodeMaster updateCode(CodeMaster codeMaster) {
        Assert.notNull(codeMaster, "codeMaster must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{codeMstCd}";

        apiTemplate.getRestTemplate().put(resourceUrl, codeMaster, codeMaster.getCodeMstCd());

        return getCode(codeMaster.getCodeMstCd());
    }

    public void deleteCode(String codeMstCd) {
        Assert.notNull(codeMstCd, "codeMstCd must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{codeMstCd}";

        apiTemplate.getRestTemplate().delete(resourceUrl, codeMstCd);
    }
}
