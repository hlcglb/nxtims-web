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
          "nxtIms.home"
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
        controller : ["$scope", "$injector", "$state", function($scope, $injector, $state){var vm = this; vm.id = "progrmaId"}],
        controllerAs: "vm"
    });
    $locationProvider.html5Mode({
        enabled: true 
    });
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
    //LocaleProvider.init();
    
}])
.run(["$rootScope", "$injector", "Authentication", "Locale", "constants", 
      function($rootScope, $injector, Authentication, Locale, constants){
    Authentication.init();
    Locale.init("en-us");
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