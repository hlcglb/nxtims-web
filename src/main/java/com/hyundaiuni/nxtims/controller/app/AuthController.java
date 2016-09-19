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

import com.hyundaiuni.nxtims.domain.app.Auth;
import com.hyundaiuni.nxtims.service.app.AuthService;

@RestController
@RequestMapping("/api/app/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping(params = "inquiry=getAuthListByParam", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthListByParam(@RequestParam("q") String query, @RequestParam("offset") int offset,
        @RequestParam("limit") int limit) {
        Assert.notNull(query, "query must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        return new ResponseEntity<>(authService.getAuthListByParam(query, offset, limit), HttpStatus.OK);
    }

    @RequestMapping(params = "inquiry=getAuthResourceListByAuthId", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthResourceListByAuthId(@RequestParam("authId") String authId) {
        Assert.notNull(authId, "authId must not be null");

        return new ResponseEntity<>(authService.getAuthResourceListByAuthId(authId), HttpStatus.OK);
    }

    @RequestMapping(value = "/{authId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthByAuthId(@PathVariable("authId") String authId) {
        Assert.notNull(authId, "authId must not be null");

        return new ResponseEntity<>(authService.getAuthByAuthId(authId), HttpStatus.OK);
    }

    @RequestMapping(params = "inquiry=getNotExistsAuthResourceListByAuthId", method = RequestMethod.GET)
    public ResponseEntity<?> getNotExistsAuthResourceListByAuthId(@RequestParam("authId") String authId) {
        Assert.notNull(authId, "authId must not be null");

        return new ResponseEntity<>(authService.getNotExistsAuthResourceListByAuthId(authId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insertAuth(@RequestBody Auth auth) {
        return new ResponseEntity<>(authService.insertAuth(auth), HttpStatus.OK);
    }

    @RequestMapping(value = "/{authId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAuth(@PathVariable("authId") String authId, @RequestBody Auth auth) {
        return new ResponseEntity<>(authService.updateAuth(authId, auth), HttpStatus.OK);
    }

    @RequestMapping(value = "/{authId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAuth(@PathVariable("authId") String authId) {
        authService.deleteAuth(authId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
