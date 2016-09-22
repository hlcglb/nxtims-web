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
          "kendo.window",
          "comn.service.resource",
          "comn.service.auth",
          "comn.service.user",
          "comn.service.message",
          "nxtims.services",
          "nxtims.directives",
          "nxtims.components",
          "nxtims.filters",
          "nxtIms.navigation",
          "nxtIms.home",
          "programModule"
          ])
.config(["$stateProvider", "$urlRouterProvider", "$httpProvider", "$locationProvider", 
         "constants", "ProgramInfoProvider",
         function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, 
                 constants, ProgramInfoProvider) {
    ProgramInfoProvider.init();
    //ui-router 
    $urlRouterProvider.otherwise(constants.login);
    var stateNav = {
        templateUrl : constants.comnTemplUrl + "navigation.html",
        resolve: {
            Menu: ["ResourceService", "Authentication", "UserService",
                   function(ResourceService, Authentication, UserService){
                if(Authentication.isAuth()){
                    return ResourceService.getPromise()
                    .then(
                            function(resource){
                                UserService.setResource(resource);
                                return UserService.getMenuList();
                            }, function(data){return null;})
                    .catch(function(error){
                        console.log("[#catch] main resolved user resource : " + error);
                        throw error;
                    });
                }
            }]
        },
        controller : "NavigationController",
        controllerAs: "nav"
    }
    $stateProvider
    .state(constants.login, {
        url : constants.loginUrl,
        templateUrl : constants.comnTemplUrl + "login.html",
        controller : "NavigationController",
        controllerAs: "controller"
    })
    
    .state("main", {
        url : constants.mainUrl,
        views: {
            "": {templateUrl : constants.layoutTemplUrl + "layout.html"},
            "navigation@main": stateNav,    
            "application@main": {
                templateUrl : constants.comnTemplUrl + "home.html",
                controller : "MainController",
                controllerAs: "controller"
            }
        }
    })

    .state("program", {
        url : constants.programUrl,
        views: {
            "": {templateUrl : constants.layoutTemplUrl + "prgmLayout.html"},
            "navigation@program": stateNav,
            "application@program": {
                templateUrl : function(urlAttr){
                    console.log(urlAttr.programUrl);
                    return constants.prgmTemplUrl + urlAttr.programUrl + ".html";
                },
                controller : ["$scope", "$injector", "$state", "UserService",
                              function($scope, $injector, $state, UserService){
                    var ctrl = this;
                    ctrl.id = ProgramInfoProvider.getId();
                }],
                controllerAs: "application"
            }
        }
    });
    $locationProvider.html5Mode({
        enabled: true
    });
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
    
    
    
    
}])
.run(["$rootScope", "$injector", "Authentication", "constants",
      function($rootScope, $injector, Authentication, constants){
    Authentication.init();
    // stateChange evnet
    $rootScope.$on("$stateChangeStart", function(event, toState, toParams, fromState, fromParams, options) {
        //인증된 사용자 메인으로 리다이렉트
        if(toState.name == constants.login && Authentication.isAuth()){
            console.log("login state change");
            $injector.get("$state").go(constants.main);
            event.preventDefault();
        }else if(toState.name != constants.login && !Authentication.isAuth()){
            console.log("auth false - go login");
            $injector.get("$state").go(constants.login);
            event.preventDefault();
        }
    });
    console.log("nxtIms run!!!");
}])
.constant("constants", {
    baseUrl: '/',
    templateUrl: '/partial/',
    layoutTemplUrl: '/partial/common/layout/',
    comnTemplUrl: '/partial/common/',
    prgmTemplUrl: '/partial/',
    popTemplUrl: '/partial/common/popup/',
    login: 'login',
    loginUrl: '/login',
    main: 'main',
    mainUrl: '/main',
    programUrl: '/program/{programUrl:.*}',
    findPassword: 'find',
    findPasswordUrl: '/find'
});

angular.module("notify", [])
.factory("Notification",[function(){
    var alert = function(message){
        
    }
    return {
        
    }
}]);