package com.hyundaiuni.nxtims.controller.app;

import java.util.List;

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

import com.hyundaiuni.nxtims.domain.app.Resource;
import com.hyundaiuni.nxtims.service.app.ResourceService;

@RestController
@RequestMapping("/api/app/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(params = "inquiry=getResourceListByParam", method = RequestMethod.GET)
    public ResponseEntity<?> getResourceListByParam(@RequestParam("q") String query, @RequestParam("offset") int offset,
        @RequestParam("limit") int limit) {
        Assert.notNull(query, "query must not be null");
        Assert.notNull(offset, "offset must not be null");
        Assert.notNull(limit, "limit must not be null");

        return new ResponseEntity<>(resourceService.getResourceListByParam(query, offset, limit), HttpStatus.OK);
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET)
    public ResponseEntity<?> getResource(@PathVariable("resourceId") String resourceId) {
        Assert.notNull(resourceId, "resourceId must not be null");

        return new ResponseEntity<>(resourceService.getResource(resourceId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insertResource(@RequestBody Resource resource) {
        return new ResponseEntity<>(resourceService.insertResource(resource), HttpStatus.OK);
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateResource(@PathVariable("resourceId") String resourceId,
        @RequestBody Resource resource) {
        return new ResponseEntity<>(resourceService.updateResource(resourceId, resource), HttpStatus.OK);
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteResource(@PathVariable("resourceId") String resourceId) {
        resourceService.deleteResource(resourceId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveResources(@RequestBody List<Resource> resourceList) {
        resourceService.saveResources(resourceList);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
