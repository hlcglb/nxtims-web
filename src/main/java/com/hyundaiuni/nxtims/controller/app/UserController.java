package com.hyundaiuni.nxtims.controller.app;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
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

import com.hyundaiuni.nxtims.domain.app.User;
import com.hyundaiuni.nxtims.exception.ServiceException;
import com.hyundaiuni.nxtims.service.app.UserService;

@RestController
@RequestMapping("/api/app/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(params = "inquiry=getUserListByParam", method = RequestMethod.GET)
    public ResponseEntity<?> getUserListByParam(@RequestParam("q") String query, @RequestParam("offset") int offset,
        @RequestParam("limit") int limit) {
        Assert.notNull(query, "query must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        return new ResponseEntity<>(userService.getUserListByParam(query, offset, limit), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
        Assert.notNull(userId, "userId must not be null");

        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insertUser(@RequestBody User user) {
        Assert.notNull(user, "user must not be null");

        return new ResponseEntity<>(userService.insertUser(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("userId") String userId, @RequestBody User user) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(user, "user must not be null");

        if(!userId.equals(user.getUserId())) {
            throw new ServiceException("MSG.INVALID_PATH_VARIABLE", "There is invalid path variable.", null);
        }

        userService.updateUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId) {
        Assert.notNull(userId, "userId must not be null");

        userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/reissuePassword", method = RequestMethod.POST)
    public ResponseEntity<?> reissuePassword(@RequestBody Map<String, Object> request) {
        Assert.notNull(request, "request must not be null");

        String userId = MapUtils.getString(request, "USER_ID");
        String userNm = MapUtils.getString(request, "USER_NM");
        String email = MapUtils.getString(request, "EMAIL");

        userService.reissuePassword(userId, userNm, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
