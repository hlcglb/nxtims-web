/*
 * Rsource Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.resource',['ngResource'])
.config(['$resourceProvider', function($resourceProvider){
    $resourceProvider.defaults.stripTrailingSlashes = false;
}])
.factory('RESTfulService1', ['$resource', 'RESTfulInterceptor', 
                             function ($resource, RESTfulInterceptor) {
    return $resource('/:area/:service/:id', 
            {
                area: '@area',
                service: '@service',
                id: '@id'
            }, 
            {
                get: {method: 'GET', interceptor: RESTfulInterceptor.log},
                query: {method: 'GET', isArray: true},
                post: {method: 'POST'},
                update: {method: 'PUT'}
            });
}])
.factory('RESTfulService', ['$resource', 'RESTfulInterceptor', 
                             function ($resource, RESTfulInterceptor) {
    return $resource('/:area/:service', 
            {
                area: '@area',
                service: '@service'
            }, 
            {
                get: {method: 'GET', interceptor: RESTfulInterceptor.log},
                getList: {method: 'GET', isArray: true},
                post: {method: 'POST'},
                update: {method: 'PUT'}
            });
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