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
          "comn.service.auth",
          "comn.service.user",
          "comn.service.message",
          "service.kendo.data",
          "directives.kendo.menu"
          ])
.config(["$stateProvider", "$urlRouterProvider", "$httpProvider", "$locationProvider", "constants",
         function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, constants) {
    
    //ui-router 
    $urlRouterProvider.otherwise("/");
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
        url : 'partial/:id', //constants.programUrl,
        templateUrl : function(urlAttr){
            angular.injector
            console.log(urlAttr);
            return "partial/app/resource.html";
        },
        controller : ["$scope", function($scope){var vm = this; vm.id = "id"}],
        controllerAs: "vm"
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
    
}])
.run(["$rootScope", "$injector", "Authentication", "constants", function($rootScope, $injector, Authentication, constants){
    Authentication.init();
    
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
    loginUrl: '/',
    main: 'main',
    mainUrl: '/main',
    program: 'program',
    programUrl: '/program/:id',
    findPassword: 'find',
    findPasswordUrl: '/find'
});