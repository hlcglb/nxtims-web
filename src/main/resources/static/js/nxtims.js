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
.config(["$stateProvider", "$urlRouterProvider", "$httpProvider", "$locationProvider", "$translateProvider",
         "constants", "ProgramInfoProvider",
         function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider,
                 constants, ProgramInfoProvider) {
    ProgramInfoProvider.init();
    
    //ui-router 
    $urlRouterProvider.otherwise(constants.login);
    var stateNav = {
        templateUrl : constants.layoutTemplUrl + "navigation.html",
        resolve: {
            User: ["ResourceService", "Authentication", "UserService",
                   function(ResourceService, Authentication, UserService){
                if(Authentication.isAuth()){
                    return ResourceService.getPromise()
                    .then(
                            function(resource){
                                UserService.setResource(resource);
                                return UserService.getUser();
                            }, function(data){return null;})
                    .catch(function(error){
                        console.log("[#catch] nav resolved user resource : " + error);
                        throw error;
                    });
                }
            }]
        },
        controller : "NavigationController",
        controllerAs: "nav"
    }
    var stateFoot = {
        templateUrl : constants.layoutTemplUrl + "message.html",
        resolve: {
            Message: ["ResourceService", "Authentication", "UserService",
                   function(ResourceService, Authentication, UserService){
                
            }]
        },
        controller : [function(){
            
        }],
        controllerAs: "message"
    }
    $stateProvider
    .state(constants.login, {
        url : constants.loginUrl,
        templateUrl : constants.comnTemplUrl + "login.html",
        controller : "LoginController",
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
            },
            "message@main": stateFoot
        }
    })
    .state("program", {
        url : constants.programUrl,
        views: {
            "": {
                templateUrl : constants.layoutTemplUrl + "prgmLayout.html"/*,
                controller : ["$scope", function($scope){
                    var ctrl = this;
                    ctrl.id = ProgramInfoProvider.getId();
                    ctrl.search = function(){
                        $scope.$broadcast("imsProgramSearch", {id: ctrl.id});
                    }
                }],
                controllerAs: "application"*/
            },
            "navigation@program": stateNav,
            "application@program": {
                templateUrl : function(urlAttr){
                    console.log(urlAttr.viewUrl);
                    return constants.prgmTemplUrl + urlAttr.viewUrl + ".html";
                }
            },
            "message@program": stateFoot
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
    programUrl: '/view/{viewUrl:.*}',
    findPassword: 'find',
    findPasswordUrl: '/find'
})
.constant('Login.CookieKey', 'NXTIMS_SAVE_ID') //아이디 저장 cookie key
.controller("LoginController",["$state", "Authentication", "constants", "$kWindow", "$cookies", "Login.CookieKey", "$timeout",
                            function($state, Authentication, constants, $kWindow, $cookies, CookieKey, $timeout) {

    var self = this;
    self.credentials = {};
    self.buttonDisabled = false; //button diabled
    /*
     * 아이디 저장 쿠키 존재여부 확인 후 셋팅
     */
    if($cookies.get(CookieKey)){
        self.credentials.username = atob($cookies.get(CookieKey));
        self.saveId = true;
    }

    //login
    self.login = function(event) {
        self.buttonDisabled = true;
        Authentication.login(self.credentials)
        .then(
                function(message){
                    self.error = false;
                    console.log(message);
                    if(self.saveId != $cookies.get(CookieKey)){
                        // 쿠키가 없고 아이디저장 체크된 상태이면 쿠키 7일간 저장
                        var now = new Date();
                        now.setDate(now.getDate() + 7);
                        $cookies.put(CookieKey, btoa(self.credentials.username), {expires: now});
                    }
                    $state.go(constants.main);
                }, 
                function(data){
                    console.log(data);
                    self.error = true;
                    self.errorData = data;
                    console.log(self);
                    self.loginNotify.show(data.message/*kValue: data.message*/, "error"/*"ngTemplate"*/);
                }
        ).catch(function(error){
            console.log("[#catch] " + error);
        })
        .finally(function(){
            // 로그인 버튼 2번 클릭 방지
            $timeout(function(){
                self.buttonDisabled = false;
            },500);
            
        });
    };
    
    //password 찾기
    self.find = function(){
        var windowInstance = $kWindow.open({
            options:{
                modal: true,
                title: "find Password",
                resizable: true,
                height: 150,
                width: 400,
                visible: false
            },
            windowTemplateUrl: constants.popTemplUrl + "window.html",
            templateUrl: constants.popTemplUrl + "commonPopup.html",
            controller: ["$scope", "$windowInstance", "message", function($scope, $windowInstance, message){
                $scope.message = message;
                $scope.$windowInstance = $windowInstance;
            }],
            resolve: {
                message: function () {
                    return "password find"
                }
            }
        });
        windowInstance.result.then(function (result) {
            if (result) {
                self.result = "confirmed!";
            }
            else{
                self.result = "canceled!";
            }
        });
    };
    
    // container tracking
    self.tracking = function(){
        var windowInstance = $kWindow.open({
            options:{
                modal: true,
                title: "find Password",
                resizable: true,
                height: 150,
                width: 400,
                visible: false
            },
            windowTemplateUrl: constants.popTemplUrl + "window.html",
            templateUrl: constants.popTemplUrl + "commonPopup.html",
            controller: ["$scope", "$windowInstance", "message", function($scope, $windowInstance, message){
                $scope.message = message;
                $scope.$windowInstance = $windowInstance;
            }],
            resolve: {
                message: function () {
                    return self.tracker;
                }
            }
        });
        windowInstance.result.then(function (result) {
            if (result) {
                self.result = "confirmed!";
            }
            else{
                self.result = "canceled!";
            }
        });
    };
    
    //
    self.notifyOptions = {
                           templates: [{
                               type: "ngTemplate",
                               template: '<p style="width: 16em; padding:1em;white-space:nowrap"> {{ngValue}}, #= kValue # </p>'
                           }]
                       };
}]);
angular.module("notify", [])
.factory("Notification",[function(){
    var alert = function(message){
        
    }
    return {
        
    }
}]);