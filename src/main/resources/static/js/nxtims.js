angular.module("nxtIms", 
        [ "ngRoute",
          "ui.router",
          "comn.service.data",
          "comn.service.auth",
          "kendo.directives"])
.config(["$stateProvider", "$urlRouterProvider", "$httpProvider", 
         function($stateProvider, $urlRouterProvider, $httpProvider) {
    $urlRouterProvider.otherwise("/");
    $stateProvider
    .state("login", {
        url : "/",
        templateUrl : "partial/login.html",
        controller : "NavigationController",
        controllerAs: "controller"
    })
    .state("main", {
        url : "/main",
        templateUrl : "partial/home.html",
        resolve: {
            User: function(comnDataService){
                console.log("resolve"); 
                return comnDataService().get({service: "resource"}).$promise.then().catch(function(error){throw error;});
            }
        },
        controller : "MainController",
        controllerAs: "controller",
    })
    .state("program", {
        url : "/program/:id",
        templateUrl : function(param){
            return "partial/home.html";
        },
        controller : function(param){
            return "controller"
        },
        controllerAs: "controller",
    });
    
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
}]).controller("NavigationController",["$rootScope", "$http", "$location", "$state", "Authenticate",
                            function($rootScope, $http, $location, $state, Authenticate) {
    console.log("navigation controller");
    var self = this;
    self.credentials = {};
    
    self.login = function() {
        console.log("login");
        Authenticate.login(self.credentials)
        .then(
                function(message){
                    console.log("Login succeeded");
                    console.log(message);
                    
                    //$location.path("/home");
                    $state.go("main");
                    self.error = false;
                }, 
                function(message){
                    console.log("Login failed");
                    console.log(message);
                    
                    //$location.path("/login");
                    $state.go("login");
                    self.error = true;
                }
        ).catch(function(error){
            console.log(error);
        });
    };    
}])
.controller("MainController", ["$rootScope", "$state", "Authenticate", "User",
                     function($rootScope, $state, Authenticate, User) {
    var self = this;
    console.log("home controller");
    console.log(User);
    console.log($rootScope);
    console.log(Authenticate.isAuthenticat());

    self.logout = function() {
        Authenticate.logout().finally(function() {
            $state.go("login");
        });
    }
    
    self.openNav = function(){
        angular.element("#programMenu").css("width","250px");
    }
    self.closeNav = function(){
        angular.element("#programMenu").css("width", "0");
    }
}]);