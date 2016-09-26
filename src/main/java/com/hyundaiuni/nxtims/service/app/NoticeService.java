package com.hyundaiuni.nxtims.service.app;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.hyundaiuni.nxtims.domain.app.Notice;
import com.hyundaiuni.nxtims.domain.app.NoticeFile;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.support.rest.MultiValueMapConverter;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;
import com.hyundaiuni.nxtims.util.MessageDigestUtils;

@Service
public class NoticeService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/notice";

    @Autowired
    private RestApiTemplate apiTemplate;

    public List<Notice> getNoticeListByParam(String query, int offset, int limit) {
        Assert.notNull(query, "parameter must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("inquiry", "getNoticeListByParam");
        urlVariables.put("q", query);
        urlVariables.put("offset", offset);
        urlVariables.put("limit", limit);

        String resourceUrl = apiServerUrl + apiUrl + "?inquiry={inquiry}&q={q}&offset={offset}&limit={limit}";

        List<Notice> noticeList = new ArrayList<>();

        CollectionUtils.addAll(noticeList,
            apiTemplate.getRestTemplate().getForObject(resourceUrl, Notice[].class, urlVariables));

        return noticeList;
    }

    public Notice getNotice(String noticeId) {
        Assert.notNull(noticeId, "noticeId must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{noticeId}";

        return apiTemplate.getRestTemplate().getForObject(resourceUrl, Notice.class, noticeId);
    }

    public Map<String, Object> getNoticeFileContentByPk(String noticeId, int seq) {
        Assert.notNull(noticeId, "noticeId must not be null");
        Assert.notNull(seq, "seq must not be null");

        Map<String, Object> urlVariables = new HashMap<>();

        urlVariables.put("noticeId", noticeId);
        urlVariables.put("seq", seq);

        String resourceUrl = apiServerUrl + apiUrl + "/getNoticeFileContent?noticeId={noticeId}&seq={seq}";

        @SuppressWarnings("unchecked")
        Map<String, Object> fileContentMap = (Map<String, Object>)apiTemplate.getRestTemplate().getForObject(
            resourceUrl, Map.class, urlVariables);

        String decodedFileContent;

        decodedFileContent = MessageDigestUtils.decodeBase64(
            MapUtils.getString(fileContentMap, "FILE_CONTENT").getBytes());

        fileContentMap.put("FILE_CONTENT", decodedFileContent.getBytes());

        return fileContentMap;
    }

    @SuppressWarnings("unchecked")
    public Notice insertNotice(Notice notice) {
        Assert.notNull(notice, "notice must not be null");

        List<NoticeFile> noticeFileList = notice.getNoticeFileList();

        if(CollectionUtils.isNotEmpty(noticeFileList)) {
            for(NoticeFile noticeFile : noticeFileList) {
                MultipartFile attachedFile = noticeFile.getFile();
                noticeFile.setFileNm(attachedFile.getOriginalFilename());
            }
        }

        String resourceUrl = apiServerUrl + apiUrl;

        MultiValueMap<String, Object> multiValueMap = null;

        try {
            multiValueMap = new MultiValueMapConverter(notice).convert();
        }
        catch(InvocationTargetException | IllegalAccessException | NoSuchMethodException | IntrospectionException e) {
            throw new ServiceException("MSG.MSG_CONVERT_ERROR", e.getMessage(), null, e);
        }

        return apiTemplate.getRestTemplate().postForObject(resourceUrl, multiValueMap, Notice.class);
    }

    @SuppressWarnings("unchecked")
    public Notice updateNotice(Notice notice) {
        Assert.notNull(notice, "notice must not be null");

        List<NoticeFile> noticeFileList = notice.getNoticeFileList();

        if(CollectionUtils.isNotEmpty(noticeFileList)) {
            for(NoticeFile noticeFile : noticeFileList) {
                if("C".equals(noticeFile.getTransactionType()) || "U".equals(noticeFile.getTransactionType())) {
                    noticeFile.setNoticeId(notice.getNoticeId());

                    MultipartFile attachedFile = noticeFile.getFile();
                    noticeFile.setFileNm(attachedFile.getOriginalFilename());
                }
            }
        }

        MultiValueMap<String, Object> multiValueMap = null;

        try {
            multiValueMap = new MultiValueMapConverter(notice).convert();
        }
        catch(InvocationTargetException | IllegalAccessException | NoSuchMethodException | IntrospectionException e) {
            throw new ServiceException("MSG.MSG_CONVERT_ERROR", e.getMessage(), null, e);
        }

        String resourceUrl = apiServerUrl + apiUrl + "/{noticeId}";

        apiTemplate.getRestTemplate().postForObject(resourceUrl, multiValueMap, Notice.class, notice.getNoticeId());

        return getNotice(notice.getNoticeId());
    }

    public void deleteNotice(String noticeId) {
        String resourceUrl = apiServerUrl + apiUrl + "/{noticeId}";

        apiTemplate.getRestTemplate().delete(resourceUrl, noticeId);
    }
}
