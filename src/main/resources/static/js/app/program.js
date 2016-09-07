/*
 * 프로그램 모듈 js
 *
 * @author 정철균
 */

'use strict';

var programModule = angular.module("programModule",[])
.config(["$controllerProvider", function($controllerProvider){
    //동적 컨트롤 설정
    programModule.controller = function(name, constructor){
        $controllerProvider.register(name, constructor);
        return (this);
    }
}])
.factory('Function',[function(){
    var transform = function(object, handler) {
        if(jQuery.isPlainObject(object) || angular.isArray(object)) {
            for( var key in object) {
                var value = object[key];
                if(angular.isFunction(handler)) {
                    handler(object, key, value);
                }
                transform(value, handler);
            }
        }
    };
    var isEmpty = function(object) {
        if(object == null || object == "") {
            return true;
        }
        return false;
    }

    return {
        transform: transform,
        isEmpty: isEmpty
    };

}])
/**
 * 동적 컨트롤러 바인딩 directive
 */
.directive('dynamicController', ['$compile', '$parse', function($compile, $parse) {
    return {
        restrict: 'A',
        terminal: true,
        priority: 100000,
        link: function(scope, element, attrs, controller){
            var name = attrs.dynamicController;
            (element.attr("dynamic-controller")) ? 
                    element.removeAttr("dynamic-controller") : element.removeAttr("data-dynamic-controller");
            element.attr("ng-controller", name);
            (element.attr("dynamic-controller")) || (element.attr("data-dynamic-controller")) ?  
                    angular.noop() : $compile(element)(scope);
        }
    };
}])
.directive('programPath', [function() {
    return {
        restrict: 'A',
        controller: ['Function', 'UserService', 'ProgramInfo', function(Function, UserService, ProgramInfo){
            var ctrl = this;
            var path = [];
            var menu = UserService.getMenuList();
            var currPrgmId = ProgramInfo.getId();
            var parentId = "";
            Function.transform(menu, function(object, key, value){
                if(key == "RESOURCE_ID" && value == currPrgmId) {
                    path.push(object.RESOURCE_NM);
                    parentId = object.PARENT_ID;
                }
                else if(object.RESOURCE_ID == parentId){
                    path.push(object.RESOURCE_NM);
                    parentId = object.PARENT_ID;
                }
            })
            var length = path.length;
            console.log(length);
            for(var i=0; i < length ; i++){
                (i == length-1) ? ctrl.prgmPath += "<strong>" + path.pop() + "[" + currPrgmId + "]<strong>" : ctrl.prgmPath += path.pop() + "<span>&gt;</span>";
            }
        }],
        link: function(scope, element, attrs, controller){
            element.html(controller.prgmPath);
        }
    };
}]);