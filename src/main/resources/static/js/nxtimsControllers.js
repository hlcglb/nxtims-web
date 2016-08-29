/*
 * 메인 app 모듈 Controllers js
 *
 * @author 정철균
 */

'use strict';

angular.module("nxtIms")
.controller("NavigationController",["$state", "Authentication", "constants",
                            function($state, Authentication, constants) {
    console.log("navigation controller");
    var self = this;
    self.credentials = {};
    self.login = function() {
        Authentication.login(self.credentials)
        .then(
                function(message){
                    console.log(message);
                    $state.go(constants.main);
                    self.error = false;
                }, 
                function(message){
                    console.log(message);
                    $state.go(constants.login);
                    self.error = true;
                }
        ).catch(function(error){
            console.log("[#catch] " + error);
        });
    };
}])
.controller("MainController", 
        ["$rootScope", "$state", "Authentication", "Menu", "KendoDataHelper", "ResourceService", "constants",
         function($rootScope, $state, Authentication, Menu, KendoDataHelper, ResourceService, constants) {
    var self = this;
    
    self.authenticated = Authentication.isAuth();
    console.log("home controller");
    if(Menu){
        self.treelist = KendoDataHelper.toKendoData(angular.copy(Menu), "tree");
    }
    self.logout = function() {
        Authentication.logout()
        .then(
                function(message){
                    console.log(message);
                }, function(message){
                    console.log(message);
                })
        .finally(function() {
            console.log("!-- go login");
            $state.go(constants.login);
        });
    }
    //menu navi
    self.openNav = function(){
        angular.element("#programMenu").css("width","250px");
    }
    self.closeNav = function(){
        angular.element("#programMenu").css("width", "0");
    }
    self.program = [];
    self.test = "test Text";
}]);