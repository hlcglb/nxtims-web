/*
 * 프로그램 모듈 js
 *
 * @author 정철균
 */

'use strict';

var programModule = angular.module("programModule",[]);
angular.module("programModule")
.config(["$controllerProvider", function($controllerProvider){
    //동적 컨트롤 설정
    programModule.controller = function(name, constructor){
        $controllerProvider.register(name, constructor);
        return (this);
    }
}]);
angular.module("programModule")
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

}]);
/**
 * 동적 컨트롤러 바인딩 directive
 */
angular.module("programModule")
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
}]);
angular.module("programModule")
.directive('programLocation', [function() {
    return {
        restrict: 'A',
        template: '<dl><dt>현위치</dt><dd program-path></dd></dl>'
    };
}]);
angular.module("programModule")
.directive('programPath', [function() {
    return {
        restrict: 'A',
        
        controller: ['Function', 'UserService', 'ProgramInfo', function(Function, UserService, ProgramInfo){
            var ctrl = this;
            ctrl.prgmPath = "";
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
            });
            var length = path.length;
            for(var i=0; i < length ; i++){
                (i == length-1) ? ctrl.prgmPath += "<strong>" + path.pop() + "[" + currPrgmId + "]<strong>" : ctrl.prgmPath += path.pop() + "<span>&gt;</span>";
            }
        }],
        link: function(scope, element, attrs, controller){
            element.html(controller.prgmPath);
        }
    };
}]);

/**
 * 공통 코드 컴포넌트
 * 
 * @require @attribute {String} code - service name
 * @attribute {String} text @default 'text'
 * @attribute {String} value @default 'value'
 * @attribute {String} property @default null
 */
angular.module("programModule")
.component('commonCodeTest', {
    template: '<select kendo-drop-down-list k-options="$ctrl.options"></select>',
    bindings: {
        code: '@',
        text: '@',
        value: '@',
        property: '@'
    },
    controller: ['RESTfulService', function(RESTfulService){
        var ctrl = this; 
        ctrl.dataSource = {
            transport: {
                serverFiltering: true,
                read: function(options){
                    RESTfulService(ctrl.code).get({}
                    ,function success(data){
                        ctrl.property ? options.success(data[ctrl.property]) : options.success(data);
                    }
                    ,function error(data){
                        options.error(data);
                    });
                }
            }
        };
        ctrl.options = {
            dataSource: ctrl.dataSource,
            placeholder: "Select item",
            dataTextField: ctrl.text || "text",
            dataValueField: ctrl.value || "value"
        };
    }]
});
angular.module("programModule")
.component('commonCodeCombo', {
    template: '<select kendo-combo-box k-options="$ctrl.options"></select>',
    bindings: {
        code: '@',
        text: '@',
        value: '@',
        property: '@'
    },
    controller: ['ApiEvent', function(ApiEvent){
        var ctrl = this; 
        var codeApi = new ApiEvent("/api/app/code/:codeMstCd");
        ctrl.dataSource = {
            transport: {
                read: function(options){
                    codeApi.get({codeMstCd: ctrl.code}
                    ,function success(response){
                        var data = angular.fromJson(angular.toJson(response));
                        console.log(data);
                        ctrl.property ? options.success(data[ctrl.property]) : options.success(data);
                    }
                    ,function error(data){
                        options.error(data);
                    });
                }
            }
        };
        ctrl.options = {
            dataSource: ctrl.dataSource,
            placeholder: "Select item",
            dataTextField: ctrl.text || "text",
            dataValueField: ctrl.value || "value"
        };
    }]
});