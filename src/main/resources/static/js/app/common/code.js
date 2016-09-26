/*
 * Code Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.code',['comn.service.resource'])
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
.factory('CodeService', ['$location', function($location) {
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
            console.log(attrs);
        }
    }

}]);