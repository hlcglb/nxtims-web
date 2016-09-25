package com.hyundaiuni.nxtims.service.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.hyundaiuni.nxtims.domain.app.Notice;
import com.hyundaiuni.nxtims.domain.app.NoticeFile;
import com.hyundaiuni.nxtims.exception.FtpClientException;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.support.rest.RestApiTemplate;
import com.hyundaiuni.nxtims.support.ftp.FtpClientTemplate;

@Service
public class NoticeService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/app/notice";

    @Autowired
    private RestApiTemplate apiTemplate;

    @Autowired
    private FtpClientTemplate ftpClientTemplate;

    private String remoteDir = "/app/javadaemon/global/nxtims/notice/attachedFile/";

    @Value("${system.temp-local-dir}")
    private String tempLocalDir;

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

                File tempFile = new File(tempLocalDir + noticeFile.getFileNm());

                noticeFile.setFileUrl(remoteDir + tempFile.getName());

                try {
                    attachedFile.transferTo(tempFile);
                    ftpClientTemplate.sendFile(remoteDir, tempFile);

                    FileUtils.deleteQuietly(tempFile);
                }
                catch(IOException | FtpClientException e) {
                    throw new ServiceException("MSG.FTP_SEND_ERROR", e.getMessage(),
                        new String[] {noticeFile.getFileNm()});
                }
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

                    File tempFile = new File(tempLocalDir + noticeFile.getFileNm());

                    noticeFile.setFileUrl(remoteDir + tempFile.getName());

                    try {
                        attachedFile.transferTo(tempFile);
                        ftpClientTemplate.sendFile(remoteDir, tempFile);

                        FileUtils.deleteQuietly(tempFile);
                    }
                    catch(IOException | FtpClientException e) {
                        throw new ServiceException("MSG.FTP_SEND_ERROR", e.getMessage(),
                            new String[] {noticeFile.getFileNm()});
                    }
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
