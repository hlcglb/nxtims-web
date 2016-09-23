package com.hyundaiuni.nxtims.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyundaiuni.nxtims.domain.app.Notice;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.service.app.NoticeService;

@RestController
@RequestMapping("/api/app/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @RequestMapping(params = "inquiry=getNoticeListByParam", method = RequestMethod.GET)
    public ResponseEntity<?> getNoticeListByParam(@RequestParam("q") String query, @RequestParam("offset") int offset,
        @RequestParam("limit") int limit) {
        Assert.notNull(query, "query must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        return new ResponseEntity<>(noticeService.getNoticeListByParam(query, offset, limit), HttpStatus.OK);
    }

    @RequestMapping(value = "/{noticeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getNotice(@PathVariable("noticeId") String noticeId) {
        Assert.notNull(noticeId, "noticeId must not be null");

        return new ResponseEntity<>(noticeService.getNotice(noticeId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insertNotice(@ModelAttribute Notice notice) {
        Assert.notNull(notice, "notice must not be null");

        return new ResponseEntity<>(noticeService.insertNotice(notice), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{noticeId}", method = RequestMethod.POST)
    public ResponseEntity<?> updateNotice(@PathVariable("noticeId") String noticeId, @ModelAttribute Notice notice) {
        Assert.notNull(noticeId, "noticeId must not be null");
        Assert.notNull(notice, "notice must not be null");

        if(!noticeId.equals(notice.getNoticeId())) {
            throw new ServiceException("MSG.INVALID_PATH_VARIABLE", "There is invalid path variable.", null);
        }

        noticeService.updateNotice(notice);

        return new ResponseEntity<>(HttpStatus.OK);
    }    
    
    @RequestMapping(value = "/{noticeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteNotice(@PathVariable("noticeId") String noticeId) {
        Assert.notNull(noticeId, "noticeId must not be null");

        noticeService.deleteNotice(noticeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }    
}
