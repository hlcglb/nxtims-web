/*
 * Rsource Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.resource',['ngResource'])
.factory('RESTfulService', ['$resource', '$cookies', 'RESTfulInterceptor', 
                             function ($resource, $cookies, RESTfulInterceptor) {
    return $resource('/:service/:method/:id', 
            {
                service: '@service',
                method: '@method',
                id: '@id'
            }, 
            {
                get: {method: 'GET', interceptor: RESTfulInterceptor.log},
                post: {method: 'POST'},
                update: {method: 'PUT'}
            });
}])
.factory('RESTfulInterceptor', [ '$rootScope', '$q', '$injector', '$window', '$cookies',
                                         function ($rootScope, $q, $injector, $window, $cookies) {
    return {
        log: {
            request : function(config){
                console.log(config);
                return config || $q.when(config);
            },
            response : function(response){
                console.log(response.data);
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
.factory('ResourceService', ['RESTfulService', function (RESTfulService) {
    return{
        get: function(){
            RESTfulService.get({service: "resource"});
        },
        getPromise: function(){
            return RESTfulService.get({service: "resource"}).$promise;
        }
    }

}]);