/*
 * Rsource Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.resource',['ngResource'])
.config(['$resourceProvider', function($resourceProvider){
    $resourceProvider.defaults.stripTrailingSlashes = false;
    $resourceProvider.defaults.actions.update = {method: 'PUT'};
}])
.factory('RESTfulFactory', ['$resource', 'RESTfulInterceptor', 
                             function ($resource, RESTfulInterceptor) {
    return function(url, paramDefaults, actions){
        if(actions){
            actions.get = {method: 'GET', interceptor: RESTfulInterceptor.log};
        }
        return $resource(url, paramDefaults, actions);
    }
}])
.factory('RESTfulService', ['RESTfulFactory', 
                             function (RESTfulFactory) {
    //make pattern '/api/:service0/:service1/.../:id'
    function makePathPattern(urlArr){
        var options = {}
        options.url = '/api/';
        options.paramDefaults = {};
        for(var i=0; i<urlArr.length; i++){
            var serviceName = urlArr[i];
            options.paramDefaults[serviceName] = '@'+ urlArr[i];
        }
        options.url += ':id';
        options.paramDefaults.id = '@id';
        return options;
    }
    return function(val){
        var defaultOptions = {};
        if(angular.isObject(val)){
            if(!angular.isArray(val)){
                defaultOptions = makePathPattern(val);
            }
            else{
                angular.extend(defaultOptions, val);
            }
        }
        else{
            //path check
            var url = val;
            if(!url) return false;
            if(url.indexOf('/') == 0) url = url.substr(1,url.length-1); //stat '/' remove
            if(url.indexOf('api') == 0) url = url.replace('api', '');   //stat 'api' remove
            if(url.lastIndexOf('/') == url.length - 1) url = url.substr(0,url.length-2); //end '/' remove  
            if(url.lastIndexOf('/:') > -1){
                var urlArr = url.split('/:');
                if(urlArr[0].length == 0 || urlArr[0].indexOf('/') > -1) urlArr.shift();
                defaultOptions = makePathPattern(urlArr);
            }
            defaultOptions.url = '/api' + url;

        }
        return RESTfulFactory(defaultOptions.url, defaultOptions.paramDefaults, defaultOptions.actions);
    }
}])
.factory('LoginRESTService', ['RESTfulFactory', 
                             function (RESTfulFactory) {
    return RESTfulFactory('/api/login/:service/:id',
        {
            service: '@service',
            id: '@id'
        }
    );
}])
.factory('AppRESTService', ['RESTfulFactory', 
                             function (RESTfulFactory) {
    return RESTfulFactory('/api/app/:service/:id',
        {
            service: '@service',
            id: '@id'
        }
    );
}])

.factory('RESTfulService1', ['RESTfulFactory', 
                             function (RESTfulFactory) {
    
    var defaultOptions = {
                   url: '/api/:app0/:app1/:app2/:app3:id',
                   paramDefaults: {
                       app0: '@app0',
                       app1: '@app1',
                       app2: '@app2',
                       app3: '@app3',
                       id: '@id'
                   }
    }
    return RESTfulFactory(defaultOptions.url, defaultOptions.paramDefaults);
    
}])
.factory('RESTfulServiceTest', ['$resource', 'RESTfulInterceptor', 
                             function ($resource, RESTfulInterceptor) {
    return function(url){
        return $resource(url,
                {},
                {
                    get: {method: 'GET', interceptor: RESTfulInterceptor.log}
                });
    }
}])
.factory('RESTfulInterceptor', [ '$rootScope', '$q',
                                         function ($rootScope, $q) {
    return {
        log: {
            request : function(config){
                console.log(config);
                return config || $q.when(config);
            },
            response : function(response){
                //console.log(response.data);
                return response.resource || $q.when(response.resource);
            },
            responseError: function(rejection) {
                console.log("[#ResourceInterceptor] Found responseError: ", rejection);
                return $q.reject(rejection);
            }
        },
        returnHeaders: {
            response : function(response){
                return response || $q.when(response);
            }
        }
    }
}])
.factory('ResourceService', ['LoginRESTService', function (LoginRESTService) {
    return{
        get: function(){
            return LoginRESTService.get({service: 'resource'});
        },
        getPromise: function(){
            return LoginRESTService.get({service: 'resource'}).$promise;
        }
    }

}])
.factory('MessageService', ['AppRESTService', '$q', function (AppRESTService, $q) {
    return{
        getMessage: function(msgCode, successHandler, faileHandler){
            return RESTfulService.get({service: "message", id:msgCode}, 
                    function(messageData){
                        if(angular.isFunction(successHandler)) successHandler(messageData);
                    },
                    function(error){
                        if(angular.isFunction(faileHandler)) faileHandler(error.data)
                    }
            );
        },
        getPromise: function(msgCode){
            return RESTfulService.get({service: "message", id:msgCode}).$promise;
        }
    }

}]);