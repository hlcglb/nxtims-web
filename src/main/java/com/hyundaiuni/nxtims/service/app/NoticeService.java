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
import org.springframework.web.multipart.MultipartFile;

import com.hyundaiuni.nxtims.domain.app.Notice;
import com.hyundaiuni.nxtims.domain.app.NoticeFile;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;

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

        return apiTemplate.getRestTemplate().postForObject(resourceUrl, notice, Notice.class);
    }

    public Notice updateNotice(Notice notice) {
        Assert.notNull(notice, "notice must not be null");

        String resourceUrl = apiServerUrl + apiUrl + "/{noticeId}";

        List<NoticeFile> noticeFileList = notice.getNoticeFileList();

        if(CollectionUtils.isNotEmpty(noticeFileList)) {
            for(NoticeFile noticeFile : noticeFileList) {
                if("C".equals(noticeFile.getTransactionType())) {
                    MultipartFile attachedFile = noticeFile.getFile();
                    noticeFile.setFileNm(attachedFile.getOriginalFilename());

                }
            }
        }

        apiTemplate.getRestTemplate().put(resourceUrl, notice, notice.getNoticeId());

        return getNotice(notice.getNoticeId());
    }

    public void deleteNotice(String noticeId) {
        String resourceUrl = apiServerUrl + apiUrl + "/{noticeId}";

        apiTemplate.getRestTemplate().delete(resourceUrl, noticeId);
    }
}
