/*
 * Locale Module define js
 *
 * @author 정철균
 */

'use strict';


angular.module('comn.service.locale',[])
.provider('Locale',[function() {
    var localeUrlPattern = '/js/angularjs/i18n/angular-locale_{{locale}}.js';
    var nodeToAppend;
    
    this.setNodeToAppend = function(node){
        nodeToAppend = node;
    };
    
    this.setLocaleUrlPattern = function(value){
        if(value){
            localeUrlPattern = value;
        }
    };
    //scpit 생성
    function loadScript(url, localeId, $locale, $timeout, callback, errorCallback)  {
        var script = document.createElement('script'),
        element = nodeToAppend ? nodeToAppend : document.getElementsByTagName("body")[0],
        removed = false;
        script.type = 'text/javascript';
        if(script.readyState) { // IE
            script.onreadystatechange = function() {
                if(script.readyState === 'complete' || script.readyState === 'loaded') {
                    script.onreadystatechange = null;
                    $timeout(function() {
                        if(removed) return;
                        removed = true;
                        element.removeChild(script);
                        (callback || angular.noop)();
                    }, 30, false);
                }
            };
        }
        else { // Others
            script.onload = function() {
                if(removed) return;
                removed = true;
                element.removeChild(script);
                (callback || angular.noop)();
            };
            script.onerror = function() {
                if(removed) return;
                removed = true;
                element.removeChild(script);
                (errorCallback || angular.noop)();
            };
        }
        script.src = url;
        script.async = true;
        script.dataType = "script",
        //element.appendChild(script);
        angular.element(element).prepend(script);

    }
    

    this.$get = ['$timeout', '$locale', '$interpolate', function($timeout, $locale, $interpolate){
        var url = $interpolate(localeUrlPattern)({locale: $locale.id});
        console.log(url);
        var that = this;
        var init = function(localeId){
            url = $interpolate(localeUrlPattern)({locale: localeId});
            console.log(url);
            loadScript(url, localeId, $locale, $timeout);
        }
        return {
            init : init
        }
    }];
}]);