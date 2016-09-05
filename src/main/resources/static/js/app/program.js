/*
 * 프로그램 모듈 js
 *
 * @author 정철균
 */

'use strict';

var programModule = angular.module("programModule",[])
.config(["$controllerProvider", function($controllerProvider){
    programModule.controller = function(name, constructor){
        $controllerProvider.register(name, constructor);
        return (this);
    }
}])
.directive('dynamicController', ['$compile', '$parse', function($compile, $parse) {
    return {
        restrict: 'A',
        terminal: true,
        priority: 100000,
        /*compile: function(tElement, tAttrs, transclude) {
            //return function postLink(scope, iElement, iAttrs, controller){
                console.log(tAttrs);
                
                var name = tAttrs.dynamicController;
                tElement.removeAttr("dynamic-controller");
                tElement.attr("ng-controller", name);
                //$compile($element)($scope);
            //}
        }*/
        link: function(scope, element, attrs, controller){
            var name = attrs.dynamicController;
            (element.attr("dynamic-controller")) ? 
                    element.removeAttr("dynamic-controller") : element.removeAttr("data-dynamic-controller");
            element.attr("ng-controller", name);
            (element.attr("dynamic-controller")) || (element.attr("data-dynamic-controller")) ?  
                    angular.noop() : $compile(element)(scope);
        }
    };
}]);