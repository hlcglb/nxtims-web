/*
 * Message Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.message',['comn.service.resource', 'pascalprecht.translate', 'ngSanitize'])
.config(['$translateProvider', function ($translateProvider) {
    //manually get $cookies
    var $cookies;
    angular.injector(['ngCookies']).invoke(['$cookies', function(_$cookies_) {
        $cookies = _$cookies_;
    }]);
    //tranlate 설정
    $translateProvider.useUrlLoader('/api/login/message');
    $translateProvider.useStorage('UrlMessageStorage');
    $translateProvider.preferredLanguage($cookies.get("language"));
    $translateProvider.fallbackLanguage($cookies.get("language"));
    //tranlate security
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
.factory('MessageService', ['$filter', function($filter) {
    function parametersMapping(list, str){
        for(var i in list){
            var regExp = new RegExp('\\{'+i+'\\}',"gim");
            str = str.replace(regExp, list[i]);
        }
        return str;
    }
    return {
        get: function(name, parameters){
            var message = $filter('translate')(name)
            if(parameters && angular.isObject(parameters)) return parametersMapping(parameters, message);
            else return message;
        }
    };
}])
.filter('imstMessage', [function(){
    
}])
.directive('imsMessage', ['MessageService', function (MessageService) {
    return{
        restrict: 'A',
        link: function($scope, element, attrs, ctrl){
            var key = attrs.imsMessage;
            var parameters = attrs.imsMessageValues;
            if(key){
                var message = MessageService.get(key, angular.fromJson(parameters));
                (element[0].tagName == "INPUT" || element[0].tagName == "TEXTAREA") 
                ? angular.element(element).val(message) : angular.element(element).text(message);
            }
        }
    }

}]);