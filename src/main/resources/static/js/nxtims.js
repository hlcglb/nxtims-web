/*
 * 메인 app 모듈을 정의 하는 js
 *
 * @author 정철균
 */

'use strict';

angular.module("nxtIms", 
        [ "ngRoute",
          "ui.router",
          "kendo.directives",
          "comn.service.resource",
          "comn.service.locale",
          "comn.service.auth",
          "comn.service.user",
          "comn.service.message",
          "service.kendo.data",
          "directives.kendo.menu",
          "nxtIms.login",
          "nxtIms.home",
          "programModule"
          ])
.config(["$stateProvider", "$urlRouterProvider", "$httpProvider", "$locationProvider", "LocaleProvider", "constants",
         function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, LocaleProvider, constants) {
    //ui-router 
    $urlRouterProvider.otherwise(constants.login);
    $stateProvider
    .state(constants.login, {
        url : constants.loginUrl,
        templateUrl : constants.templateUrl + "/login.html",
        controller : "NavigationController",
        controllerAs: "controller"
    })
    .state(constants.main, {
        url : constants.mainUrl,
        templateUrl : constants.templateUrl + "/home.html",
        resolve: {
            Menu: ["ResourceService", "Authentication", "UserService",
                   function(ResourceService, Authentication, UserService){
                
                console.log("resolve main");
                if(Authentication.isAuth()){
                    return ResourceService.getPromise()
                    .then(
                            function(resource){
                                UserService.setUser(resource.USER);
                                UserService.setMenuList(resource.MENU_LIST);
                                //resource["LOCALE"] = "ko-kr";
                                return UserService.getMenuList();
                            }, function(data){return null;})
                    .catch(function(error){
                        console.log("[#catch] main resolved user resource : " + error);
                        throw error;
                    });
                }
                
            }]
        },
        controller : "MainController",
        controllerAs: "controller",
    })
    .state(constants.program, {
        url : constants.programUrl,
        templateUrl : function(urlAttr){
            return constants.templateUrl + "/app/" + urlAttr.id +".html";
        },
        controller : ["$scope", "$injector", "$state", "$controller", "$window", "$element",
                      function($scope, $injector, $state, $controller, $window, $element){
            // template script controller bind
            if($window.controller){
                // function apply
                //$window.controller.apply(this, arguments);
            }
            var vm = this;
            vm.id = "progrmaId";
            /*console.log(programModule.mod.controller("tt", function(){}));*/
            console.log($state.$current.locals["@"].$element[0]);
            console.log($element);
            var element =$state.$current.locals["@"].$element[0];
            /*console.log(angular.module("program")._invokeQueue[0][2][1]);
            
            var controller = $controller(angular.module("program")._invokeQueue[0][2][1], {
                "$scope": $scope,
                "$element": angular.element(".content")
            });*/
            //$injector.invoke(module, this, { "$scope": $scope });
            /*angular.element(".content").data("$ngControllerController", controller)
            angular.element(document).ready(function() {
                console.log("ready in ui-router controller");
                //console.log(angular.module("program"));
                
            });*/
        }],
        controllerAs: "program"
    });
    $locationProvider.html5Mode({
        enabled: true 
    });
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
    //LocaleProvider.init();
    
}])
/*.decorator("$controller", function ($delegate) {
    return function (constructor, locals) {
        var controller = $delegate.apply(null, arguments);

        return angular.extend(function () {
            locals.$scope.controllerName = locals.$$controller;
            console.log(locals);
            return controller();
        }, controller);
    };
})*/
/*.config(["$provide", function ($provide) {
    $provide.decorator("$controller", [ "$delegate",function ($delegate) {
            return function(constructor, locals) {
                console.log(locals);
                if (typeof constructor == "string") {
                    //locals.$scope.controllerName =  constructor;
                }
                return $delegate(constructor, locals);
            }
        }]);
}])*/
.run(["$rootScope", "$injector", "Authentication", "Locale", "constants", 
      function($rootScope, $injector, Authentication, Locale, constants){
    Authentication.init();
    //Locale.init("en-us");
    // stateChange evnet
    $rootScope.$on("$stateChangeStart", function(event, toState, toParams, fromState, fromParams, options) {
        //console.log(event);
        //console.log(toState);
        //console.log(toParams);
        //console.log(fromState);
        //console.log(fromParams);
        //console.log(options);
        /*var $http = $injector.get("$http");
        console.log($http.defaults.headers.common);*/
        
        //인증된 사용자 메인으로 리다이렉트
        if(toState.name == constants.login && Authentication.isAuth()){
            console.log("logind state change");
            $injector.get("$state").go(constants.main);
            event.preventDefault();
        }
    });
    console.log("nxtIms run!!!");
}])
.constant("constants", {
    baseUrl: '/',
    templateUrl: '/partial',
    login: 'login',
    loginUrl: '/login',
    main: 'main',
    mainUrl: '/main',
    program: 'program/:id',
    programUrl: '/program/:id',
    findPassword: 'find',
    findPasswordUrl: '/find'
});

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
            /*terminal: true,*/
            priority: 100000,
            link: function($scope, $element) {
                console.log($element);
                var name = $parse($element.attr("dynamicController"))($scope);
                $element.removeAttr("dynamicController");
                $element.attr("ng-controller", name);
                $compile($element)($scope);
            }
        };
    }]);;
