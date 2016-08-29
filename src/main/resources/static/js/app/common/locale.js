/*
 * Locale Module define js
 *
 * @author 정철균
 */

'use strict';


angular.module('comn.service.locale',[]).config([function(){
    console.log('comn.service.locale config!!');
}])
.run(function(){
    console.log('comn.service.Locale run!!');
})
.provider('Locale',[function() {
    var localeUrlPattern = 'angularjs/i18n/angular-locale_{{locale}}.js';
    
    this.setLocaleUrlPattern = function(value){
        if(value){
            localeUrlPattern = value;
        }
    };
    //scpit 생성
    function loadScript(url, callback, errorCallback, $timeout) {
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
                        callback();
                    }, 30, false);
                }
            };
        }
        else { // Others
            script.onload = function() {
                if(removed) return;
                removed = true;
                element.removeChild(script);
                callback();
            };
            script.onerror = function() {
                if(removed) return;
                removed = true;
                element.removeChild(script);
                errorCallback();
            };
        }
        script.src = url;
        script.async = true;
        element.appendChild(script);
    }
    

    this.$get = ['$timeout', function($timeout){
        loadScript();
    }];
    
    return{

    }


}])
.directive('', ['$window', function ($window) {
    return{
        link: function($scope, element, attrs, ctrl){
            
        }
    }

}]);