package com.hyundaiuni.nxtims.controller.app;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hyundaiuni.nxtims.domain.app.Notice;
import com.hyundaiuni.nxtims.domain.app.NoticeFile;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoticeControllerTest {
    private static final Log log = LogFactory.getLog(NoticeControllerTest.class);

    private static final String URL = "/api/app/notice";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        Exception ex = null;

        try {
            this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }
    
    @Test
    public void testGetNoticeFileContent() {
        Exception ex = null;

        try {

            mvc.perform(get(URL + "/getNoticeFileContent?noticeId=0000000000&seq=1")).andDo(
                print()).andExpect(status().isOk()).andExpect(
                    content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        }
        catch(Exception e) {
            log.error(e.getMessage());
            ex = e;
        }

        assertEquals(null, ex);
    }    

    @Test
    public void testInsertNotice() throws Exception {
        MockMultipartFile file0 = new MockMultipartFile("noticeFileList[0].file", "filename.txt", "text/plain",
            "some xml".getBytes());
        MockMultipartFile file1 = new MockMultipartFile("noticeFileList[1].file", "filename2.txt", "text/plain",
            "some2 xml".getBytes());

        MvcResult result = mvc.perform(
            fileUpload(URL).file(file0).file(file1).param("title", "INSERT NOTICE TEST").param("content",
                "TEST CONTENT").param("openYmd", "20160923").param("closeYmd", "20160924").param("sessionUserId",
                    "200065")).andExpect(status().isOk()).andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();

        log.info(result.getResponse().getContentAsString());

        String query = result.getResponse().getContentAsString();

        Notice retrieveNotice = JsonTestUtils.jsonStringToObject(query, Notice.class);

        List<NoticeFile> tempNoticeFileList = retrieveNotice.getNoticeFileList();

        MockMultipartFile file2 = new MockMultipartFile("noticeFileList[" + tempNoticeFileList.size() + "].file",
            "filename3.txt", "text/plain", "some3 xml".getBytes());

        MockHttpServletRequestBuilder builder = fileUpload(URL + "/" + retrieveNotice.getNoticeId()).file(file2).param(
            "noticeId", retrieveNotice.getNoticeId()).param("title", "INSERT NOTICE TESTS").param("content",
                "TEST CONTENT").param("openYmd", "20160923").param("closeYmd", "20160924").param("sessionUserId",
                    "200065").param("noticeFileList[" + tempNoticeFileList.size() + "].transactionType", "C");

        int i = 0;

        if(CollectionUtils.isNotEmpty(tempNoticeFileList)) {
            for(NoticeFile tempNoticeFile : tempNoticeFileList) {
                builder.param("noticeFileList[" + i + "].noticeId", tempNoticeFile.getNoticeId()).param(
                    "noticeFileList[" + i + "].seq", Integer.toString(tempNoticeFile.getSeq())).param(
                        "noticeFileList[" + i + "].transactionType", "D");
                i++;
            }
        }

        mvc.perform(builder).andExpect(status().isOk());

        mvc.perform(delete(URL + "/{noticeId}", retrieveNotice.getNoticeId())).andDo(print()).andExpect(
            status().isOk());
    }
}
