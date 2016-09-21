/*
 * Meddage Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.message',['comn.service.resource', 'pascalprecht.translate', 'ngSanitize'])
.config(["$translateProvider", function ($translateProvider) {
    $translateProvider.useUrlLoader('/api/login/message');
    $translateProvider.useStorage('UrlMessageStorage');
    $translateProvider.preferredLanguage('ko_KR');
    $translateProvider.fallbackLanguage('ko_KR');
    //security
    $translateProvider.useSanitizeValueStrategy('escape'); //(https://angular-translate.github.io/docs/#/guide/19_security)
}])
.factory('UrlMessageStorage', ['$location', function($location) {
    return {
        put: function (name, value) {},
        get: function (name) {
            return $location.search()['langCode']
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