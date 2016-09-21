package com.hyundaiuni.nxtims.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyundaiuni.nxtims.domain.app.CodeMaster;
import com.hyundaiuni.nxtims.service.app.CodeService;

@RestController
@RequestMapping("/api/app/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @RequestMapping(params = "inquiry=getCodeMasterListByParam", method = RequestMethod.GET)
    public ResponseEntity<?> getCodeMasterListByParam(@RequestParam("q") String query,
        @RequestParam("offset") int offset, @RequestParam("limit") int limit) {
        Assert.notNull(query, "query must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        return new ResponseEntity<>(codeService.getCodeMasterListByParam(query, offset, limit), HttpStatus.OK);
    }

    @RequestMapping(params = "inquiry=getCodeDetailListByCodeMstCd", method = RequestMethod.GET)
    public ResponseEntity<?> getCodeDetailListByCodeMstCd(@RequestParam("codeMstCd") String codeMstCd) {
        Assert.notNull(codeMstCd, "codeMstCd must not be null");

        return new ResponseEntity<>(codeService.getCodeDetailListByCodeMstCd(codeMstCd), HttpStatus.OK);
    }

    @RequestMapping(value = "/{codeMstCd}", method = RequestMethod.GET)
    public ResponseEntity<?> getCode(@PathVariable("codeMstCd") String codeMstCd) {
        Assert.notNull(codeMstCd, "codeMstCd must not be null");

        return new ResponseEntity<>(codeService.getCode(codeMstCd), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insertCode(@RequestBody CodeMaster codeMaster) {
        return new ResponseEntity<>(codeService.insertCode(codeMaster), HttpStatus.OK);
    }

    @RequestMapping(value = "/{codeMstCd}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCode(@PathVariable("codeMstCd") String codeMstCd,
        @RequestBody CodeMaster codeMaster) {
        codeService.updateCode(codeMstCd, codeMaster);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{codeMstCd}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCode(@PathVariable("codeMstCd") String codeMstCd) {
        codeService.deleteCode(codeMstCd);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
