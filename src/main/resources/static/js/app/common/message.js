/*
 * Meddage Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.message',['comn.service.resource', 'pascalprecht.translate'])
.config(["$translateProvider", function ($translateProvider) {
    $translateProvider.useUrlLoader('/message');
    $translateProvider.useStorage('UrlMessageStorage');
    $translateProvider.preferredLanguage('ko');
    $translateProvider.fallbackLanguage('ko');
}])
.factory('MessageService', ['RESTfulService', '$q', function (RESTfulService, $q) {
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

}])
.factory('UrlMessageStorage', ['$location', function($location) {
    return {
        put: function (name, value) {},
        get: function (name) {
            return $location.search()['lang']
        }
    };
}])
.directive('nxtMessage', ['MessageService', function (MessageService) {
    return{
        restrict: 'EA',
        link: function($scope, element, attrs, ctrl){
            MessageService.getMessage("code",
                    function(messageData){
                        (element[0].tagName == "INPUT" || element[0].tagName == "TEXTAREA") 
                            ? angular.element(element).val("메세지") :angular.element(element).text(messageData);
                    },
                    function(error){
                        (element[0].tagName == "INPUT" || element[0].tagName == "TEXTAREA")
                            ? angular.element(element).val("메세지") : angular.element(element).text("메세지");
                    }
            );
        }
    }

}]);